package com.example.vibechat.data.model.repository

import com.example.vibechat.data.model.ChatCardData
import com.example.vibechat.ui.screens.chatscreen.components.Message
import com.example.vibechat.ui.screens.chatscreen.components.OnlineStatus
import com.example.vibechat.ui.screens.chatscreen.components.TypingStatus
import io.ktor.client.HttpClient
import io.ktor.client.plugins.websocket.WebSockets
import io.ktor.client.plugins.websocket.webSocketSession
import io.ktor.client.request.url
import io.ktor.websocket.Frame
import io.ktor.websocket.WebSocketSession
import io.ktor.websocket.close
import io.ktor.websocket.readText
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.serialization.builtins.serializer
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.jsonObject

class SocketRepo(
    private val client: HttpClient = HttpClient {
        install(WebSockets)
    }
) {
    private var session: WebSocketSession? = null
    private val json = Json { ignoreUnknownKeys = true }

    private val _messages = MutableStateFlow<Message?>(null)
    val messages: StateFlow<Message?> = _messages

    private val _onlineStatus = MutableStateFlow(false)
    val onlineStatus: StateFlow<Boolean> = _onlineStatus

    private val _isTyping = MutableStateFlow(false)
    val isTyping: StateFlow<Boolean> = _isTyping

    private val _chatCardData = MutableStateFlow(ChatCardData())
    val chatCardData: StateFlow<ChatCardData> = _chatCardData

    private var listenJob: Job? = null

    suspend fun connect(userId: String) {
        if (session != null) return

        val urlWithParams = "$BASE_URL?userId=$userId"
        session = client.webSocketSession { url(urlWithParams) }

        listenJob = CoroutineScope(Dispatchers.Default).launch {
            try {
                for (frame in session!!.incoming) {
                    if (frame is Frame.Text) {
                        val payload = frame.readText()
                        handlePayload(payload)
                    }
                }
            } catch (e: Exception) {
                println("WebSocket error: ${e.message}")
            }
        }
    }

    fun subscribe(topic: String) {
        println("Subscribed to topic: $topic")
    }

    private fun handlePayload(payload: String) {
        try {
            val jsonObject: JsonObject = json.parseToJsonElement(payload).jsonObject

            when {
                "message" in jsonObject -> {
                    val message = json.decodeFromString<Message>(payload)
                    _messages.value = message
                }
                "online" in jsonObject -> {
                    val status = json.decodeFromString<OnlineStatus>(payload)
                    _onlineStatus.value = status.online
                }
                "typing" in jsonObject -> {
                    val status = json.decodeFromString<TypingStatus>(payload)
                    _isTyping.value = status.typing
                }
                "friendUserName" in jsonObject -> {
                    val conversation = json.decodeFromString<ChatCardData>(payload)
                    _chatCardData.value = conversation
                }
                else -> {
                    println("Unknown payload: $payload")
                }
            }
        } catch (e: Exception) {
            println("Parse error: ${e.message}")
        }
    }

    suspend fun sendMessage(destination: String, message: Any) {
//        val jsonText = when (message) {
//            is Message -> json.encodeToString<Message>(message)
//            is OnlineStatus -> json.encodeToString<OnlineStatus>(message)
//            is TypingStatus -> json.encodeToString<TypingStatus>(message)
//            else -> json.encodeToString(Message.serializer(), message as Message)
//        }
//        session?.send(Frame.Text(jsonText))
    }

    fun disconnect() {
        listenJob?.cancel()
        listenJob = null
        CoroutineScope(Dispatchers.Default).launch {
            session?.close()
            session = null
        }
    }
}

private const val BASE_URL = "ws://192.168.31.8:8080/ws-chat"
