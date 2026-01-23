package com.example.vibechat.data.model.network

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import com.example.vibechat.constants.CONSTANTS
import io.ktor.client.plugins.api.createClientPlugin
import io.ktor.http.HttpHeaders
import kotlinx.coroutines.flow.first

val AuthPlugin = createClientPlugin("AuthPlugin", ::AuthPluginConfig) {
    val dataStore = pluginConfig.dataStore
    
    onRequest { request, _ ->
        val token = dataStore.data.first()[CONSTANTS.TOKEN_KEY]
        if (!token.isNullOrEmpty()) {
            request.headers.append(HttpHeaders.Authorization, "Bearer $token")
        }
    }
}

class AuthPluginConfig {
    lateinit var dataStore: DataStore<Preferences>
}
