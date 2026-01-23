package com.example.vibechat.ui.screens.friendsscreen

import androidx.lifecycle.viewModelScope
import com.example.vibechat.core.BaseViewModel
import com.example.vibechat.data.model.repository.FriendRepository
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class FriendsViewModel(
    private val friendsRepository: FriendRepository
) : BaseViewModel<FriendsUIState, FriendsEvent, FriendsSideEffect>() {

    override val initialState: FriendsUIState
        get() = FriendsUIState()

    override fun handleEvent(event: FriendsEvent) {

    }


    fun getFriends(userId: String) {
        viewModelScope.launch {
            _uiState.update { state ->
                state.copy(
                    isLoading = true
                )
            }

            try {
                val res = friendsRepository.getFriendConversations(userId)
                _uiState.update { state ->
                    state.copy(
                        friends = res ?: arrayListOf(),
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
}