package com.pascal.dansandroid.ui.navigation

sealed class Screen(val route: String) {
    data object SplashScreen: Screen("splash")
    data object LoginScreen: Screen("login")
    data object HomeScreen: Screen("home")
    data object DetailScreen: Screen("detail/{id}") {
        fun createRoute(id: String) = "detail/$id"
    }
    data object AccountScreen: Screen("account")
}