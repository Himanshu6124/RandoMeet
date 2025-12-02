package com.example.vibechat.ui.viewmodels.uiStates

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