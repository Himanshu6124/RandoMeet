package com.example.vibechat.data.model.repository

import kotlinx.serialization.Serializable

@Serializable
data class SignUpResponse(
    val userId: String,
    val jwt: String
)