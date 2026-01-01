package com.example.vibechat.data.model.repository

import com.example.vibechat.data.model.ChatCardData
import com.example.vibechat.data.model.User
import com.example.vibechat.data.model.network.KtorClient.BASE_URL
import com.example.vibechat.ui.screens.chatscreen.components.Message
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.statement.bodyAsText
import io.ktor.http.ContentType
import io.ktor.http.contentType

class ChatRepo(
    private val client: HttpClient
) {

    suspend fun getConversations(userId: String): List<ChatCardData> =
        client.get("$BASE_URL/conversations/user/$userId").body()

    suspend fun getMessages(conversationId: String): List<Message> =
        client.get("$BASE_URL/messages/conversation/$conversationId").body()

    suspend fun sendFriendRequest(
        userId: String,
        friendId: String,
        param: String = "send"
    ): Unit =
        client.post("$BASE_URL/friendships/request/$userId/$friendId/$param").body()

    suspend fun getData(){
            val res = client.get("https://jsonplaceholder.typicode.com/todos/1").bodyAsText()
        println("res from ktor: $res")
    }

}