package com.example.vibechat.data.model

import kotlinx.serialization.Serializable

@Serializable
data class FriendRequest(
    val username: String? = null,
    val name: String? = null,
    val photoUrl: String? = null,
    val bio: String? = null,
    val gender: String? = null
)
