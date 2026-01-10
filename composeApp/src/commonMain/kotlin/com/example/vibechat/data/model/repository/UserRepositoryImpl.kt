package com.example.vibechat.data.model.repository

import com.example.vibechat.data.model.User
import com.example.vibechat.data.model.network.KtorClient
import com.example.vibechat.domain.intefaces.UserRepository
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType

class UserRepositoryImpl(
    private val client: HttpClient
) : UserRepository {
    override suspend fun getUser(deviceId: String): User? {
       return client.get("${KtorClient.BASE_URL}/users/$deviceId").body()
    }

    override suspend fun getProfilePictures(): ArrayList<String> {
        return client.get("${KtorClient.BASE_URL}/users/profile-pictures").body()
    }

    override suspend fun saveUser(user: User): AuthResponse? {
        return client.post("${KtorClient.BASE_URL}/auth/signup") {
            contentType(ContentType.Application.Json)
            setBody(user)
        }.body()
    }

    override suspend fun loginUser(
        loginRequest: LoginRequest
    ): AuthResponse? {
        return client.post("${KtorClient.BASE_URL}/auth/login") {
            contentType(ContentType.Application.Json)
            setBody(loginRequest)
        }.body()
    }
}