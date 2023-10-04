package com.example.shoppingcomposemvi.presentation

import android.annotation.SuppressLint
import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import coil.compose.SubcomposeAsyncImage
import com.example.shoppingcomposemvi.R
import com.example.shoppingcomposemvi.domain.model.Basket
import com.example.shoppingcomposemvi.presentation.contract.BasketContract
import com.example.shoppingcomposemvi.presentation.viewmodel.BasketViewModel
import com.example.shoppingcomposemvi.util.Screen
import java.text.DecimalFormat

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter", "RememberReturnType")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BasketScreen(
    navController: NavHostController,
    viewModel: BasketViewModel = hiltViewModel()
) {
    val openAlertDialog = remember { mutableStateOf(false) }
    val state by viewModel.uiState.collectAsState()
    var basket: Basket? by remember { mutableStateOf(null) }
    val decimalFormat = DecimalFormat("0.00")


    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                modifier = Modifier.fillMaxWidth(),
                title = {
                    Text(
                        text = "Sepetim",
                        modifier = Modifier
                            .fillMaxWidth()
                            .wrapContentWidth(Alignment.CenterHorizontally)
                    )
                },
                navigationIcon = {
                    IconButton(
                        onClick = {
                            navController.navigate(Screen.Home.route)
                        }
                    ) {
                        Icon(imageVector = Icons.Default.ArrowBack, contentDescription = null)
                    }
                },
                actions = {
                    basket?.let {
                        IconButton(
                            onClick = {
                                openAlertDialog.value = !openAlertDialog.value
                            }
                        ) {
                            Icon(
                                painter = painterResource(id = R.drawable.delete_icon),
                                contentDescription = null
                            )
                        }
                    }
                },
                colors = TopAppBarDefaults.smallTopAppBarColors(
                    containerColor = colorResource(id = R.color.purple_200),
                    titleContentColor = colorResource(id = R.color.white),
                    actionIconContentColor = colorResource(id = R.color.white),
                    navigationIconContentColor = colorResource(id = R.color.white)
                )
            )
        },
        content = { paddingValue ->
            when (state.basketState) {
                is BasketContract.BasketState.Idle -> {
                    viewModel.setEvent(BasketContract.Event.OnFetchBasket)
                }

                is BasketContract.BasketState.Loading -> {
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        CircularProgressIndicator(
                            color = colorResource(id = R.color.purple_200),
                            strokeWidth = 8.dp,
                            modifier = Modifier.size(65.dp)
                        )
                    }
                }

                is BasketContract.BasketState.Success -> {
                    basket = (state.basketState as BasketContract.BasketState.Success).basket
                }

                is BasketContract.BasketState.Error -> {
                    basket = null
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Text(text = "${(state.basketState as BasketContract.BasketState.Error).message}")
                    }
                }
            }
            basket?.let {
                BasketContent(paddingValue, basket!!, viewModel, openAlertDialog, decimalFormat)
            }
            BackHandler {
                navController.navigate(Screen.Home.route) {
                    popUpTo(Screen.Home.route) {
                        inclusive = true
                    }
                }
            }
        },
        bottomBar = {
            val totalAmount = decimalFormat.format(basket?.totalAmount ?: 0.00)
            BottomAppBar {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(
                        modifier = Modifier
                            .weight(1f)
                            .padding(end = 4.dp),
                        horizontalAlignment = Alignment.Start
                    ) {
                        Text(
                            text = "Toplam Tutar",
                            modifier = Modifier.wrapContentWidth(Alignment.Start)
                        )
                        Text(
                            text = "$totalAmount TL",
                            modifier = Modifier.wrapContentWidth(Alignment.Start),
                            fontWeight = FontWeight.Bold
                        )
                    }
                    basket?.let {
                        Button(
                            onClick = {
                                navController.navigate(Screen.Payment.route)
                            }
                        ) {
                            Text(text = "Sepeti Onayla")
                        }
                    }
                }
            }
        }
    )
}

@Composable
private fun BasketContent(
    it: PaddingValues,
    basket: Basket,
    viewModel: BasketViewModel,
    openAlertDialog: MutableState<Boolean>,
    decimalFormat: DecimalFormat
) {
    val context = LocalContext.current

    LazyColumn(modifier = Modifier.padding(it)) {
        basket.products.let { productsInBasket ->
            items(count = productsInBasket.count(), itemContent = { index ->
                val item = productsInBasket[index]
                val itemPrice = decimalFormat.format(item.productPrice * item.productCount)
                Card(
                    modifier = Modifier
                        .padding(all = 5.dp)
                        .fillMaxWidth(),
                    elevation = CardDefaults.cardElevation(defaultElevation = 5.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(100.dp)
                                .background(Color.Gray)
                        ) {
                            SubcomposeAsyncImage(
                                model = item.productImageUrl,
                                loading = { CircularProgressIndicator(
                                    modifier = Modifier.padding(25.dp),
                                    color = colorResource(id = R.color.purple_200),
                                    strokeWidth = 4.dp,
                                ) },
                                contentDescription = "",
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .background(Color.White)
                                    .height(200.dp)
                            )
                        }

                        Spacer(modifier = Modifier.width(16.dp))

                        Column(
                            modifier = Modifier
                                .fillMaxHeight()
                                .weight(1f),
                            verticalArrangement = Arrangement.Center
                        ) {
                            Text(
                                text = item.productName,
                                modifier = Modifier
                                    .wrapContentWidth(Alignment.CenterHorizontally),
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = "$itemPrice TL",
                                modifier = Modifier
                                    .wrapContentWidth(Alignment.CenterHorizontally),
                            )

                            Spacer(modifier = Modifier.height(16.dp))

                            if (item.productCount == 0) {
                                Button(
                                    onClick = {
                                        viewModel.setEvent(
                                            BasketContract.Event.IncreaseProductCount(
                                                basket,
                                                item
                                            )
                                        )
                                    },
                                    modifier = Modifier
                                        .wrapContentWidth(Alignment.CenterHorizontally)
                                ) {
                                    Text(text = "+")
                                }
                            } else {
                                Row(
                                    modifier = Modifier.wrapContentWidth(Alignment.CenterHorizontally)
                                ) {
                                    Button(
                                        onClick = {
                                            viewModel.setEvent(
                                                BasketContract.Event.DecreaseProductCount(
                                                    basket,
                                                    item
                                                )
                                            )
                                        },
                                    ) {
                                        Text(text = "-")
                                    }
                                    Text(
                                        text = item.productCount.toString(),
                                        modifier = Modifier.padding(all = 10.dp)
                                    )
                                    Button(
                                        onClick = {
                                            if (item.productCount < 5) {
                                                viewModel.setEvent(
                                                    BasketContract.Event.IncreaseProductCount(
                                                        basket,
                                                        item
                                                    )
                                                )
                                            } else {
                                                Toast.makeText(
                                                    context,
                                                    "Bu üründen maksimum 5 adet sepete eklenebilir.",
                                                    Toast.LENGTH_LONG
                                                ).show()
                                            }
                                        },
                                    ) {
                                        Text(text = "+")
                                    }
                                }
                            }
                        }
                    }
                }

            })
        }
    }
    when {
        openAlertDialog.value -> {
            CustomAlertDialog(
                onDismissRequest = { openAlertDialog.value = false },
                onConfirmation = {
                    viewModel.setEvent(BasketContract.Event.ClearBasket)
                    openAlertDialog.value = false
                },
                dialogText = "Ürünleri silmek istediğinize emin misiniz?",
                positiveButtonText = "Evet, Sil",
                negativeButtonText = "İptal"
            )
        }
    }
}

