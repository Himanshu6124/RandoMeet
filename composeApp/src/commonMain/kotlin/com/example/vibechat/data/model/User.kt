package com.example.vibechat.data.model
import kotlinx.serialization.Serializable

@Serializable
data class User(
    val deviceId: String,
    val name: String? = null,
    val gender: String? = null,
    val location: String? = null,
    val status: String? = null,
    val suspectLevel: Int? = null,
    val bio: String? = null,
    val photoId: String? = null,
    val lastOnline: String? = null,
    val fcmToken: String? = null
)

