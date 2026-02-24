package com.example.e_concalcmobile.navigation

sealed class Screen(val route: String) {
    object Splash : Screen("splash")
    object Login : Screen("login")
    object Register : Screen("register")
    object Calculator : Screen("calculator")
    object Converter : Screen("converter")
    object Currency : Screen("currency")
}
