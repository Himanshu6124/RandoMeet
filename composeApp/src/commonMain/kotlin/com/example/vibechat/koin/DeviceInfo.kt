package com.example.vibechat.koin

import io.ktor.client.HttpClient

interface DeviceInfo {
    fun getDeviceId(): String
}

expect fun getDeviceInfo(): DeviceInfo

//expect class KtorClientFactory {
//    fun create(): HttpClient
//}
