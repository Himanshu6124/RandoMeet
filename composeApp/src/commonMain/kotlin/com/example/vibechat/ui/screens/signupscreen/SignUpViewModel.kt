package com.example.vibechat.ui.screens.signupscreen

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.lifecycle.viewModelScope
import com.example.vibechat.constants.CONSTANTS.TOKEN_KEY
import com.example.vibechat.constants.CONSTANTS.USER_ID
import com.example.vibechat.core.BaseViewModel
import com.example.vibechat.data.model.User
import com.example.vibechat.data.model.repository.AuthResponse
import com.example.vibechat.domain.intefaces.UserRepository
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class SignUpViewModel(
    private val userRepository: UserRepository,
    private val dataStore: DataStore<Preferences>
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

            is SignUpEvent.OnNameChange -> {
                _uiState.update {
                    it.copy(name = event.name)
                }
            }
            is SignUpEvent.OnPasswordChange -> {
                _uiState.update {
                    it.copy(password = event.password)
                }
            }
        }
    }
    fun handleSignUpClick() {
        viewModelScope.launch {
            val res = userRepository.saveUser(
                User(
                    name = _uiState.value.name,
                    username = _uiState.value.userName,
                    password = _uiState.value.password,
                    gender = _uiState.value.selectedGender.displayName,
                    email = "email_hp@gmail.com"
                )
            )
            print("Response of sign up is $res")
            res?.let {
                _effect.emit(SignUpSideEffect.NavigateToMatchScreen)
                updateTokenInDataStore(res)
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