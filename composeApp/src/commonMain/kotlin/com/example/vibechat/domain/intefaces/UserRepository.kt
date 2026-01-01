package com.example.vibechat.domain.intefaces

import com.example.vibechat.data.model.User

interface UserRepository {
    suspend fun getUser(deviceId: String): User?
    suspend fun getProfilePictures(): ArrayList<String>
    suspend fun saveUser(user: User): User?


}