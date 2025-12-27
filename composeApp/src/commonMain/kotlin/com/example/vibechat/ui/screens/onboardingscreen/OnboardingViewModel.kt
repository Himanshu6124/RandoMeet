package com.example.vibechat.ui.screens.onboardingscreen

import androidx.lifecycle.viewModelScope
import com.example.vibechat.core.BaseViewModel
import com.example.vibechat.koin.DeviceInfo
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class OnboardingViewModel(
    private val deviceInfo: DeviceInfo
) : BaseViewModel<OnboardingUIState,OnboardingUIEvent, OnboardingEffect>() {

    init {
        isUserOnboarded()
    }

    override val initialState: OnboardingUIState
        get() = OnboardingUIState()

    override fun handleEvent(event: OnboardingUIEvent) {

    }

    fun isUserOnboarded(){
        viewModelScope.launch {
            val deviceId = deviceInfo.getDeviceId()
            delay(1000)
            _effect.emit(value = OnboardingEffect.NavigateToRandomMatchScreen(deviceId))
        }
    }
}