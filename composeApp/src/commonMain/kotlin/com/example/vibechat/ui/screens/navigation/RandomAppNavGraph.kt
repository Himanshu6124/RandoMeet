package com.example.vibechat.ui.screens.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.vibechat.constants.CONSTANTS.MATCHED_CONVERSATION
import com.example.vibechat.ui.screens.chatscreen.ChatScreen
import com.example.vibechat.ui.screens.friendsscreen.FriendsScreenUI
import com.example.vibechat.ui.screens.loginscreen.LoginUI
import com.example.vibechat.ui.screens.matchscreen.Conversation
import com.example.vibechat.ui.screens.matchscreen.HomeScreenV2
import com.example.vibechat.ui.screens.onboardingscreen.OnboardingScreen
import com.example.vibechat.ui.screens.signupscreen.SignUpUI
import kotlinx.serialization.json.Json

@Composable
fun RandomAppNavGraph(){

    val navController = rememberNavController()
    var selectedTab by remember { mutableStateOf("Match") }

    NavHost(
        navController = navController,
        startDestination = Screen.Splash.route,
    ){
        composable(route = Screen.Splash.route) {
            OnboardingScreen(
                goToMatchScreen = { userId ->
                    navController.navigate(Screen.RandomMatch.route) {
                        popUpTo(Screen.Splash.route) {
                            inclusive = true
                        }
                    }
                },
                gotoSignUpScreen = {
                    navController.navigate(Screen.SignUp.route) {
                        popUpTo(Screen.Splash.route) {
                            inclusive = true
                        }
                    }
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
                selectedTab = selectedTab,
                onMatchFound = {
                    val json = Json.encodeToString(it)
                    navController.currentBackStackEntry?.savedStateHandle?.set(
                        MATCHED_CONVERSATION, json
                    )
                    navController.navigate(Screen.ChatDetail.createRoute(true))
                },
                onTabChange = {
                    selectedTab = it
                    when (it) {
                        "Match" -> navController.navigate(Screen.RandomMatch.route) {
                            launchSingleTop = true
                        }
                        "Friends" -> navController.navigate(Screen.FriendsScreen.route) {
                            launchSingleTop = true
                        }
                    }
                }

            )
        }

        composable(
            route = Screen.ChatDetail.route,
            arguments = listOf(
                navArgument("is_random") {
                    type = NavType.BoolType
                }
            )
        ) { backStackEntry->
            val json = navController.previousBackStackEntry?.savedStateHandle?.get<String>(MATCHED_CONVERSATION)
            val isRandom: Boolean = backStackEntry.savedStateHandle["is_random"] ?: false
            json?.let {
                val chat = Json.decodeFromString<Conversation>(json)
                ChatScreen(
                    selectedTab = selectedTab,
                    isRandomMatch = isRandom,
                    chat = chat,
                    navigateBack = { navController.navigateUp() },
                    navigateToMatchScreen = {
                        navController.navigate(Screen.RandomMatch.route)
                    },
                    onTabChange = {
                        selectedTab = it
                        when (it) {
                            "Match" -> navController.navigate(Screen.RandomMatch.route) {
                                launchSingleTop = true
                            }
                            "Friends" -> navController.navigate(Screen.FriendsScreen.route) {
                                launchSingleTop = true
                            }
                        }
                    }

                )
            }
        }

        composable(
            route = Screen.FriendsScreen.route,
        ) {
            FriendsScreenUI(
                selectedTab = selectedTab,
                onFriendClick = {
                    val json = Json.encodeToString(it)
                    navController.currentBackStackEntry?.savedStateHandle?.set(
                        MATCHED_CONVERSATION, json
                    )
                    navController.navigate(Screen.ChatDetail.createRoute(false))
                },
                onBackPress = {
                    navController.navigateUp()
                },
                onTabChange = {
                    selectedTab = it
                    when (it) {
                        "Match" -> navController.navigate(Screen.RandomMatch.route) {
                            launchSingleTop = true
                        }
                        "Friends" -> navController.navigate(Screen.FriendsScreen.route) {
                            launchSingleTop = true
                        }
                    }
                }
            )
        }
    }
}