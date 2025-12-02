package com.example.vibechat.data.model.repository

import com.example.vibechat.data.model.ChatCardData
import com.example.vibechat.data.model.Message
import com.example.vibechat.data.model.User
import com.example.vibechat.data.model.network.KtorClient
import com.example.vibechat.data.model.network.KtorClient.BASE_URL
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType

class ChatRepo(
    private val client: HttpClient = KtorClient.httpClient

) {

    suspend fun saveUser(user: User): User {
        return client.post("$BASE_URL/users") {
            contentType(ContentType.Application.Json)
            setBody(user)
        }.body()
    }

    suspend fun getUser(userId: String): User =
        client.get("$BASE_URL/users/$userId").body()

    suspend fun getProfilePictures(): List<String> =
        client.get("$BASE_URL/users/profile-pictures").body()

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

}