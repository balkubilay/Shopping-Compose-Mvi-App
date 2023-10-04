package com.example.shoppingcomposemvi.util

sealed class Screen(val route: String) {
    object Home : Screen("main")
    object Basket : Screen("basketScreen")
    object Transactions : Screen("transactionsScreen")
    object Payment : Screen("paymentScreen")
    object PaymentResult : Screen("paymentResult")
}
