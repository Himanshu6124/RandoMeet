package com.example.vibechat.ui.screens.signupscreen

import androidx.compose.runtime.Immutable
import com.example.vibechat.core.utils.EMPTY
import com.example.vibechat.data.model.User

@Immutable
data class SignUpUIState(
    val loading : Boolean = false,
    val error: Exception? = null,
    val user : User? = null,
    val userName : String = String.EMPTY,
    val selectedImage : String = String.EMPTY,
    val selectedGender : GENDER = GENDER.BOY,
    val allPictures :ArrayList<String> = arrayListOf(),
    val filteredPictures :ArrayList<String> = arrayListOf()
)

enum class GENDER(val displayName: String) {
    BOY("boy"),
    GIRL("girl")
}

sealed interface SignUpEvent {
    data class OnUserNameChange(val userName: String) : SignUpEvent
    data class OnSelectedImageChange(val selectedImage: String) : SignUpEvent
    data class OnSelectedGenderChange(val selectedGender: GENDER) : SignUpEvent
    data object OnSignUpClick : SignUpEvent
}

sealed interface SignUpSideEffect {
    data object NavigateToMatchScreen : SignUpSideEffect
    data class ShowError(val error: Exception) : SignUpSideEffect
    data object NavigateToBack : SignUpSideEffect
}