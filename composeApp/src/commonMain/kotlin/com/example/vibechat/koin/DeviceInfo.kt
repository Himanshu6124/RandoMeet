package com.example.vibechat.koin

import androidx.compose.runtime.Composable
import io.ktor.client.HttpClient

interface DeviceInfo {
    fun getDeviceId(): String
}

expect fun getDeviceInfo(): DeviceInfo

//expect class KtorClientFactory {
//    fun create(): HttpClient
//}


interface ToastManager {
    fun showToast(message: String)
}

expect fun showToast(message: String)