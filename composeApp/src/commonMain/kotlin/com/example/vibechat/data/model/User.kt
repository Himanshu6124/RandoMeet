package com.example.vibechat.data.model
import kotlinx.serialization.Serializable

@Serializable
data class User(
    val username: String? = null,
    val password: String? = null,
    val name: String? = null,
    val email: String? = null,
    val gender: String? = null,
    val bio: String? = null,
    val photoUrl: String? = null,
    val location: String? = null,
    val status: String? = null,
    val suspectLevel: Int? = null,
    val lastOnline: String? = null,
    val fcmToken: String? = null
)

