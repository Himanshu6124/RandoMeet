package com.example.vibechat.ui.screens.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.vibechat.HomeScreenV2
import com.example.vibechat.ui.screens.onboardingscreen.OnboardingScreen
import com.example.vibechat.ui.screens.signupscreen.SignUpUI

@Composable
fun RandomAppNavGraph(){

    val navController = rememberNavController()
    NavHost(
        navController = navController,
        startDestination = Screen.Splash.route,
    ){
        composable(route = Screen.Splash.route) {
            OnboardingScreen(
                goToMatchScreen = {
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
                }
            )
        }
        composable(route = Screen.RandomMatch.route) {
            HomeScreenV2()
        }
    }
}