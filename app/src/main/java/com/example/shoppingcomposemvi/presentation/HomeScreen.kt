package com.example.shoppingcomposemvi.presentation

import android.annotation.SuppressLint
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
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
import com.example.shoppingcomposemvi.domain.model.Product
import com.example.shoppingcomposemvi.presentation.contract.HomeContract
import com.example.shoppingcomposemvi.presentation.viewmodel.HomeViewModel
import com.example.shoppingcomposemvi.util.Constants.BottomBar
import com.example.shoppingcomposemvi.util.Screen

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun MainScreen(
    navController: NavHostController,
    viewModel: HomeViewModel = hiltViewModel()
) {
    val bottomBarItems = listOf(BottomBar.MAIN, BottomBar.BASKET, BottomBar.TRANSACTIONS)
    val selectedBottomItem = remember { mutableIntStateOf(0) }

    var productList: List<Product>? by remember { mutableStateOf(emptyList()) }

    val onProductListChanged: (List<Product>?) -> Unit = { newList ->
        productList = newList
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = "ShoppingApp") },
                colors = TopAppBarDefaults.smallTopAppBarColors(
                    containerColor = colorResource(id = R.color.purple_200),
                    titleContentColor = colorResource(id = R.color.white)
                )
            )
        },
        content = {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(it),
                Arrangement.Top,
                Alignment.CenterHorizontally
            ) {
                when (selectedBottomItem.intValue) {
                    0 -> MainContent(productList, viewModel, onProductListChanged)
                    1 -> navController.navigate(Screen.Basket.route)
                    2 -> navController.navigate(Screen.Transactions.route)
                }
            }
        },
        bottomBar = {
            NavigationBar {
                bottomBarItems.forEachIndexed { index, item ->
                    NavigationBarItem(
                        selected = selectedBottomItem.intValue == index,
                        onClick = { selectedBottomItem.intValue = index },
                        icon = {
                            when (item) {
                                BottomBar.MAIN -> Icon(
                                    painter = painterResource(id = R.drawable.home_icon),
                                    contentDescription = ""
                                )

                                BottomBar.BASKET -> BadgedBox(badge = {
                                    Badge {
                                        val productCount = productList?.sumOf { it.productCount }
                                        Text(text = productCount.toString())
                                    }
                                }) {
                                    Icon(
                                        painter = painterResource(id = R.drawable.basket_icon),
                                        contentDescription = ""
                                    )
                                }

                                BottomBar.TRANSACTIONS -> Icon(
                                    painter = painterResource(id = R.drawable.list_icon),
                                    contentDescription = ""
                                )
                            }
                        }
                    )
                }
            }
        }
    )
}


@SuppressLint("CoroutineCreationDuringComposition")
@Composable
fun MainContent(
    productList: List<Product>?,
    viewModel: HomeViewModel,
    onProductListChanged: (List<Product>?) -> Unit
) {
    val state by viewModel.uiState.collectAsState()
    val context = LocalContext.current

    when (state.homeState) {
        is HomeContract.HomeState.Idle -> {
            viewModel.setEvent(HomeContract.Event.OnFetchProducts)
        }
        is HomeContract.HomeState.Loading -> {
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
        is HomeContract.HomeState.Success -> {
            onProductListChanged((state.homeState as HomeContract.HomeState.Success).products)
        }
        is HomeContract.HomeState.Error -> {
            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(text = "${(state.homeState as HomeContract.HomeState.Error).message}")
            }
        }
    }

    productList?.let {
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            modifier = Modifier.padding(top = 5.dp),
            content = {
                items(it.size) { index ->
                    val item = it[index]
                    Card(
                        modifier = Modifier
                            .padding(4.dp)
                            .fillMaxWidth(),
                        elevation = CardDefaults.cardElevation(defaultElevation = 5.dp)
                    ) {
                        Column(Modifier.fillMaxSize()) {
                            SubcomposeAsyncImage(
                                model = item.productImageUrl,
                                loading = { CircularProgressIndicator(
                                    modifier = Modifier.padding(55.dp),
                                    color = colorResource(id = R.color.purple_200),
                                    strokeWidth = 8.dp,
                                ) },
                                contentDescription = "",
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .background(Color.White)
                                    .height(200.dp)
                            )
                            Column(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(bottom = 5.dp),
                                Arrangement.Top,
                                Alignment.CenterHorizontally
                            ) {
                                Text(
                                    text = item.productName,
                                    fontWeight = FontWeight.Bold
                                )
                                Text(text = item.productPrice.toString() + " TL")
                            }
                            if (item.productCount == 0) {
                                Button(
                                    onClick = {
                                        viewModel.setEvent(
                                            HomeContract.Event.IncreaseProductCount(
                                                item
                                            )
                                        )
                                    },
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .wrapContentWidth(Alignment.End)
                                        .wrapContentHeight(Alignment.Bottom)
                                ) {
                                    Text(text = "+")
                                }
                            } else {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceEvenly,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Button(
                                        onClick = {
                                            viewModel.setEvent(
                                                HomeContract.Event.DecreaseProductCount(
                                                    item
                                                )
                                            )
                                        }
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
                                                    HomeContract.Event.IncreaseProductCount(
                                                        item
                                                    )
                                                )
                                            } else {
                                                Toast.makeText(context, "Bu üründen maksimum 5 adet sepete eklenebilir.", Toast.LENGTH_LONG).show()
                                            }
                                        }
                                    ) {
                                        Text(text = "+")
                                    }
                                }
                            }
                        }
                    }
                }
            }
        )
    }
}

