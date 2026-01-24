package com.example.vibechat.data.model.repository

import com.example.vibechat.data.model.network.KtorClient.BASE_URL
import com.example.vibechat.ui.screens.chatscreen.components.MessageResponse
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get

class ChatRepo(
    private val client: HttpClient
) {
    suspend fun getMessages(conversationId: String, page: Int = 0, size: Int = 20): MessageResponse =
        client.get("$BASE_URL/messages/conversation/$conversationId?page=$page&size=$size").body()

}