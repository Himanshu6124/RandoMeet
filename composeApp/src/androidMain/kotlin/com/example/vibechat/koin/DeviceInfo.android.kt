package com.example.vibechat.koin

import android.content.Context
import android.provider.Settings
import android.widget.Toast
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import io.ktor.client.HttpClient
import io.ktor.client.engine.okhttp.OkHttp
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import org.koin.dsl.module
import org.koin.mp.KoinPlatform


class AndroidDeviceInfo(private val context : Context) : DeviceInfo {
    override fun getDeviceId(): String {
        return Settings.Secure.getString(context.contentResolver, Settings.Secure.ANDROID_ID)
    }

}

actual fun getDeviceInfo(): DeviceInfo {
    return AndroidDeviceInfo(KoinPlatform.getKoin().get())
}

//actual class KtorClientFactory {
//    actual fun create(): HttpClient =
//        HttpClient(OkHttp) {
//            install(ContentNegotiation) {
//                json(Json {
//                    ignoreUnknownKeys = true
//                })
//            }
//        }
//}

class AndroidToastManager(private val context: Context) : ToastManager {
    override fun showToast(message: String) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }
}

actual fun showToast(message: String) {
    AndroidToastManager(KoinPlatform.getKoin().get()).showToast(message)
}