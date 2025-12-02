package com.example.vibechat.data.model
import kotlinx.serialization.Serializable

enum class MessageStatus {
    SENT, DELIVERED, READ
}


@Serializable
data class Message(
    val id : String? = null,
    val senderId: String?= null,
    val message: String,
    var status: MessageStatus? = null,
    val conversationId: String,
    val timeStamp: String,
)
@Serializable
data class OnlineStatus(
    val senderId: String?= null,
    val conversationId: String,
    val online: Boolean = false,
)
@Serializable
data class TypingStatus(
    val senderId: String?= null,
    val conversationId: String,
    val typing: Boolean = false ,
)