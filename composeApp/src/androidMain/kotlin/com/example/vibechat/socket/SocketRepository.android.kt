package com.example.vibechat.socket

import android.annotation.SuppressLint
import android.util.Log
import com.example.vibechat.ui.screens.chatscreen.components.Message
import com.example.vibechat.ui.screens.chatscreen.components.OnlineStatus
import com.example.vibechat.ui.screens.chatscreen.components.TypingStatus
import com.example.vibechat.ui.screens.matchscreen.ChatCardData
import com.google.gson.Gson
import com.google.gson.JsonParser
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import ua.naiksoftware.stomp.Stomp
import ua.naiksoftware.stomp.StompClient
import ua.naiksoftware.stomp.dto.LifecycleEvent

actual class SocketRepository actual constructor() {

    private var stompClient: StompClient? = null
    private val gson = Gson()

    private val _messages = MutableStateFlow<Message?>(null)
    actual val messages: StateFlow<Message?> = _messages

    private val _onlineStatus = MutableStateFlow(false)
    actual val onlineStatus: StateFlow<Boolean> = _onlineStatus

    private val _isTyping = MutableStateFlow(false)
    actual val isTyping: StateFlow<Boolean> = _isTyping

    private val _chatCardData = MutableStateFlow(ChatCardData())
    actual val chatCardData: StateFlow<ChatCardData> = _chatCardData


    @SuppressLint("CheckResult")
    actual fun connect(userId: String) {
        if (stompClient?.isConnected == true) return

        stompClient = createStompClient(userId)
        stompClient?.connect()

        stompClient?.lifecycle()?.subscribe { lifecycleEvent ->
            when (lifecycleEvent.type) {
                LifecycleEvent.Type.OPENED -> Log.d("STOMP", "Connection opened")
                LifecycleEvent.Type.ERROR -> Log.e("STOMP", "Error", lifecycleEvent.exception)
                LifecycleEvent.Type.CLOSED -> Log.d("STOMP", "Connection closed")
                else -> Unit
            }
        }
    }

    @SuppressLint("CheckResult")
    actual suspend fun subscribe(
        topic : String,
    ) {
        stompClient?.topic(topic)?.subscribe { topicMessage ->
            Log.d("STOMP", "Received: ${topicMessage.payload}")

            try {
                val jsonObject = JsonParser.parseString(topicMessage.payload).asJsonObject

                when {
                    jsonObject.has("message") -> {
                        val message = gson.fromJson(topicMessage.payload, Message::class.java)
                        _messages.value = message
                    }
                    jsonObject.has("online") -> {
                        val status = gson.fromJson(topicMessage.payload, OnlineStatus::class.java)
                        _onlineStatus.value = status.online
                    }
                    jsonObject.has("typing") -> {
                        val status = gson.fromJson(topicMessage.payload, TypingStatus::class.java)
                        _isTyping.value = status.typing
                    }
                    jsonObject.has("friendUserName") -> {
                        val conversation = gson.fromJson(topicMessage.payload, ChatCardData::class.java)
                        _chatCardData.value = conversation
                    }
                    else -> {
                        Log.w("STOMP", "Unknown payload: ${topicMessage.payload}")
                    }
                }
            } catch (e: Exception) {
                Log.e("STOMP", "Parse error", e)
            }
        }
    }


    actual fun sendMessage(destination: String, message: Any) {
        val json = Gson().toJson(message)
        stompClient?.send(destination, json)?.subscribe()
    }

    actual fun disconnect() {
        if (stompClient?.isConnected == true) {
            stompClient?.disconnect()
            Log.d("STOMP", "Disconnected")
        }
    }
}


private fun createStompClient(userId: String): StompClient {
    val urlWithParams = "$BASE_URL?userId=$userId"
    return Stomp.over(Stomp.ConnectionProvider.OKHTTP, urlWithParams)
}

//private val BASE_URL = "ws://192.168.31.8:8080/ws-chat"

private val BASE_URL = "ws://10.0.2.2:8080/ws-chat"