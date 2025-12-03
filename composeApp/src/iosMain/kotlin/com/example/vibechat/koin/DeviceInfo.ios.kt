package com.example.vibechat.koin

import org.koin.dsl.module
import org.koin.core.context.startKoin


class IOSDeviceInfo : DeviceInfo {
    override fun getDeviceId(): String {
        return "IOS Device Id"
    }

}

actual fun getDeviceInfo(): DeviceInfo {
    return IOSDeviceInfo()
}

val iosPlatformModule = module {
    single<DeviceInfo> { IOSDeviceInfo() }
}

fun initKoin() = startKoin { modules( iosPlatformModule ) }
