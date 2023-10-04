package com.example.shoppingcomposemvi.presentation

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.shoppingcomposemvi.R
import com.example.shoppingcomposemvi.domain.model.Transactions
import com.example.shoppingcomposemvi.util.Screen

@Composable
fun PaymentResultScreen(navController: NavHostController, transaction: Transactions) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .clickable {
                navController.navigate(Screen.Home.route) {
                    popUpTo(Screen.Home.route) {
                        inclusive = true
                    }
                }
            },
        verticalArrangement = Arrangement.SpaceEvenly,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(id = R.drawable.ok_circle_svgrepo_com),
            contentDescription = "",
            modifier = Modifier.size(180.dp)
        )
        Text(text = "Ödeme Tamamlandı", fontSize = 30.sp)
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = "Sipariş Numarası: ${transaction.orderId}",
                modifier = Modifier.padding(bottom = 3.dp),
                fontSize = 20.sp
            )
            Text(
                text = transaction.maskedCardNo,
                modifier = Modifier.padding(bottom = 3.dp),
                fontSize = 20.sp
            )
            Text(
                text = "Tutar: ${transaction.totalAmount}",
                modifier = Modifier.padding(bottom = 3.dp),
                fontSize = 20.sp
            )
            Text(
                text = transaction.txnTime,
                modifier = Modifier.padding(bottom = 3.dp),
                fontSize = 20.sp
            )
        }
    }
}