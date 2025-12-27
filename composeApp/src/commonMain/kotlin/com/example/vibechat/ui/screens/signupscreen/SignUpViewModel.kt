package com.example.vibechat.ui.screens.signupscreen

import com.example.vibechat.core.BaseViewModel
import kotlinx.coroutines.flow.update

class SignUpViewModel : BaseViewModel<SignUpUIState,SignUpEvent,SignUpSideEffect >() {

    override val initialState: SignUpUIState
        get() = SignUpUIState()


    override fun handleEvent(event: SignUpEvent) {
        when(event) {

            is SignUpEvent.OnSelectedGenderChange -> {
                println("$TAG OnSelectedGenderChange ${event.selectedGender}")
                _uiState.update {
                    it.copy(selectedGender = event.selectedGender)
                }
            }
            is SignUpEvent.OnSelectedImageChange -> {
                _uiState.update {
                    it.copy(selectedImage = event.selectedImage)
                }
            }
            is SignUpEvent.OnSignUpClick -> {
                println("$TAG OnSignupClick")

            }
            is SignUpEvent.OnUserNameChange -> {
                _uiState.update {
                    it.copy(userName = event.userName)
                }
            }
        }
    }
}