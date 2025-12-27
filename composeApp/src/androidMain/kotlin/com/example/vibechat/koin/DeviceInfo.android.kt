package com.example.vibechat.koin

import android.content.Context
import android.provider.Settings
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