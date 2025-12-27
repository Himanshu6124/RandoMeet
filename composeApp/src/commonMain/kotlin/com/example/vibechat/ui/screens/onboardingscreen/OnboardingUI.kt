package com.example.vibechat.ui.screens.onboardingscreen

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import kotlinx.coroutines.delay


@Composable
fun OnboardingScreen(
    goToMatchScreen: () -> Unit,
    gotoSignUpScreen: () -> Unit
){

    LaunchedEffect(Unit){
        delay(3000)
        goToMatchScreen()
    }

    Box(
        modifier = Modifier.fillMaxSize()
    ){
        Text(
            text = "Onboarding Screen",
            modifier = Modifier.align(Alignment.Center)
        )
    }
}