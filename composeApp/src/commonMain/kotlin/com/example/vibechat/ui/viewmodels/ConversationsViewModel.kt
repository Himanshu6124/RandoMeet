package com.example.vibechat.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.vibechat.data.model.ChatCardData
import com.example.vibechat.data.model.User
import com.example.vibechat.data.model.repository.ChatRepo
import com.example.vibechat.data.model.repository.SocketRepo
import com.example.vibechat.data.model.repository.UserDataStore
import com.example.vibechat.ui.viewmodels.uiStates.ConversationUIState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ConversationsViewModel : ViewModel() {

    private val chatRepository = ChatRepo()
    private val stompRepository = SocketRepo()
    private val _user = MutableStateFlow<User?>(null)
    val user: StateFlow<User?> = _user

    init {
        viewModelScope.launch {
//            _user.value = userDataStore.getUser()
        }
    }

    private val _uiState = MutableStateFlow(ConversationUIState())
    val uiState: StateFlow<ConversationUIState> = _uiState
    private val _conversation = stompRepository.chatCardData

    init {
        viewModelScope.launch {
            _conversation.collect { conversation ->
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        matchingConversation = conversation
                    )
                }
            }
        }
    }


    fun getConversations(userId: String) {
        viewModelScope.launch {
            _uiState.update { state ->
                state.copy(
                    isLoading = true
                )
            }

            try {
                val res = chatRepository.getConversations(userId) as ArrayList<ChatCardData>
                _uiState.update { state ->
                    state.copy(
                        conversations = res,
                        isLoading = false
                    )
                }
            } catch (e: Exception) {
                _uiState.update { state ->
                    state.copy(
                        isLoading = false
                    )
                }
            }
        }
    }

    fun connectToSocketAndSubscribe(userId: String) {
        viewModelScope.launch {
            stompRepository.connect(
                userId = userId,
            )
        }


        stompRepository.subscribe(
            topic = "/topic/room/random/$userId"
        )
//        startMatching(userId)
    }

    private fun startMatching(userId: String) {

        viewModelScope.launch {
            _uiState.update { state ->
                state.copy(
                    isLoading = true
                )
            }
            stompRepository.sendMessage("/app/chat.random", userId)
        }
    }
}