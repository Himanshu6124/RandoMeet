package com.example.vibechat.ui.screens.friendsscreen

import com.example.vibechat.data.model.User
import com.example.vibechat.ui.screens.matchscreen.Conversation

data class FriendsUIState(
    val isLoading: Boolean = false,
    val friends: List<Conversation> = emptyList(),
    val friendRequests: List<User> = emptyList(),
    val showRequestDialog: Boolean = false,
    val error: Exception? = null
)

sealed interface FriendsEvent {
    data object OnFabClick : FriendsEvent
    data object OnDismissDialog : FriendsEvent
    data class OnAcceptRequest(val friendUsername: String) : FriendsEvent
    data class OnRejectRequest(val friendUsername: String) : FriendsEvent
    data object OnLoadFriendRequests : FriendsEvent
}


sealed interface FriendsSideEffect {
    data class ShowToast(val message: String) : FriendsSideEffect
}