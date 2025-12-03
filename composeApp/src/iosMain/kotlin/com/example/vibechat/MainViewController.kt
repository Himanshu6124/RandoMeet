package com.example.vibechat

import androidx.compose.ui.window.ComposeUIViewController
import com.example.vibechat.koin.iosPlatformModule
import org.koin.core.context.startKoin

fun MainViewController() = ComposeUIViewController {
    initKoin() // start Koin before Composable loads
    App()
}

fun initKoin() = startKoin { modules( iosPlatformModule ) }
