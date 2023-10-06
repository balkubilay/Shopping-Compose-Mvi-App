package com.example.shoppingcomposemvi.util

sealed class Screen(val route: String) {
    object Splash : Screen("splashScreen")
    object Home : Screen("homeScreen")
    object Basket : Screen("basketScreen")
    object Transactions : Screen("transactionsScreen")
    object Payment : Screen("paymentScreen")
    object PaymentResult : Screen("paymentResultScreen")
}
