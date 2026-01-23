package com.example.vibechat.data.model.repository

import com.example.vibechat.data.model.ChatCardData
import com.example.vibechat.data.model.User
import com.example.vibechat.data.model.network.KtorClient
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.http.ContentType
import io.ktor.http.contentType
import com.example.vibechat.data.model.network.KtorClient.BASE_URL
import com.example.vibechat.ui.screens.matchscreen.Conversation

class FriendRepository(
    private val client: HttpClient = KtorClient.httpClient
) {

    suspend fun getFriends(userId: String): List<User> =
        client.get("$BASE_URL/friendships/list/$userId").body()

    suspend fun getPendingFriendRequests(userId: String): List<User> =
        client.get("$BASE_URL/friendships/to-be-accepted/$userId").body()

    suspend fun acceptFriendRequest(userId: String, friendId: String, param: String = "accept") {
        client.post("$BASE_URL/friendships/request/$userId/$friendId/$param") {
            contentType(ContentType.Application.Json)
        }
    }

    suspend fun getFriendConversations(userId: String): List<Conversation>? =
        client.get("$BASE_URL/friendships/conversations/$userId").body()

}