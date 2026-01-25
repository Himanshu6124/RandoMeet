package com.example.vibechat.socket

import android.annotation.SuppressLint
import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import com.example.vibechat.constants.CONSTANTS.TOKEN_KEY
import com.example.vibechat.core.utils.EMPTY
import com.example.vibechat.ui.screens.chatscreen.components.DisconnectStatus
import com.example.vibechat.ui.screens.chatscreen.components.Message
import com.example.vibechat.ui.screens.chatscreen.components.OnlineStatus
import com.example.vibechat.ui.screens.chatscreen.components.SeenStatus
import com.example.vibechat.ui.screens.chatscreen.components.TypingStatus
import com.example.vibechat.ui.screens.matchscreen.Conversation
import com.google.gson.Gson
import com.google.gson.JsonObject
import com.google.gson.JsonParser
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
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
    private val _disconnectedUserName : MutableStateFlow<String?> = MutableStateFlow(String.EMPTY)

    actual val disconnectedUserName: StateFlow<String?> = _disconnectedUserName

    private val _chatCardData = MutableStateFlow(Conversation())
    actual val matchedConversation: StateFlow<Conversation> = _chatCardData

    private val _seenStatus = MutableStateFlow(SeenStatus())
    actual val seenStatus: StateFlow<SeenStatus> = _seenStatus


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
            try {
                val jsonObject = JsonParser
                    .parseString(topicMessage.payload)
                    .asJsonObject

                val parsedEvent = parseEvent(jsonObject)
                when(parsedEvent){
                    is SocketEvent.ChatCardEvent -> {
                        _chatCardData.tryEmit(parsedEvent.chat)

                    }
                    is SocketEvent.MessageEvent -> {
                        _messages.tryEmit(parsedEvent.message)

                    }
                    is SocketEvent.OnlineEvent -> {
                        _onlineStatus.tryEmit(parsedEvent.online.online)
                    }
                    is SocketEvent.TypingEvent -> {
                        _isTyping.tryEmit(parsedEvent.typing.typing)

                    }
                    is SocketEvent.DisconnectEvent -> {
                        _disconnectedUserName.tryEmit(parsedEvent.status.senderId)
                    }

                    is SocketEvent.MessageSeenEvent -> {
                        _seenStatus.tryEmit(parsedEvent.status)
                    }

                    null -> {
                        Log.w("STOMP", "Unknown event type ${jsonObject}")
                    }
                }
            } catch (e: Exception) {
                Log.e("STOMP", "Socket parse error", e)
            }
        }
    }

    private fun parseEvent(json: JsonObject): SocketEvent? {
        val type = json.get("type")?.asString ?: return null
        val payload = json.getAsJsonObject("payload")

        return when (type) {
            "MESSAGE" ->
                SocketEvent.MessageEvent(
                    gson.fromJson(payload, Message::class.java)
                )

            "TYPING" ->
                SocketEvent.TypingEvent(
                    gson.fromJson(payload, TypingStatus::class.java)
                )

            "ONLINE_STATUS" ->
                SocketEvent.OnlineEvent(
                    gson.fromJson(payload, OnlineStatus::class.java)
                )

            "CONVERSATION_DTO" ->
                SocketEvent.ChatCardEvent(
                    gson.fromJson(payload, Conversation::class.java)
                )
            "DISCONNECTED_DTO" ->
                SocketEvent.DisconnectEvent(
                    gson.fromJson(payload, DisconnectStatus::class.java)
                )

            "SEEN" ->{
                SocketEvent.MessageSeenEvent(
                    gson.fromJson(payload, SeenStatus::class.java)
                )
            }
            else -> {
                null
            }
        }
    }


    actual fun sendMessage(destination: String, message: Any?) {
        if(message == null) {
            stompClient?.send(destination)?.subscribe()
        }else{
            val json = Gson().toJson(message)
            stompClient?.send(destination,json)?.subscribe()
        }
   
    }

    actual fun disconnect() {
        if (stompClient?.isConnected == true) {
            stompClient?.disconnect()
            Log.d("STOMP", "Disconnected")
        }
    }
}
sealed interface SocketEvent {
    data class MessageEvent(val message: Message) : SocketEvent
    data class TypingEvent(val typing: TypingStatus) : SocketEvent
    data class OnlineEvent(val online: OnlineStatus) : SocketEvent
    data class ChatCardEvent(val chat: Conversation) : SocketEvent
    data class DisconnectEvent(val status: DisconnectStatus) : SocketEvent
    data class MessageSeenEvent(val status: SeenStatus) : SocketEvent
}


private fun createStompClient(token: String): StompClient {
    val urlWithParams = "$BASE_URL?token=$token"
    return Stomp.over(Stomp.ConnectionProvider.OKHTTP, urlWithParams)
}

//private val BASE_URL = "ws://192.168.31.8:8080/ws-chat"

private val BASE_URL = "wss://randomchat.qzz.io/ws-chat"

//private const val TOKEN = "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJoaW0iLCJ1c2VybmFtZSI6ImhpbSIsImlhdCI6MTc2ODA1MDEzNCwiZXhwIjoxNzY4MTM2NTM0fQ.jBwoYwRzGOt8ogTmy9i9lzlpefNDnz22UkPg31KZF04admh8qbrxokwVmSt3fpZTW8KMadmeFpQd7D39JBaGEg"