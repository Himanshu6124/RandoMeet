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
    const val BASE_URL = "http://10.0.2.2:8080"
//    const val BASE_URL = "http://192.168.1.6:8080"
}

