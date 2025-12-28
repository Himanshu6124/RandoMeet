package com.example.vibechat.koin

import com.example.vibechat.data.model.repository.ChatRepo
import com.example.vibechat.data.model.repository.SocketRepo
import com.example.vibechat.ui.screens.ConversationsViewModel
import com.example.vibechat.ui.screens.chatscreen.ChatScreenViewModel
import com.example.vibechat.ui.screens.matchscreen.RandomMatchViewModel
import com.example.vibechat.ui.screens.onboardingscreen.OnboardingViewModel
import com.example.vibechat.ui.screens.signupscreen.SignUpViewModel
import io.ktor.client.HttpClient
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.http.ContentType
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import org.koin.core.module.Module
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

expect val platformModule : Module

val conversationViewModel = module {
    viewModel { ConversationsViewModel(get()) }
}

val onboardingViewModel = module {
    viewModel { OnboardingViewModel(get()) }
}
val signUpViewModel = module {
    viewModel { SignUpViewModel() }
}

val chatViewModel = module {
    viewModel { ChatScreenViewModel(get(),get()) }
}
val randomMatchViewModel = module {
    viewModel { RandomMatchViewModel(get(),get()) }
}

val provideHttpClientModule = module {
    single {
        HttpClient {
            install(ContentNegotiation) {
                json(json = Json { ignoreUnknownKeys = true }, contentType = ContentType.Any)
            }
        }
    }
}

val chatRepo = module {
    single<ChatRepo> { ChatRepo(get()) }
}
val socketRepo = module {
    single<SocketRepo> { SocketRepo(get()) }
}