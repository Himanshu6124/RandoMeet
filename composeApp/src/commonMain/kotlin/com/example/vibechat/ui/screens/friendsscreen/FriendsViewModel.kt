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

    init {
        getFriends()
    }

    override fun handleEvent(event: FriendsEvent) {
        when (event) {
            is FriendsEvent.OnFabClick -> {
                loadFriendRequests()
                _uiState.update { it.copy(showRequestDialog = true) }
            }
            is FriendsEvent.OnDismissDialog -> {
                _uiState.update { it.copy(showRequestDialog = false) }
            }
            is FriendsEvent.OnAcceptRequest -> {
                acceptFriendRequest(event.friendUsername)
            }
            is FriendsEvent.OnRejectRequest -> {
                rejectFriendRequest(event.friendUsername)
            }
            is FriendsEvent.OnLoadFriendRequests -> {
                loadFriendRequests()
            }
        }
    }

    private fun loadFriendRequests() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            
            try {
                val requests = friendsRepository.getPendingFriendRequests()
                _uiState.update { state ->
                    state.copy(
                        friendRequests = requests,
                        isLoading = false
                    )
                }
            } catch (e: Exception) {
                _uiState.update { state ->
                    state.copy(
                        isLoading = false,
                        error = e
                    )
                }
                viewModelScope.launch {
                    _effect.emit(FriendsSideEffect.ShowToast("Failed to load friend requests"))
                }
            }
        }
    }

    private fun acceptFriendRequest(friendUsername: String) {
        viewModelScope.launch {
            try {
                friendsRepository.acceptFriendRequest(friendUsername)
                _uiState.update { state ->
                    state.copy(
                        friendRequests = state.friendRequests.filter { it.username != friendUsername }
                    )
                }
                viewModelScope.launch {
                    _effect.emit(FriendsSideEffect.ShowToast("Friend request accepted"))
                }
                // Reload friends list
                getFriends()
            } catch (e: Exception) {
                viewModelScope.launch {
                    _effect.emit(FriendsSideEffect.ShowToast("Failed to accept request"))
                }
            }
        }
    }

    private fun rejectFriendRequest(friendUsername: String) {
        viewModelScope.launch {
            try {
                friendsRepository.rejectFriendRequest(friendUsername)
                _uiState.update { state ->
                    state.copy(
                        friendRequests = state.friendRequests.filter { it.username != friendUsername }
                    )
                }
                viewModelScope.launch {
                    _effect.emit(FriendsSideEffect.ShowToast("Friend request rejected"))
                }
            } catch (e: Exception) {
                viewModelScope.launch {
                    _effect.emit(FriendsSideEffect.ShowToast("Failed to reject request"))
                }
            }
        }
    }


    fun getFriends() {
        viewModelScope.launch {
            _uiState.update { state ->
                state.copy(
                    isLoading = true
                )
            }

            try {
                val res = friendsRepository.getFriendConversations()
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