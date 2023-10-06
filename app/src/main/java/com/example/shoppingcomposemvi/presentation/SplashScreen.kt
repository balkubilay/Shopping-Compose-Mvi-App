package com.example.shoppingcomposemvi.presentation

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.shoppingcomposemvi.R
import com.example.shoppingcomposemvi.util.Screen
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(
    navController: NavController
) {

    var startAnimation by remember { mutableStateOf(false) }
    val alphaAnimation = animateFloatAsState(
        targetValue = if (startAnimation) 1f else 0f,
        animationSpec = tween(
            durationMillis = 2000
        ), label = ""
    )

    LaunchedEffect(key1 = true) {
        startAnimation = true
        delay(2000)
        navController.navigate(Screen.Home.route) {
            popUpTo(Screen.Splash.route) {
                inclusive = true
            }
        }
    }

    SplashDesign(alpha = alphaAnimation.value)
}

@Composable
fun SplashDesign(alpha: Float) {
    Column(
        modifier = Modifier
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Image(
            modifier = Modifier
                .size(200.dp)
                .alpha(alpha = alpha),
            painter = painterResource(id = R.drawable.baseline_shopping_cart_24),
            contentDescription = "",
        )
    }
}