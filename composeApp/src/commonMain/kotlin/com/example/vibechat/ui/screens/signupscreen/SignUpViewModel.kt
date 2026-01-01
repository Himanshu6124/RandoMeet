package com.example.vibechat.ui.screens.signupscreen

import androidx.lifecycle.viewModelScope
import com.example.vibechat.core.BaseViewModel
import com.example.vibechat.data.model.User
import com.example.vibechat.domain.intefaces.UserRepository
import com.example.vibechat.koin.DeviceInfo
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class SignUpViewModel(
    private val userRepository: UserRepository,
    private val deviceInfo: DeviceInfo
) : BaseViewModel<SignUpUIState,SignUpEvent,SignUpSideEffect >() {

    override val initialState: SignUpUIState
        get() = SignUpUIState()

    init {
        getProfilePictures()
    }

    override fun handleEvent(event: SignUpEvent) {
        when(event) {
            is SignUpEvent.OnSelectedGenderChange -> {
                _uiState.update {
                    it.copy(
                        selectedGender = event.selectedGender,
                        filteredPictures = _uiState.value.allPictures.filter { pic-> pic.contains(event.selectedGender.displayName,true)  } as ArrayList
                    )
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
            val res = userRepository.saveUser(
                User(
                    deviceId = deviceInfo.getDeviceId(),
                    name = uiState.value.userName,
                    gender = uiState.value.selectedGender.displayName,
                )
            )
            res?.let {
                _effect.emit(SignUpSideEffect.NavigateToMatchScreen)
            }
        }
    }

    fun getProfilePictures() {
        viewModelScope.launch {
            val pics = userRepository.getProfilePictures()
            _uiState.update {
                it.copy(
                    allPictures = pics,
                    filteredPictures = pics.filter {pic-> pic.contains(_uiState.value.selectedGender.displayName,true)  } as ArrayList
                )
            }
        }
    }
}