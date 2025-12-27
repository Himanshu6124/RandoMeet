package com.example.vibechat.ui.screens.onboardingscreen

import androidx.lifecycle.ViewModel
import com.example.vibechat.koin.DeviceInfo

class OnboardingViewModel(
    private val deviceInfo: DeviceInfo
) : ViewModel() {

    init {
        isUserOnboarded()
    }

    fun isUserOnboarded(){
        val deviceId = deviceInfo.getDeviceId()
        println("Device ID: $deviceId")
    }
}