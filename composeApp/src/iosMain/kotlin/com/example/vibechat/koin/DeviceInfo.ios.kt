package com.example.vibechat.koin

import io.ktor.client.HttpClient
import io.ktor.client.engine.darwin.Darwin
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import platform.UIKit.UIDevice


class IOSDeviceInfo : DeviceInfo {
    override fun getDeviceId(): String {
        return UIDevice.currentDevice.identifierForVendor?.UUIDString() ?: "Unknown"
    }

}

actual fun getDeviceInfo(): DeviceInfo {
    return IOSDeviceInfo()
}


//actual class KtorClientFactory {
//    actual fun create(): HttpClient =
//        HttpClient(Darwin) {
//            install(ContentNegotiation) {
//                json(Json {
//                    ignoreUnknownKeys = true
//                })
//            }
//        }
//}
