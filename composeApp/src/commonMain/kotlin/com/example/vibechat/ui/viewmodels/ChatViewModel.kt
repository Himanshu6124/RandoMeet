package com.example.vibechat.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.vibechat.data.model.ChatCardData
import com.example.vibechat.data.model.Message
import com.example.vibechat.data.model.OnlineStatus
import com.example.vibechat.data.model.TypingStatus
import com.example.vibechat.data.model.repository.ChatRepo
import com.example.vibechat.data.model.repository.SocketRepo
import com.example.vibechat.data.model.network.KtorClient.httpClient
import com.example.vibechat.ui.viewmodels.uiStates.ChatUIState
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch


class ChatViewModel(

) : ViewModel() {
    private val chatRepository: ChatRepo = ChatRepo(httpClient)
    private val stompRepository: SocketRepo = SocketRepo()
    private var typingJob: Job? = null
    private var hasSentTypingStatus = false
    private val typingDelayMillis = 1000L

    private val _uiState = MutableStateFlow(ChatUIState())
    val uiState: StateFlow<ChatUIState> = _uiState

    private val _isRequestLoading = MutableStateFlow(false)
    val isRequestLoading: StateFlow<Boolean> = _isRequestLoading

    private val _requestSuccess = MutableStateFlow<Boolean?>(null)
    val requestSuccess: StateFlow<Boolean?> = _requestSuccess

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

    val message: StateFlow<Message?> = stompRepository.messages
    val isOnline: StateFlow<Boolean> = stompRepository.onlineStatus
    val isTyping: StateFlow<Boolean> = stompRepository.isTyping
    val chatCardData: StateFlow<ChatCardData> = stompRepository.chatCardData

    /*** UI / Chat state methods ***/
    fun addMessage(message: Message) {
        val current = _uiState.value.messages.toMutableList()
        current.add(message)
        _uiState.value = _uiState.value.copy(messages = current)
    }

    fun getMessages(conversationId: String) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)
            try {
                val messages = chatRepository.getMessages(conversationId)
                _uiState.value = _uiState.value.copy(messages = messages, isLoading = false)
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(isLoading = false)
                _error.value = e.message
            }
        }
    }

    /*** Friend request ***/
    fun sendFriendRequest(userId: String, friendId: String, action: String = "send") {
        viewModelScope.launch {
            _isRequestLoading.value = true
            try {
                chatRepository.sendFriendRequest(userId, friendId, action)
                _requestSuccess.value = true
            } catch (e: Exception) {
                _requestSuccess.value = false
                _error.value = e.message
            } finally {
                _isRequestLoading.value = false
            }
        }
    }

    /*** Socket methods ***/
    fun connectToSocket(friendUserId: String, conversationId: String, senderId: String) {
        viewModelScope.launch {
            stompRepository.connect(userId = senderId)
            stompRepository.subscribe(topic = "/topic/room/$friendUserId-$conversationId")
        }
    }

    fun sendMessage(message: Message, isRandom: Boolean) {
        viewModelScope.launch {
            val destination = if (isRandom) "/app/chat.random.send" else "/app/chat.send"
            stompRepository.sendMessage(destination, message)
        }
    }

    fun sendOnlineStatus(senderId: String, conversationId: String) {
        viewModelScope.launch {
            val onlineStatus =
                OnlineStatus(senderId = senderId, conversationId = conversationId, online = true)
            stompRepository.sendMessage("/app/chat.online", onlineStatus)
        }
    }

    private fun sendTypingStatus(senderId: String, conversationId: String, isTyping: Boolean) {
        viewModelScope.launch {
            val typingStatus = TypingStatus(
                senderId = senderId,
                conversationId = conversationId,
                typing = isTyping
            )
            stompRepository.sendMessage("/app/chat.typing", typingStatus)
        }
    }

    fun getUserStatus(friendId: String, conversationId: String) {
        viewModelScope.launch {
            val status = OnlineStatus(senderId = friendId, conversationId = conversationId)
            stompRepository.sendMessage("/app/chat.user.status", status)
        }
    }

    fun onUserTyping(senderId: String, conversationId: String, inputText: String) {
        if (inputText.isNotEmpty()) {
            if (!hasSentTypingStatus) {
                hasSentTypingStatus = true
                sendTypingStatus(senderId, conversationId, true)
            }
            typingJob?.cancel()
            typingJob = viewModelScope.launch {
                delay(typingDelayMillis)
                hasSentTypingStatus = false
                sendTypingStatus(senderId, conversationId, false)
            }
        } else {
            sendTypingStatus(senderId, conversationId, false)
        }
    }

    /*** Cleanup ***/
    override fun onCleared() {
        super.onCleared()
        stompRepository.disconnect()
    }
}
