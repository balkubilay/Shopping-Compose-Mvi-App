package com.example.shoppingcomposemvi.presentation

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.shoppingcomposemvi.R
import com.example.shoppingcomposemvi.domain.model.Transactions
import com.example.shoppingcomposemvi.presentation.contract.TransactionsContract
import com.example.shoppingcomposemvi.presentation.viewmodel.TransactionsViewModel
import com.example.shoppingcomposemvi.util.Constants
import com.example.shoppingcomposemvi.util.Screen
import java.text.DecimalFormat

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TransactionsScreen(
    navController: NavController,
    viewModel: TransactionsViewModel = hiltViewModel()
) {
    val state by viewModel.uiState.collectAsState()
    var transactionsList: List<Transactions>? by remember { mutableStateOf(emptyList()) }


    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                modifier = Modifier.fillMaxWidth(),
                title = {
                    Text(
                        text = "Tüm İşlemlerim",
                        modifier = Modifier
                            .fillMaxWidth()
                            .wrapContentWidth(Alignment.CenterHorizontally)
                    )
                },
                navigationIcon = {
                    IconButton(
                        onClick = {
                            navController.navigate(Screen.Home.route) {
                                popUpTo(Screen.Home.route) {
                                    inclusive = true
                                }
                            }
                        }
                    ) {
                        Icon(imageVector = Icons.Default.ArrowBack, contentDescription = null)
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
        content = {
            when (state.transactionsState) {
                is TransactionsContract.TransactionsState.Idle -> {
                    viewModel.setEvent(TransactionsContract.Event.OnFetchTransactions)
                }
                is TransactionsContract.TransactionsState.Loading -> {
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
                is TransactionsContract.TransactionsState.Success -> {
                    transactionsList = (state.transactionsState as TransactionsContract.TransactionsState.Success).transactions.sortedByDescending { it.id }
                    transactionsList?.let {transactionsList->
                        TransactionsContent(it, transactionsList, viewModel)
                    }
                }
                is TransactionsContract.TransactionsState.Error -> {
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Text(text = "${(state.transactionsState as TransactionsContract.TransactionsState.Error).message}")
                    }
                }
            }
        }
    )
}

@Composable
private fun TransactionsContent(
    it: PaddingValues,
    transactionsList: List<Transactions>,
    viewModel: TransactionsViewModel
) {
    val openAlertDialog = remember { mutableStateOf(false) }
    val selectedItem = remember { mutableIntStateOf(0) }
    val decimalFormat = DecimalFormat("0.00")

    LazyColumn(modifier = Modifier.padding(it)) {
        items(count = transactionsList.count(), itemContent = { index ->
            val item = transactionsList[index]
            Card(
                modifier = Modifier
                    .padding(all = 5.dp)
                    .fillMaxWidth(),
                elevation = CardDefaults.cardElevation(defaultElevation = 5.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(80.dp)
                        .padding(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(
                        modifier = Modifier
                            .weight(1f)
                            .padding(start = 16.dp)
                    ) {
                        Text(text = "${decimalFormat.format(item.totalAmount)} TL")
                        Text(text = "Sipariş No: ${item.orderId}")
                        Text(text = item.txnTime)
                    }
                    if (item.txnType == Constants.TXN_TYPE_SALE) {
                        Button(
                            onClick = {
                                selectedItem.intValue = item.id!!
                                openAlertDialog.value = !openAlertDialog.value },
                            modifier = Modifier.padding(8.dp),
                            colors = ButtonDefaults.buttonColors(
                                contentColor = Color.White,
                                containerColor = colorResource(id = R.color.red)
                            )
                        ) {
                            Text(text = "İptal")
                        }
                    } else {
                        Text(
                            text = "İptal Edildi",
                            color = colorResource(id = R.color.red),
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }

        })
    }
    when {
        openAlertDialog.value -> {
            CustomAlertDialog(
                onDismissRequest = { openAlertDialog.value = false },
                onConfirmation = {
                    viewModel.setEvent(TransactionsContract.Event.VoidTransaction(selectedItem.intValue))
                    openAlertDialog.value = false
                },
                dialogText = "İşlem iptal edilsin mi?",
                positiveButtonText = "Evet",
                negativeButtonText = "Hayır"
            )
        }
    }
}