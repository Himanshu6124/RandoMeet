package com.example.vibechat.ui.screens.signupscreen

import androidx.lifecycle.viewModelScope
import com.example.vibechat.core.BaseViewModel
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

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
                    handleSignUpClick()
            }
            is SignUpEvent.OnUserNameChange -> {
                _uiState.update {
                    it.copy(userName = event.userName)
                }
            }
        }
    }
    fun handleSignUpClick() {
        viewModelScope.launch {
            _effect.emit(SignUpSideEffect.NavigateToChat(
                userId = uiState.value.userName
            ))
        }
    }

    fun postUser(userId : String){
        // To DO
    }
}