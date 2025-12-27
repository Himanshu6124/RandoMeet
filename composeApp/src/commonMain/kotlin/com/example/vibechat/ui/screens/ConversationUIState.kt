package com.example.vibechat.ui.screens

import androidx.compose.runtime.Immutable
import com.example.vibechat.data.model.ChatCardData

@Immutable
data class ConversationUIState(
    val conversations :ArrayList<ChatCardData> = arrayListOf(),
    val matchingConversation :ChatCardData = ChatCardData(),
    val isLoading : Boolean = false,
    val exception: Exception?  = null
)