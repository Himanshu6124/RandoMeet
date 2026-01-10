package com.example.vibechat.socket

import android.annotation.SuppressLint
import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import com.example.vibechat.constants.CONSTANTS.TOKEN_KEY
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
import kotlinx.coroutines.flow.first
import org.koin.compose.koinInject
import org.koin.java.KoinJavaComponent.inject
import ua.naiksoftware.stomp.Stomp
import ua.naiksoftware.stomp.StompClient
import ua.naiksoftware.stomp.dto.LifecycleEvent

actual class SocketRepository actual constructor(
    private val dataStore: DataStore<Preferences>
) {

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
    actual suspend fun connect(userId: String) {
        if (stompClient?.isConnected == true) return
        val token = dataStore.data.first()[TOKEN_KEY]
        if(token.isNullOrEmpty()) {
            Log.e("STOMP", "Token is empty")
            return
        }
        stompClient = createStompClient(token)
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
        stompClient?.send(destination)?.subscribe()
    }

    actual fun disconnect() {
        if (stompClient?.isConnected == true) {
            stompClient?.disconnect()
            Log.d("STOMP", "Disconnected")
        }
    }
}


private fun createStompClient(token: String): StompClient {
    val urlWithParams = "$BASE_URL?token=$token"
    return Stomp.over(Stomp.ConnectionProvider.OKHTTP, urlWithParams)
}

//private val BASE_URL = "ws://192.168.31.8:8080/ws-chat"

private val BASE_URL = "wss://randomchat.qzz.io/ws-chat"

//private const val TOKEN = "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJoaW0iLCJ1c2VybmFtZSI6ImhpbSIsImlhdCI6MTc2ODA1MDEzNCwiZXhwIjoxNzY4MTM2NTM0fQ.jBwoYwRzGOt8ogTmy9i9lzlpefNDnz22UkPg31KZF04admh8qbrxokwVmSt3fpZTW8KMadmeFpQd7D39JBaGEg"