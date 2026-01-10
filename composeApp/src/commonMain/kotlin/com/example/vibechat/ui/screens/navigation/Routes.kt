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

    data object Login : Screen {
        override val route: String
            get() = "login"
        override val name: String
            get() = "Log in"
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
            get() = "random_match/{user_id}"
        override val name: String
            get() = "Random Match"

        fun createRoute(id: String): String {
            return "random_match/$id"
        }
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

        fun createRoute(id: String): String {
            return "random_match/$id"
        }
    }
}