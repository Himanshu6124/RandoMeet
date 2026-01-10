package com.example.vibechat.data.model.repository

import kotlinx.serialization.Serializable

@Serializable
data class AuthResponse(
    val userId: String,
    val jwt: String
)
@Serializable
data class LoginRequest(
    val username: String,
    val password: String
)