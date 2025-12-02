package com.example.vibechat.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.vibechat.data.model.User
import com.example.vibechat.data.model.repository.FriendRepo
import com.example.vibechat.ui.viewmodels.uiStates.FriendsUIState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class FriendsViewModel : ViewModel() {

    private val friendsRepository = FriendRepo()

    private val _user = MutableStateFlow<User?>(null)
    val user: StateFlow<User?> = _user

    init {
        viewModelScope.launch {
//            _user.value = userDataStore.getUser()
        }
    }

    private val _uiState = MutableStateFlow(FriendsUIState())
    val uiState: StateFlow<FriendsUIState> = _uiState

//    fun getFriends(userId: String) {
//        viewModelScope.launch {
//            _uiState.update { it.copy(isLoading = true, exception = null) }
//
//            try {
//                val response = friendsRepository.getFriends(userId)
//                _uiState.update {
//                    it.copy(friends = response.body() ?: emptyList())
//                }
//            } catch (e: Exception) {
//                _uiState.update {
//                    it.copy(exception = e)
//                }
//            } finally {
//                _uiState.update {
//                    it.copy(isLoading = false)
//                }
//            }
//        }
//    }

    fun getFriendsConversations(userId: String) {
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

    fun getPendingFriendRequests(userId: String) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, exception = null) }

            try {
                val response = friendsRepository.getPendingFriendRequests(userId)
                _uiState.update {
                    it.copy(pendingRequests = response)
                }
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(exception = e)
                }
            } finally {
                _uiState.update {
                    it.copy(isLoading = false)
                }
            }
        }
    }

    fun acceptFriendRequest(userId: String, friendId: String) {
        viewModelScope.launch {
            try {
                val response = friendsRepository.acceptFriendRequest(userId, friendId)
                println("accepting friend request: $response")
            } catch (e: Exception) {
                println("Exception in accepting friend request: ${e.message}")

            } finally {
            }
        }
    }
}