package com.example.vibechat.koin

import com.example.vibechat.ui.screens.onboardingscreen.OnboardingViewModel
import org.koin.core.module.Module
import org.koin.dsl.module

actual val platformModule: Module
    get() = module {
        single<DeviceInfo> { AndroidDeviceInfo(get()) }
        single<ToastManager> { AndroidToastManager(get()) }
    }