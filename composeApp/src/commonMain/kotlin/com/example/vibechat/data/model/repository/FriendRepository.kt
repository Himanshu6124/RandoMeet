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

    suspend fun getFriends(): List<User> =
        client.get("$BASE_URL/friendships/list").body()

    suspend fun getPendingFriendRequests(): List<User> =
        client.get("$BASE_URL/friendships/to-be-accepted").body()

    suspend fun acceptFriendRequest(friendId: String) {
        client.post("$BASE_URL/friendships/request/$friendId/accept") {
            contentType(ContentType.Application.Json)
        }
    }

    suspend fun rejectFriendRequest(friendId: String) {
        client.post("$BASE_URL/friendships/request/$friendId/reject") {
            contentType(ContentType.Application.Json)
        }
    }

    suspend fun getFriendConversations(): List<Conversation>? =
        client.get("$BASE_URL/friendships/conversations").body()

}