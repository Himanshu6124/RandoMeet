package com.example.vibechat.ui.screens.chatscreen

import androidx.compose.runtime.Immutable
import com.example.vibechat.core.utils.EMPTY
import com.example.vibechat.ui.screens.chatscreen.components.Message

@Immutable
data class ChatUIState(
    val messages: List<Message> = emptyList(),
    val isLoading: Boolean = false,
    val isOnline : Boolean = false,
    val userId : String? = String.EMPTY,
    val isTyping : Boolean = false,
    val exception: Exception?  = null
)

sealed interface ChatEvent {
    data class SendMessage(val message: Message , val isForRandomMatching : Boolean ) : ChatEvent
}

sealed interface ChatSideEffect {
    data class ShowSnackBar(val message: String) : ChatSideEffect
    data class AppendMessage(val message: Message) : ChatSideEffect
    data class ShowToast(val message: String) : ChatSideEffect
}

