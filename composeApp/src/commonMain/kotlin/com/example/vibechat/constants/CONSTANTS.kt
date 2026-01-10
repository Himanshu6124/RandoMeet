package com.example.vibechat.constants

import androidx.datastore.preferences.core.stringPreferencesKey

object CONSTANTS {

    const val MATCHED_CONVERSATION = "matched_conversation"
    val TOKEN_KEY = stringPreferencesKey("token")
    val USER_ID = stringPreferencesKey("user_id")

}