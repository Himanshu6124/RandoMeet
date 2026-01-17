package com.example.vibechat.datastore

interface AppDataStore{

    suspend fun getToken(): String

    suspend fun saveToken(token: String)

    suspend fun getUserId(): String

    suspend fun saveUserId(userId: String)

    suspend fun clearUserId()

    suspend fun clearToken()

    suspend fun clearAll()
}