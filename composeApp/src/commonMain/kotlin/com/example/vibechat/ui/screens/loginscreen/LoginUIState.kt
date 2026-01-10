package com.example.vibechat.ui.screens.loginscreen

import com.example.vibechat.core.utils.EMPTY

data class LoginUIState(
    val userName: String = String.EMPTY,
    val password: String = String.EMPTY,
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)

sealed interface LoginUIEvent {
    data class OnUserNameChange(val userName: String): LoginUIEvent
    data class OnPasswordChange(val password: String): LoginUIEvent
    object OnLoginClick: LoginUIEvent
}

sealed interface LoginEffect {
    data object NavigateToMatchScreen: LoginEffect
    data class NavigateToSignUpScreen(val message: String): LoginEffect
    data class ShowError(val message: String): LoginEffect
}