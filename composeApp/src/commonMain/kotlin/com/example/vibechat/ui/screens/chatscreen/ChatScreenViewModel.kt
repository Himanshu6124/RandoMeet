package com.example.vibechat.ui.screens.chatscreen

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.example.vibechat.constants.CONSTANTS.MATCHED_CONVERSATION
import com.example.vibechat.constants.CONSTANTS.USER_ID
import com.example.vibechat.core.BaseViewModel
import com.example.vibechat.data.model.repository.ChatRepo
import com.example.vibechat.socket.SocketRepository
import com.example.vibechat.ui.screens.chatscreen.components.DisconnectStatus
import com.example.vibechat.ui.screens.chatscreen.components.Message
import com.example.vibechat.ui.screens.chatscreen.components.OnlineStatus
import com.example.vibechat.ui.screens.chatscreen.components.TypingStatus
import com.example.vibechat.ui.screens.matchscreen.Conversation
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json

class ChatScreenViewModel(
    private val chatRepository: ChatRepo,
    private val socketRepository: SocketRepository,
    private val savedStateHandle: SavedStateHandle,
    private val dataStore: DataStore<Preferences>
) : BaseViewModel<ChatUIState, ChatEvent, ChatSideEffect>() {

    override val initialState: ChatUIState
        get() = ChatUIState()

    private var typingJob: Job? = null
    private var hasSentTypingStatus = false
    private val typingDelayMillis = 1000L
    init {
        observeIncomingMessages()
        observeTypingStatus()
        observeOnlineStatus()
        observeDisconnectedStatus()
    }

    override fun handleEvent(event: ChatEvent) {
        when (event) {
            is ChatEvent.SendMessage -> {
                    sendMessage(message = event.message, isRandom = event.isForRandomMatching)
            }

            ChatEvent.DisconnectSocket -> disconnectSocket()
            is ChatEvent.InitState -> iniState(event.conversation)
        }
    }

    fun iniState(conversation : Conversation?) {
        if (conversation == null) return
        viewModelScope.launch {
            _uiState.update {
                it.copy(conversation = conversation)
            }
            connectToSocket()
            sendOnlineStatus()
        }
    }

    private fun observeTypingStatus(){
        viewModelScope.launch {
            socketRepository.isTyping.collect { isTyping ->
                _uiState.update {
                    it.copy(isTyping = isTyping)
                }
            }
        }
    }

    private fun observeOnlineStatus(){
        viewModelScope.launch {
            socketRepository.onlineStatus.collect { isOnline ->
                _uiState.update {
                    it.copy(isOnline = isOnline)
                }
            }
        }
    }

    private fun observeIncomingMessages() {
        viewModelScope.launch {
            socketRepository.messages
                .filterNotNull()   // VERY IMPORTANT
                .collect { newMessage ->
                    addMessage(newMessage)
                }

        }
    }

    fun addMessage(message: Message) {
        viewModelScope.launch {
            _uiState.update {
                val messages = it.messages.toMutableList().apply {
                    add(message)
                }
                it.copy(messages = messages)
            }
            _effect.emit(ChatSideEffect.ScrollToBottom(uiState.value.messages.lastIndex))
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

    fun connectToSocket() {
        viewModelScope.launch {
            val userId = dataStore.data.first()[USER_ID]
            val friendUserId = _uiState.value.conversation.friendUserId
            val conversationId = _uiState.value.conversation.conversationId

            _uiState.update {
                it.copy(userId = userId)
            }

            println("Connecting to socket with userId: $userId and conversationId: $conversationId and friendUserId: $friendUserId")

            if (userId == null || friendUserId == null || conversationId == null) {
                return@launch
            }
            socketRepository.connect(userId = userId)
            socketRepository.subscribe(topic = "/topic/room/$friendUserId-$conversationId")
        }
    }

    private fun sendMessage(message: Message, isRandom: Boolean) {
        viewModelScope.launch {
            val destination = if (isRandom) "/app/chat.random.send" else "/app/chat.send"
            println("Sending message to $destination with message: $message")
            socketRepository.sendMessage(destination, message)
            _uiState.update {
                val messages = it.messages.toMutableList().apply {
                    add(message)
                }
                it.copy(messages = messages)
            }
            _effect.emit(ChatSideEffect.ScrollToBottom(uiState.value.messages.lastIndex))
        }
    }

    fun sendOnlineStatus() {
        viewModelScope.launch {
            val userId = dataStore.data.first()[USER_ID]
            val conversationId = _uiState.value.conversation.conversationId
            println("Sending online status with userId: $userId and conversationId: $conversationId")
            if (conversationId == null)
                return@launch

            val onlineStatus = OnlineStatus(senderId = userId, conversationId = conversationId, online = true)
            socketRepository.sendMessage("/app/chat.online", onlineStatus)
        }
    }

    fun getFriendStatus() {
        viewModelScope.launch {
            val conversationId = _uiState.value.conversation.conversationId
            val friendId = _uiState.value.conversation.friendUserId
            println("getting online status with frined: $friendId and conversationId: $conversationId")
            if (conversationId == null)
                return@launch

            val onlineStatus = OnlineStatus(senderId = friendId, conversationId = conversationId)
            socketRepository.sendMessage("/app/chat.user.status", onlineStatus)
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
        disconnectSocket()
    }

    fun observeDisconnectedStatus(){
        viewModelScope.launch {
            socketRepository.disconnectedUserName.collect { disconnectedUserName ->
                _effect.emit(ChatSideEffect.NavigateToMatchScreen(disconnectedUserName))
            }
        }
    }

    fun disconnectSocketPermanently() {
        val disconnectedStatus = DisconnectStatus(
            conversationId = _uiState.value.conversation.conversationId,
            senderId = _uiState.value.conversation.friendUserName
        )
        socketRepository.sendMessage("/app/chat.disconnect", disconnectedStatus)
        disconnectSocket()

    }
    fun disconnectSocket(){
        socketRepository.disconnect()
    }
}