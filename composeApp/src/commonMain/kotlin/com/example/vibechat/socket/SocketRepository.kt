package com.example.vibechat.socket

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import com.example.vibechat.ui.screens.chatscreen.components.Message
import com.example.vibechat.ui.screens.chatscreen.components.SeenStatus
import com.example.vibechat.ui.screens.matchscreen.Conversation
import kotlinx.coroutines.flow.StateFlow

expect class SocketRepository(dataStore: DataStore<Preferences>) {
    val matchedConversation: StateFlow<Conversation>
    val onlineStatus: StateFlow<Boolean>
    val isTyping: StateFlow<Boolean>
    val disconnectedUserName: StateFlow<String?>
    val messages: StateFlow<Message?>
    val seenStatus: StateFlow<SeenStatus>
    suspend fun connect(userId : String)
    fun disconnect()
    suspend fun subscribe(topic : String)
    fun sendMessage(destination: String, message: Any?)
}