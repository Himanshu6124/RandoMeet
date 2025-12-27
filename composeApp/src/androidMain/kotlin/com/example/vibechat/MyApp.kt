package com.example.vibechat

import android.app.Application
import com.example.vibechat.koin.conversationViewModel
import com.example.vibechat.koin.platformModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class MyApp : Application() {
    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidContext(this@MyApp)  // ⬅️ This provides 'context'
            modules(
                platformModule,
                conversationViewModel
            )
        }
    }
}