package com.example.vibechat.ui.screens.loginscreen

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.flow.collectLatest
import org.koin.compose.viewmodel.koinViewModel


@Composable
fun LoginUI(
    onNavigateToMatchScreen: () -> Unit,
    onNavigateToSignUpScreen: () -> Unit
){
    val viewModel : LoginViewModel = koinViewModel()
    val uiState = viewModel.uiState.collectAsState().value

    LaunchedEffect(Unit){
        viewModel.effect.collectLatest {
            when(it){
                LoginEffect.NavigateToMatchScreen -> {
                    onNavigateToMatchScreen()
                }
                is LoginEffect.ShowError -> {

                }

                is LoginEffect.NavigateToSignUpScreen -> {
                    onNavigateToSignUpScreen()
                }
            }
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {

        // Gradient Background
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(260.dp)
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            MaterialTheme.colorScheme.primary,
                            MaterialTheme.colorScheme.primaryContainer
                        )
                    )
                )
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 140.dp, start = 20.dp, end = 20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(24.dp),
                elevation = CardDefaults.cardElevation(10.dp)
            ) {
                Column(
                    modifier = Modifier.padding(20.dp),
                    verticalArrangement = Arrangement.spacedBy(18.dp)
                ) {

                    Text(
                        text = "Sign in",
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Bold
                    )

                    OutlinedTextField(
                        value = uiState.userName,
                        onValueChange = {
                            viewModel.handleEvent(
                                LoginUIEvent.OnUserNameChange(it)
                            )
                        },
                        label = { Text("Your user name") },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(14.dp),
                        singleLine = true
                    )

                    OutlinedTextField(
                        value = uiState.password,
                        visualTransformation = PasswordVisualTransformation(),
                        onValueChange = {
                            viewModel.handleEvent(
                                LoginUIEvent.OnPasswordChange(it)
                            )
                        },
                        label = { Text("Your password") },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(14.dp),
                        singleLine = true
                    )

                    Text(
                        text = "Don't have an account?",
                        color = Color.Blue,
                        modifier = Modifier
                            .align(Alignment.End)
                            .clickable{onNavigateToSignUpScreen()}
                        ,
                    )

                    Button(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(54.dp),
                        shape = RoundedCornerShape(30.dp),
                        enabled = uiState.userName.isNotEmpty() && uiState.password.isNotEmpty(),
                        onClick = {
                            viewModel.handleEvent(LoginUIEvent.OnLoginClick)
                        }
                    ) {
                        Text(
                            text = "Get Started",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }

        if (uiState.isLoading) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = 0.35f)),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(color = Color.White)
            }
        }
    }
}
