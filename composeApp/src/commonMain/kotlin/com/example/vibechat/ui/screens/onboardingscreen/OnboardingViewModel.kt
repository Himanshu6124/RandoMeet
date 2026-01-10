package com.example.vibechat.ui.screens.onboardingscreen

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.lifecycle.viewModelScope
import com.example.vibechat.core.BaseViewModel
import kotlinx.coroutines.launch

class OnboardingViewModel(
    private val dataStore: DataStore<Preferences>
) : BaseViewModel<OnboardingUIState,OnboardingUIEvent, OnboardingEffect>() {

    init {
        isUserOnboarded()
    }
    private val key = stringPreferencesKey("token")

    override val initialState: OnboardingUIState
        get() = OnboardingUIState()

    override fun handleEvent(event: OnboardingUIEvent) {

    }

    fun isUserOnboarded(){
        viewModelScope.launch {
            dataStore.data.collect { storedData ->
                val token = storedData[key]
                println("Token is $token")
                if (token == null) {
                    _effect.emit(value = OnboardingEffect.NavigateToSignupScreen)
                } else {
                    _effect.emit(value = OnboardingEffect.NavigateToRandomMatchScreen("deviceId"))
                }
            }
        }
    }
}