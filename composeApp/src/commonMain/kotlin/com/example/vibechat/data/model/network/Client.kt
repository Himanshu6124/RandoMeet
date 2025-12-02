package com.example.vibechat.data.model.network

import io.ktor.client.HttpClient
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.serialization.kotlinx.json.*

object KtorClient {
    val httpClient = HttpClient {
        install(ContentNegotiation) {
            json()
        }
    }
    const val BASE_URL = "https://your.api.com"
}

