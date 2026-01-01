package com.example.vibechat.ui.screens.matchscreen

import androidx.compose.runtime.Immutable
import com.example.vibechat.core.utils.EMPTY
import kotlinx.serialization.Serializable

@Immutable
data class RandomMatchUIState(
    val matchingConversation: ChatCardData = ChatCardData(),
    val isLoading: Boolean = false,
    val exception: Exception? = null
)

@Serializable
data class ChatCardData(
    val conversationId: String = String.EMPTY,
    val friendUserName: String = String.EMPTY,
    val friendUserId: String = String.EMPTY,
    val isTyping: Boolean = false,
    val photoUrl: String = String.EMPTY,
    val lastMessage: String? = String.EMPTY,
    val isByYou: Boolean = false,
    val messageStatus: MessageStatus? = MessageStatus.SENT,
    val lastMessageTime: String? = String.EMPTY,
    val messageType: String? = String.EMPTY
)

enum class MessageStatus(val status: String) {
    SENT("SENT"), DELIVERED("DELIVERED"), READ("READ")
}

sealed interface RandomMatchEvent {
    data object OnStartMatchClick : RandomMatchEvent
}

sealed interface RandomMatchSideEffect {
    data class NavigateToChatScreen(val matchedConversation: ChatCardData) : RandomMatchSideEffect
}