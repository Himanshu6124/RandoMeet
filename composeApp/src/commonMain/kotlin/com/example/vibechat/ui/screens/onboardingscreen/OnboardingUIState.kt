package com.example.vibechat.ui.screens.onboardingscreen

import androidx.compose.runtime.Immutable
import com.example.vibechat.data.model.User

@Immutable
data class OnboardingUIState(
    val loading : Boolean = false,
    val error: Exception? = null,
    val user : User? = null,
)


sealed interface OnboardingUIEvent
sealed interface OnboardingEffect {
    object NavigateToRandomMatchScreen : OnboardingEffect
    object NavigateToSignupScreen : OnboardingEffect
}