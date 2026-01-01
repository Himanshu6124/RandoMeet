package com.example.vibechat.socket

import com.example.vibechat.ui.screens.chatscreen.components.Message
import com.example.vibechat.ui.screens.matchscreen.ChatCardData
import kotlinx.coroutines.flow.StateFlow

expect class SocketRepository() {
    val chatCardData: StateFlow<ChatCardData>
    val onlineStatus: StateFlow<Boolean>
    val isTyping: StateFlow<Boolean>
    val messages: StateFlow<Message?>
    fun connect(userId : String)
    fun disconnect()
    suspend fun subscribe(topic : String)
    fun sendMessage(destination: String, message: Any)
}