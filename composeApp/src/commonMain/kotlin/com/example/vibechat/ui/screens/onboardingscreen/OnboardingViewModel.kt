package com.example.vibechat.ui.screens.onboardingscreen

import androidx.lifecycle.viewModelScope
import com.example.vibechat.core.BaseViewModel
import com.example.vibechat.data.model.repository.ChatRepo
import com.example.vibechat.domain.intefaces.UserRepository
import com.example.vibechat.koin.DeviceInfo
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class OnboardingViewModel(
    private val deviceInfo: DeviceInfo,
    private val userRepository: UserRepository
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
            val user = userRepository.getUser(deviceId)
            println("$TAG user is $user")
            if(user == null){
                _effect.emit(value = OnboardingEffect.NavigateToSignupScreen)
            }else{
                _effect.emit(value = OnboardingEffect.NavigateToRandomMatchScreen(deviceId))
            }
        }
    }
}