package com.example.vibechat.koin

import com.example.vibechat.datastore.createDataStore
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.Module
import org.koin.dsl.module

actual val platformModule: Module
    get() = module {
        single<DeviceInfo> { AndroidDeviceInfo(get()) }
        single<ToastManager> { AndroidToastManager(get()) }
    }

actual val dataStoreModule: Module
    get() = module { single { createDataStore(androidContext()) } }