package com.example.vibechat.koin

import org.koin.dsl.module


class AndroidDeviceInfo : DeviceInfo {
    override fun getDeviceId(): String {
        return "Android Device ID"
    }

}

actual fun getDeviceInfo(): DeviceInfo {
    return AndroidDeviceInfo()
}

val androidPlatformModule = module {
    single<DeviceInfo> { AndroidDeviceInfo() }
}
