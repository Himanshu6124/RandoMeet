package com.example.vibechat.ui.viewmodels.uiStates

import androidx.compose.runtime.Immutable
import com.example.vibechat.data.model.Message

@Immutable
data class ChatUIState(
    val messages: List<Message> = emptyList(),
    val isLoading: Boolean = false,
    val isOnline : Boolean = false,
    val exception: Exception?  = null
)