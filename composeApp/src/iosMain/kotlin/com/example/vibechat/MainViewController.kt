package com.example.vibechat

import androidx.compose.ui.window.ComposeUIViewController
import com.example.vibechat.koin.chatRepo
import com.example.vibechat.koin.conversationViewModel
import com.example.vibechat.koin.onboardingViewModel
import com.example.vibechat.koin.platformModule
import com.example.vibechat.koin.provideHttpClientModule
import com.example.vibechat.koin.randomMatchViewModel
import com.example.vibechat.koin.signUpViewModel
import org.koin.core.context.startKoin


fun MainViewController() = ComposeUIViewController(
    configure = {
        initKoin()
    }
) {
    App()
}

fun initKoin() = startKoin { modules(
    platformModule,
    conversationViewModel,
    onboardingViewModel,
    randomMatchViewModel,
    signUpViewModel,
    provideHttpClientModule,
    chatRepo
) }
