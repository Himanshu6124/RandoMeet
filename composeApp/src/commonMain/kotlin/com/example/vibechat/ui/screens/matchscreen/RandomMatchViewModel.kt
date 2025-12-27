package com.example.vibechat.ui.screens.matchscreen

import androidx.lifecycle.viewModelScope
import com.example.vibechat.core.BaseViewModel
import com.example.vibechat.data.model.repository.SocketRepo
import com.example.vibechat.koin.DeviceInfo
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class RandomMatchViewModel(
    private val deviceInfo: DeviceInfo,
    private val stompRepository: SocketRepo
) : BaseViewModel<RandomMatchUIState, RandomMatchEvent, RandomMatchSideEffect>(){

    override val initialState: RandomMatchUIState
        get() = RandomMatchUIState()

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

    private suspend fun startMatching(userId: String) {
        _uiState.update { state ->
            state.copy(
                isLoading = true
            )
        }
        stompRepository.sendMessage("/app/chat.random", userId)
    }

}