package com.example.vibechat.ui.screens.signupscreen

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

@Composable
fun SignUpUI(
    onSignUpSuccess: () -> Unit
){
    Box(
        modifier = Modifier.fillMaxSize()
    ){
        Text(
            text = "Sign Up Screen",
            modifier = Modifier.align(Alignment.Center)
        )
    }
}