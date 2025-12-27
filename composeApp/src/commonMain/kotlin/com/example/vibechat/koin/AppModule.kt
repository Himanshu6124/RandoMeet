package com.example.vibechat.koin

import com.example.vibechat.ui.viewmodels.ConversationsViewModel
import org.koin.core.module.Module
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

expect val platformModule : Module

val conversationViewModel = module {
    viewModel { ConversationsViewModel() }
}