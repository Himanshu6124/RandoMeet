package com.example.vibechat.ui.screens.onboardingscreen

import androidx.compose.runtime.Immutable
import com.example.vibechat.data.model.User

@Immutable
data class OnboardingUIState(
    val loading : Boolean = false,
    val error: Exception? = null,
    val user : User? = null,
    val userName : String = "",
    val selectedImage : String = "",
    val selectedGender : String = "boy",
    val allPictures :ArrayList<String> = arrayListOf(),
    val filteredPictures :ArrayList<String> = arrayListOf()
)


sealed interface UIEvent {
    data class OnUserNameChange(val userName: String) : UIEvent
    data class OnSelectedImageChange(val selectedImage: String) : UIEvent
    data class OnSelectedGenderChange(val selectedGender: String) : UIEvent
    object OnNextClick : UIEvent
    object OnBackClick : UIEvent
}