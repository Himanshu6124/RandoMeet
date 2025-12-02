package com.example.vibechat.ui.viewmodels.uiStates

import androidx.compose.runtime.Immutable
import com.example.vibechat.data.model.ChatCardData
import com.example.vibechat.data.model.User

@Immutable
data class FriendsUIState(
    val friends: List<ChatCardData> = emptyList(),
    val pendingRequests: List<User> = emptyList(),
    val isLoading: Boolean = false,
    val exception: Exception?  = null
)