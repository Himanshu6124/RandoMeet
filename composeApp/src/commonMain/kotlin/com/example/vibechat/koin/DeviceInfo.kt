package com.example.vibechat.koin

interface DeviceInfo {
    fun getDeviceId(): String
}

expect fun getDeviceInfo(): DeviceInfo