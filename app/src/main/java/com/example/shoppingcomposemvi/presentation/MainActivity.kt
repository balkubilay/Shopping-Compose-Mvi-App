package com.example.shoppingcomposemvi.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.shoppingcomposemvi.domain.model.Transactions
import com.example.shoppingcomposemvi.ui.theme.ShoppingComposeMviTheme
import com.example.shoppingcomposemvi.util.Screen
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ShoppingComposeMviTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    NavGraph()
                }
            }
        }
    }
}

@Composable
fun NavGraph() {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = Screen.Home.route) {
        composable(Screen.Home.route) { MainScreen(navController) }
        composable(Screen.Basket.route) { BasketScreen(navController) }
        composable(Screen.PaymentResult.route) {
            val transaction = navController.previousBackStackEntry?.savedStateHandle?.get<Transactions>("transaction")
            transaction?.let {
                PaymentResultScreen(navController, transaction)
            }
        }
        composable(Screen.Payment.route) { PaymentScreen(navController) }
        composable(Screen.Transactions.route) { TransactionsScreen(navController) }
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    ShoppingComposeMviTheme {
    }
}