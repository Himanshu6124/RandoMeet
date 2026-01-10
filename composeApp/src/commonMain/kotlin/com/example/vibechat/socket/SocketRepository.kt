package com.example.vibechat.socket

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import com.example.vibechat.ui.screens.chatscreen.components.Message
import com.example.vibechat.ui.screens.matchscreen.ChatCardData
import kotlinx.coroutines.flow.StateFlow

expect class SocketRepository(dataStore: DataStore<Preferences>) {
    val chatCardData: StateFlow<ChatCardData>
    val onlineStatus: StateFlow<Boolean>
    val isTyping: StateFlow<Boolean>
    val messages: StateFlow<Message?>
    suspend fun connect(userId : String)
    fun disconnect()
    suspend fun subscribe(topic : String)
    fun sendMessage(destination: String, message: Any)
}