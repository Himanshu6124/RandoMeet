package com.example.vibechat.koin

import com.example.vibechat.ui.screens.ConversationsViewModel
import com.example.vibechat.ui.screens.onboardingscreen.OnboardingViewModel
import com.example.vibechat.ui.screens.signupscreen.SignUpViewModel
import org.koin.core.module.Module
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

expect val platformModule : Module

val conversationViewModel = module {
    viewModel { ConversationsViewModel() }
}

val onboardingViewModel = module {
    viewModel { OnboardingViewModel(get()) }
}
val signUpViewModel = module {
    viewModel { SignUpViewModel() }
}