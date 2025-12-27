package com.example.vibechat

import androidx.compose.ui.window.ComposeUIViewController
import com.example.vibechat.koin.conversationViewModel
import com.example.vibechat.koin.platformModule
import org.koin.core.context.startKoin


fun MainViewController() = ComposeUIViewController(
    configure = {
        initKoin()
    }
) {
    App()
}

fun initKoin() = startKoin { modules(platformModule, conversationViewModel) }
