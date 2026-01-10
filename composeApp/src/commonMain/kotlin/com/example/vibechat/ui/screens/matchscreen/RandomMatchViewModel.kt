package com.example.vibechat.ui.screens.matchscreen

import androidx.lifecycle.viewModelScope
import com.example.vibechat.core.BaseViewModel
import com.example.vibechat.data.model.repository.ChatRepo
import com.example.vibechat.data.model.repository.SocketRepo
import com.example.vibechat.koin.DeviceInfo
import com.example.vibechat.socket.SocketRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class RandomMatchViewModel(
    private val deviceInfo: DeviceInfo,
    private val stompRepository: SocketRepository,
    private val chatRepo: ChatRepo
) : BaseViewModel<RandomMatchUIState, RandomMatchEvent, RandomMatchSideEffect>(){

    override val initialState: RandomMatchUIState
        get() = RandomMatchUIState()
    private val _conversation = stompRepository.chatCardData

    init {
        viewModelScope.launch {
            _conversation.collect { conversation->
                println("$TAG Conversation data is $conversation")
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        matchingConversation = conversation
                    )
                }
                _effect.emit(RandomMatchSideEffect.NavigateToChatScreen(conversation))
            }
        }
    }

    override fun handleEvent(event: RandomMatchEvent) {

        when(event) {
            is RandomMatchEvent.OnStartMatchClick ->{
                connectToSocketAndSubscribe()
            }
        }
    }

    fun connectToSocketAndSubscribe() {
        viewModelScope.launch {
            val userId = deviceInfo.getDeviceId()
            stompRepository.connect(
                userId = userId,
            )
            stompRepository.subscribe(
                topic = "/topic/room/random/$userId"
            )
            startMatching(userId)
        }

    }

    private fun startMatching(userId: String) {
        _uiState.update { state ->
            state.copy(
                isLoading = true
            )
        }
        stompRepository.sendMessage("/app/chat.random", userId)
    }

    override fun onCleared() {
        super.onCleared()
        stompRepository.disconnect()
    }
}