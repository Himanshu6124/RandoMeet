package com.example.vibechat.ui.screens.navigation

sealed interface Screen {
    val route: String
    val name : String

    data object SignUp : Screen {
        override val route: String
            get() = "signup"
        override val name: String
            get() = "Sign up"
    }

    data object Splash : Screen {
        override val route: String
            get() = "splash"
        override val name: String
            get() = "splash"
    }

    data object Friends : Screen {
        override val route: String
            get() = "friends"
        override val name: String
            get() = "Friends"
    }

    data object RandomMatch : Screen {
        override val route: String
            get() = "random_match"
        override val name: String
            get() = "Random Match"
    }

    data object Status : Screen {
        override val route: String
            get() = "status"
        override val name: String
            get() = "Status"
    }

    data object ChatDetail : Screen {
        override val route: String
            get() = "chat_detail"
        override val name: String
            get() = "ChatDetail"
    }
}