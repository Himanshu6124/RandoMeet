package com.example.vibechat.domain.intefaces

import com.example.vibechat.data.model.User
import com.example.vibechat.data.model.repository.AuthResponse
import com.example.vibechat.data.model.repository.LoginRequest

interface UserRepository {
    suspend fun getUser(deviceId: String): User?
    suspend fun getProfilePictures(): ArrayList<String>
    suspend fun saveUser(user: User): AuthResponse?
    suspend fun loginUser(loginRequest : LoginRequest): AuthResponse?


}