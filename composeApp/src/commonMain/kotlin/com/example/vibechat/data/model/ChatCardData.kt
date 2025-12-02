package com.example.vibechat.data.model
import kotlinx.serialization.Serializable

@Serializable
data class ChatCardData(
    val conversationId : String = "",
    val friendUserName: String = "",
    val friendUserId: String = "",
    val isTyping : Boolean= false,
    val photoUrl: String = "",
    val lastMessage: String? = "",
    val isByYou : Boolean = false,
    val messageStatus : String? = "SENT",
    val lastMessageTime: String? = "",
    val messageType  :String? = ""
)