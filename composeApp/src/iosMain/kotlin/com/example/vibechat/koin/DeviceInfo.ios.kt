package com.example.vibechat.koin

import platform.UIKit.UIDevice


class IOSDeviceInfo : DeviceInfo {
    override fun getDeviceId(): String {
        return UIDevice.currentDevice.identifierForVendor?.UUIDString() ?: "Unknown"
    }

}

actual fun getDeviceInfo(): DeviceInfo {
    return IOSDeviceInfo()
}

