package com.example.vibechat.data.model.repository

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.example.vibechat.data.model.User
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.serialization.json.Json
class UserDataStore(private val dataStore: DataStore<Preferences>) {

    private val json = Json {
        ignoreUnknownKeys = true
        isLenient = true
        encodeDefaults = true
    }

    companion object {
        private val USER_KEY = stringPreferencesKey("user_data")
    }

    // Save user
    suspend fun saveUser(user: User?) {
        dataStore.edit { preferences ->
            if (user != null) {
                preferences[USER_KEY] = json.encodeToString(user)
            } else {
                preferences.remove(USER_KEY)
            }
        }
    }

    // Get user as suspend function (one-time read)
    suspend fun getUser(): User? {
        return dataStore.data.map { preferences ->
            preferences[USER_KEY]?.let {
                json.decodeFromString<User>(it)
            }
        }.first()
    }

    // Get user as Flow (reactive)
    fun getUserFlow(): Flow<User?> {
        return dataStore.data.map { preferences ->
            preferences[USER_KEY]?.let {
                json.decodeFromString<User>(it)
            }
        }
    }

    // Clear user
    suspend fun clearUser() {
        dataStore.edit { preferences ->
            preferences.clear()
        }
    }

    // Check if user exists
    suspend fun hasUser(): Boolean {
        return getUser() != null
    }
}