package com.example.vibechat.ui.screens.chatscreen.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.vibechat.core.utils.EMPTY
import com.example.vibechat.ui.commoncomposables.whiteColor
import com.example.vibechat.ui.screens.chatscreen.greyGradient
import com.example.vibechat.ui.screens.matchscreen.MessageStatus
import com.example.vibechat.utils.getBubbleTime
import kotlinx.serialization.Serializable
import org.jetbrains.compose.ui.tooling.preview.Preview

@Preview
@Composable
fun MessageCard(
    userId :String? = "fa",
    message: Message = dummyMessages[0] ,
    onDeleteMessage : (Message)-> Unit = {}
){
    Row(
        modifier =
            Modifier
                .background(Color.Black)
                .fillMaxWidth()
                .padding(bottom = if(message.senderId == userId ) 0.dp else 20.dp, start = 8.dp,end = 8.dp)
        ,
        horizontalArrangement = if(message.senderId == userId ) Arrangement.End else Arrangement.Start
    ) {
        Column(
            modifier = Modifier
                .clickable { onDeleteMessage(message) }
                .background(Color.Black)
            ,
        ){
            Text(
                text = message.message,
                color = whiteColor,
                modifier = Modifier.background(
                    shape = RoundedCornerShape(50),
                    color = Color.Gray
                ).padding(10.dp)
            )

            Text(
                color = whiteColor,
                modifier = Modifier.align(Alignment.End).padding(end = 10.dp),
                text = getBubbleTime(message.timeStamp),
                fontSize = 10.sp
            )
            if(message.senderId == userId ){
                Text(
                    color = whiteColor,
                    modifier = Modifier.align(Alignment.End).padding(end = 10.dp),
                    text = if(message.isSeen) "seen" else "",
                    fontSize = 10.sp
                )
            }
        }
    }
}

val dummyMessages = listOf(
    Message(
        id = "1",
        senderId = "12",
        message = "Hey 👋",
        status = MessageStatus.READ,
        conversationId = "conv_123",
        timeStamp = "10:01 AM"
    ),
    Message(
        id = "2",
        senderId = "12",
        message = "Hi! How are you?",
        status = MessageStatus.SENT,
        conversationId = "conv_123",
        timeStamp = "10:02 AM"
    ),
    Message(
        id = "3",
        senderId = "user_1",
        message = "I’m good 😄 Working on a chat app",
        status = MessageStatus.READ,
        conversationId = "conv_123",
        timeStamp = "10:03 AM"
    ),
    Message(
        id = "4",
        senderId = "12",
        message = "Nice! Is it random chat or friends based?",
        status = MessageStatus.DELIVERED,
        conversationId = "conv_123",
        timeStamp = "10:04 AM"
    ),
    Message(
        id = "5",
        senderId = "user_1",
        message = "Random chat with online users 🔥",
        status = MessageStatus.SENT,
        conversationId = "conv_123",
        timeStamp = "10:05 AM"
    ),
    Message(
        id = "6",
        senderId = "user_1",
        message = "Planning audio calling next 🎧",
        status = MessageStatus.SENT,
        conversationId = "conv_123",
        timeStamp = "10:06 AM"
    )
)

@Serializable
data class MessageResponse(
    val content: List<Message> = emptyList()
)

@Serializable
data class Message(
    val id : String? = null,
    val senderId: String?= null,
    val message: String,
    var status: MessageStatus? = null,
    val conversationId: String,
    val timeStamp: String,
    val messageType: MessageType? = MessageType.TEXT,
    val isSeen: Boolean = false
)
enum class MessageType{
    TEXT,
    IMAGE,
}

data class OnlineStatus(
    val senderId: String?= null,
    val conversationId: String,
    val online: Boolean = false,
)

data class TypingStatus(
    val senderId: String?= null,
    val conversationId: String,
    val typing: Boolean = false ,
)

data class DisconnectStatus(
    val senderId: String?= null,
    val conversationId: String = String.EMPTY ,
)
data class SeenStatus(
    val seenAt: String?= null,
    val conversationId: String?= null,
    val messageId: String? = null ,
)