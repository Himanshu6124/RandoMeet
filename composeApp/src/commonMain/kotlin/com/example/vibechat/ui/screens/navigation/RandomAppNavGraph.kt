package com.example.vibechat.ui.screens.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.vibechat.constants.CONSTANTS.MATCHED_CONVERSATION
import com.example.vibechat.ui.screens.chatscreen.ChatScreen
import com.example.vibechat.ui.screens.loginscreen.LoginUI
import com.example.vibechat.ui.screens.loginscreen.LoginUIState
import com.example.vibechat.ui.screens.matchscreen.ChatCardData
import com.example.vibechat.ui.screens.matchscreen.HomeScreenV2
import com.example.vibechat.ui.screens.onboardingscreen.OnboardingScreen
import com.example.vibechat.ui.screens.signupscreen.SignUpUI
import kotlinx.serialization.json.Json

@Composable
fun RandomAppNavGraph(){

    val navController = rememberNavController()
    NavHost(
        navController = navController,
        startDestination = Screen.ChatDetail.route,
    ){
        composable(route = Screen.Splash.route) {
            OnboardingScreen(
                goToMatchScreen = { userId->
                    navController.navigate(Screen.RandomMatch.route)
                },
                gotoSignUpScreen = {
                    navController.navigate(Screen.SignUp.route)
                }
            )
        }

        composable(route = Screen.SignUp.route) {
            SignUpUI(
                onSignUpSuccess = {
                    navController.navigate(Screen.RandomMatch.route)
                },
                onNavigateToLoginScreen = {
                    navController.navigate(Screen.Login.route)
                }
            )
        }

        composable(route = Screen.Login.route) {
            LoginUI(
                onNavigateToMatchScreen = {
                    navController.navigate(Screen.RandomMatch.route)

                },
                onNavigateToSignUpScreen = {
                    navController.navigate(Screen.SignUp.route)
                }
            )
        }
        composable(route = Screen.RandomMatch.route) {
            HomeScreenV2(
                onMatchFound = {
                    val json = Json.encodeToString(it)
                    navController.currentBackStackEntry?.savedStateHandle?.set(
                        MATCHED_CONVERSATION, json
                    )
                    navController.navigate(Screen.ChatDetail.route)
                }
            )
        }

        composable(route = Screen.ChatDetail.route) {
            val json = navController.previousBackStackEntry?.savedStateHandle?.get<String>(MATCHED_CONVERSATION)
//            json?.let {
//                val chat = Json.decodeFromString<ChatCardData>(json)
                ChatScreen(
                    userId = "12",
                    isRandomMatch = true,
                    chat = null,
                    navigateBack = { navController.navigateUp() }
                )
//            }
        }
    }
}