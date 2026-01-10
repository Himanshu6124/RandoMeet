package com.example.vibechat.ui.screens.loginscreen

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.lifecycle.viewModelScope
import com.example.vibechat.constants.CONSTANTS.TOKEN_KEY
import com.example.vibechat.constants.CONSTANTS.USER_ID
import com.example.vibechat.core.BaseViewModel
import com.example.vibechat.data.model.repository.AuthResponse
import com.example.vibechat.data.model.repository.LoginRequest
import com.example.vibechat.domain.intefaces.UserRepository
import kotlinx.coroutines.launch

class LoginViewModel(
    private val userRepository: UserRepository,
    private val dataStore: DataStore<Preferences>
) : BaseViewModel<LoginUIState, LoginUIEvent, LoginEffect>() {
    override val initialState: LoginUIState
        get() = LoginUIState()

    override fun handleEvent(event: LoginUIEvent) {
        when(event) {
            is LoginUIEvent.OnLoginClick -> {
                login()
            }
            is LoginUIEvent.OnPasswordChange -> {
                _uiState.value = _uiState.value.copy(password = event.password)

            }
            is LoginUIEvent.OnUserNameChange -> {
                _uiState.value = _uiState.value.copy(userName = event.userName)
            }
        }
    }

    private fun login(){
        viewModelScope.launch {
            val loginRequest = LoginRequest(
                username = _uiState.value.userName,
                password = _uiState.value.password
            )
            val res = userRepository.loginUser(loginRequest)
            println("LoginResponse: $res")
            res?.let {
                updateTokenInDataStore(res)
                _effect.emit(LoginEffect.NavigateToMatchScreen)
            }
        }
    }
    fun updateTokenInDataStore(value: AuthResponse){
        viewModelScope.launch {
            dataStore.updateData {
                it.toMutablePreferences().apply {
                    set(TOKEN_KEY, value.jwt)
                    set(USER_ID, value.userId)
                }
            }
        }
    }
}