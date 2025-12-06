package com.example.vibechat.koin

import com.example.vibechat.ui.viewmodels.ConversationsViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

fun appModule(){
    val platformModule = module { }
}

val conversationViewModel = module {
    viewModel { ConversationsViewModel() }
}