package com.example.vibechat.ui.screens.chatscreen

import androidx.compose.runtime.Immutable
import com.example.vibechat.core.utils.EMPTY
import com.example.vibechat.ui.screens.chatscreen.components.Message
import com.example.vibechat.ui.screens.matchscreen.Conversation

@Immutable
data class ChatUIState(
    val messages: List<Message> = emptyList(),
    val isLoading: Boolean = false,
    val isOnline : Boolean = false,
    val userId : String? = String.EMPTY,
    val isTyping : Boolean = false,
    val exception: Exception?  = null,
    val conversation: Conversation = Conversation()
)

sealed interface ChatEvent {
    data class SendMessage(val message: Message , val isForRandomMatching : Boolean ) : ChatEvent
    data object DisconnectSocket : ChatEvent
    data class InitState(val conversation: Conversation?) : ChatEvent
}

sealed interface ChatSideEffect {
    data class ShowSnackBar(val message: String) : ChatSideEffect
    data class ScrollToBottom(val index: Int) : ChatSideEffect
    data class ShowToast(val message: String) : ChatSideEffect
    data class NavigateToMatchScreen(val disconnectedUserName: String?) : ChatSideEffect
}

