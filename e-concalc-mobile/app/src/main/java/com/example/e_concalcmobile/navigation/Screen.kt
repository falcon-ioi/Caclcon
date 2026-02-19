package com.example.e_concalcmobile.navigation

sealed class Screen(val route: String) {
    object Calculator : Screen("calculator")
    object Converter : Screen("converter")
    object Currency : Screen("currency")
}
