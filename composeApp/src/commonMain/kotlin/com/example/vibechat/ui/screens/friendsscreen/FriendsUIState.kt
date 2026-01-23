package com.example.vibechat.ui.screens.friendsscreen

import com.example.vibechat.ui.screens.matchscreen.Conversation

data class FriendsUIState(
    val isLoading: Boolean = false,
    val friends: List<Conversation> = emptyList(),
    val error: Exception? = null
)

sealed interface FriendsEvent {

}


sealed interface FriendsSideEffect {

}