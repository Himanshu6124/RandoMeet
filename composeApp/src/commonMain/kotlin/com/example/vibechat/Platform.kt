package com.example.vibechat

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform