package com.example.vibechat.ui.screens.onboardingscreen

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.example.vibechat.ui.screens.onboardingscreen.OnboardingEffect.*
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest
import org.koin.compose.viewmodel.koinViewModel
import org.koin.mp.KoinPlatform.getKoin


@Composable
fun OnboardingScreen(
    goToMatchScreen: (userId: String) -> Unit,
    gotoSignUpScreen: () -> Unit
) {
    val viewModel: OnboardingViewModel = koinViewModel()
    val state = viewModel.uiState.collectAsState()


    LaunchedEffect(Unit) {
        viewModel.effect.collectLatest {
            when (it) {
                NavigateToSignupScreen ->{
                    gotoSignUpScreen()
                }

                is NavigateToRandomMatchScreen -> {
                    goToMatchScreen(it.userId)
                }
            }
        }
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