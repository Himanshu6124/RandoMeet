package com.example.vibechat.ui.screens.matchscreen

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.lifecycle.viewModelScope
import com.example.vibechat.constants.CONSTANTS.TOKEN_KEY
import com.example.vibechat.constants.CONSTANTS.USER_ID
import com.example.vibechat.core.BaseViewModel
import com.example.vibechat.data.model.repository.ChatRepo
import com.example.vibechat.data.model.repository.SocketRepo
import com.example.vibechat.domain.intefaces.UserRepository
import com.example.vibechat.koin.DeviceInfo
import com.example.vibechat.socket.SocketRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class RandomMatchViewModel(
    private val deviceInfo: DeviceInfo,
    private val stompRepository: SocketRepository,
    private val dataStore: DataStore<Preferences>,
    private val userRepository: UserRepository
) : BaseViewModel<RandomMatchUIState, RandomMatchEvent, RandomMatchSideEffect>(){

    val tokenFlow: StateFlow<String?> =
        dataStore.data
            .map { prefs -> prefs[TOKEN_KEY] }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5_000),
                initialValue = null
            )

    val userIdFlow: StateFlow<String?> =
        dataStore.data
            .map { prefs -> prefs[USER_ID] }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5_000),
                initialValue = null
            )

    init {
        viewModelScope.launch {
            val res = userRepository.getUser("him")
            println("User is $res")

        }
    }

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
            stompRepository.connect(
                userId = dataStore.data.first()[TOKEN_KEY].orEmpty(),
            )
            stompRepository.subscribe(
                topic = "/topic/room/random/${dataStore.data.first()[USER_ID].orEmpty()}"
            )
            startMatching(tokenFlow.value.orEmpty())
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