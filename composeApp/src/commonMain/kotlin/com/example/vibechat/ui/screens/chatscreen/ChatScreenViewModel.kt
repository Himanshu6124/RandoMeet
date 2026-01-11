package com.example.vibechat.ui.screens.chatscreen

import androidx.lifecycle.viewModelScope
import com.example.vibechat.core.BaseViewModel
import com.example.vibechat.data.model.repository.ChatRepo
import com.example.vibechat.socket.SocketRepository
import com.example.vibechat.ui.screens.chatscreen.components.Message
import com.example.vibechat.ui.screens.chatscreen.components.OnlineStatus
import com.example.vibechat.ui.screens.chatscreen.components.TypingStatus
import com.example.vibechat.ui.screens.matchscreen.ChatCardData
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ChatScreenViewModel(
    private val chatRepository: ChatRepo,
    private val socketRepository: SocketRepository
) : BaseViewModel<ChatUIState, ChatEvent, ChatSideEffect>() {

    override val initialState: ChatUIState = ChatUIState()

    private var typingJob: Job? = null
    private var hasSentTypingStatus = false
    private val typingDelayMillis = 1000L
    val message: StateFlow<Message?> = socketRepository.messages
    val isOnline: StateFlow<Boolean> = socketRepository.onlineStatus
    val isTyping: StateFlow<Boolean> = socketRepository.isTyping
    val chatCardData: StateFlow<ChatCardData> = socketRepository.chatCardData

    override fun handleEvent(event: ChatEvent) {
        when (event) {
            is ChatEvent.SendMessage -> {
                    sendMessage(message = event.message, isRandom = event.isForRandomMatching)
            }
        }
    }

    fun addMessage(message: Message) {
        val current = _uiState.value.messages.toMutableList()
        current.add(message)
        _uiState.update {
            it.copy(messages = current)
        }
    }

    fun getMessages(conversationId: String) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)
            try {
                val messages = chatRepository.getMessages(conversationId)
                _uiState.update {
                    it.copy(messages = messages, isLoading = false)
                }
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        exception = e,
                        isLoading = false
                    )
                }
            }
        }
    }

    fun sendFriendRequest(userId: String, friendId: String, action: String = "send") {
        viewModelScope.launch {
            try {
                chatRepository.sendFriendRequest(userId, friendId, action)
            } catch (e: Exception) {

            }
        }
    }

    fun connectToSocket(friendUserId: String, conversationId: String, senderId: String) {
        viewModelScope.launch {
            socketRepository.connect(userId = senderId)
            socketRepository.subscribe(topic = "/topic/room/$friendUserId-$conversationId")
        }
    }

    private fun sendMessage(message: Message, isRandom: Boolean) {
        viewModelScope.launch {
            val destination = if (isRandom) "/app/chat.random.send" else "/app/chat.send"
            socketRepository.sendMessage(destination, message)
            _uiState.update {
                val messages = it.messages.toMutableList().apply {
                    add(message)
                }
                it.copy(messages = messages)
            }
            _effect.emit(ChatSideEffect.AppendMessage(message))
        }
    }

    fun sendOnlineStatus(senderId: String, conversationId: String) {
        viewModelScope.launch {
            val onlineStatus =
                OnlineStatus(senderId = senderId, conversationId = conversationId, online = true)
            socketRepository.sendMessage("/app/chat.online", onlineStatus)
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

    private fun sendTypingStatus(senderId: String, conversationId: String, isTyping: Boolean) {
        viewModelScope.launch {
            val typingStatus = TypingStatus(
                senderId = senderId,
                conversationId = conversationId,
                typing = isTyping
            )
            socketRepository.sendMessage("/app/chat.typing", typingStatus)
        }
    }

    override fun onCleared() {
        super.onCleared()
        socketRepository.disconnect()
    }
}