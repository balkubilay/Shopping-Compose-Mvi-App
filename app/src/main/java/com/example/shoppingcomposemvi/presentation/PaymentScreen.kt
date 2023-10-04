package com.example.shoppingcomposemvi.presentation

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.example.shoppingcomposemvi.R
import com.example.shoppingcomposemvi.domain.model.CreditCard
import com.example.shoppingcomposemvi.presentation.contract.PaymentContract
import com.example.shoppingcomposemvi.presentation.viewmodel.PaymentViewModel
import com.example.shoppingcomposemvi.util.PaymentValidator
import com.example.shoppingcomposemvi.util.Screen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PaymentScreen(
    navController: NavHostController,
    viewModel: PaymentViewModel = hiltViewModel()
) {
    var cardNo by remember { mutableStateOf("") }
    var cardHolderName by remember { mutableStateOf("") }
    var cvv by remember { mutableStateOf("") }
    var expDate by remember { mutableStateOf("") }
    val cardHolderRequester = FocusRequester()
    val cvvRequester = FocusRequester()
    val expDateRequester = FocusRequester()
    val context = LocalContext.current
    val state by viewModel.uiState.collectAsState()

    when (state.paymentState) {
        is PaymentContract.PaymentState.Idle -> {

        }

        is PaymentContract.PaymentState.Success -> {
            val transaction = (state.paymentState as PaymentContract.PaymentState.Success).transaction
            navController.currentBackStackEntry?.savedStateHandle?.set(
                key = "transaction",
                value = transaction
            )
            navController.navigate(Screen.PaymentResult.route)
        }

        is PaymentContract.PaymentState.Loading -> {
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

        is PaymentContract.PaymentState.Error -> {

        }
    }


    val creditCard = CreditCard(cardNo.replace(" ", ""), expDate, cardHolderName, cvv)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        CustomCard(
            brandImage = {
                Image(
                    painter = painterResource(id = creditCard.logoCardIssuer),
                    contentDescription = "",
                    modifier = Modifier
                        .align(Alignment.End)
                        .size(45.dp)
                )
            }, cardNumber = cardNo, userName = cardHolderName, expirationDate = expDate
        )
        var cardNoTextState by remember { mutableStateOf(TextFieldValue("")) }
        OutlinedTextField(
            value = cardNoTextState,
            onValueChange = {
                val kartNoLength = it.text.replace(" ", "").length
                val isDeleteAction = it.text.length < cardNoTextState.text.length
                val kartNoWithoutSpaces = it.text.replace(" ", "")

                if (kartNoLength <= 16) {
                    cardNo = if (!isDeleteAction) {
                        val formattedKartNo = StringBuilder()
                        for (i in 1..kartNoLength) {
                            formattedKartNo.append(kartNoWithoutSpaces[i - 1])
                            if (i % 4 == 0 && i != 16) {
                                formattedKartNo.append(' ')
                            }
                        }
                        formattedKartNo.toString()
                    } else {
                        it.text
                    }
                    cardNoTextState =
                        TextFieldValue(text = cardNo, selection = TextRange(cardNo.length))
                }
            },
            label = { Text(text = "Kart Numarası") },
            keyboardOptions = KeyboardOptions.Default.copy(
                keyboardType = KeyboardType.Number, imeAction = ImeAction.Next
            ),
            keyboardActions = KeyboardActions(onNext = { cardHolderRequester.requestFocus() }),
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp)
        )

        OutlinedTextField(
            value = cardHolderName,
            onValueChange = { cardHolderName = it.uppercase() },
            label = { Text(text = "Kart Sahibi Adı Soyadı") },
            keyboardOptions = KeyboardOptions.Default.copy(
                keyboardType = KeyboardType.Text, imeAction = ImeAction.Next
            ),
            keyboardActions = KeyboardActions(onNext = { cvvRequester.requestFocus() }),
            modifier = Modifier
                .fillMaxWidth()
                .focusRequester(cardHolderRequester)
                .padding(vertical = 8.dp)
        )

        OutlinedTextField(
            value = cvv,
            onValueChange = {
                if (it.length <= 3) {
                    cvv = it
                }
            },
            label = { Text(text = "CVV") },
            keyboardOptions = KeyboardOptions.Default.copy(
                keyboardType = KeyboardType.Number, imeAction = ImeAction.Next
            ),
            keyboardActions = KeyboardActions(onNext = { expDateRequester.requestFocus() }),
            modifier = Modifier
                .fillMaxWidth()
                .focusRequester(cvvRequester)
                .padding(vertical = 8.dp)
        )

        ExpirationDateInput(onDateValueChange = {
            expDate = it
        }, focusRequester = expDateRequester)

        Button(
            onClick = {
                val (isValid, message) = PaymentValidator.execute(
                    creditCard.cardNo,
                    creditCard.cardHolderName,
                    creditCard.cvv,
                    creditCard.expirationDate
                )

                if (isValid) {
                    viewModel.setEvent(PaymentContract.Event.CompletePayment(maskedCardNumber(cardNo)))
                } else {
                    Toast.makeText(context, message, Toast.LENGTH_LONG).show()
                }
            }, modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp)
        ) {
            Text(text = "Ödemeyi Tamamla")
        }
    }
}

@Composable
fun CustomCard(
    brandImage: @Composable () -> Unit,
    cardNumber: String,
    userName: String,
    expirationDate: String
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(200.dp),
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(
            containerColor = colorResource(id = R.color.matte_black)
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 5.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
        ) {
            brandImage()
            Spacer(modifier = Modifier.height(14.dp))
            Image(
                painter = painterResource(id = R.drawable.chip_credit_card),
                contentDescription = "",
                modifier = Modifier.size(45.dp)
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = cardNumber,
                style = MaterialTheme.typography.bodyMedium,
                color = Color.White,
                fontSize = 20.sp
            )

            Spacer(modifier = Modifier.height(8.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = userName,
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.White,
                    fontSize = 15.sp
                )
                Text(
                    text = expirationDate,
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.White,
                    fontSize = 15.sp
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalComposeUiApi::class)
@Composable
fun ExpirationDateInput(
    modifier: Modifier = Modifier,
    onDateValueChange: (String) -> Unit,
    focusRequester: FocusRequester
) {
    var dateText by remember { mutableStateOf(("")) }
    var textState by remember { mutableStateOf(TextFieldValue("")) }
    val keyboardController = LocalSoftwareKeyboardController.current

    Surface(
        modifier = modifier,
        color = MaterialTheme.colorScheme.surface,
        shape = MaterialTheme.shapes.small
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            OutlinedTextField(
                modifier = Modifier
                    .weight(1f)
                    .focusRequester(focusRequester),
                value = textState,
                onValueChange = {
                    val newValue = it.text
                    val isDeleteAction = newValue.length < textState.text.length
                    if (newValue.length <= 5) {
                        val formattedValue = if (newValue.length > 2 && !isDeleteAction) {
                            if (newValue.length == 3 && !newValue.endsWith("/") && !newValue.contains(
                                    "/"
                                )
                            ) {
                                "${newValue.substring(0, 2)}/${newValue.substring(2, 3)}"
                            } else {
                                newValue
                            }
                        } else {
                            if (newValue.isNotEmpty() && newValue[0] in '2'..'9') {
                                "0$newValue/"
                            } else {
                                if (newValue.length == 2 && newValue[1].code <= 2) {
                                    newValue
                                } else if (newValue.length == 2 && newValue[1] > '2') {
                                    newValue.substring(0, 1) + ""
//                                    "0${newValue.substring(0,1)}/${newValue.substring(1)}"
                                } else {
                                    newValue
                                }
                            }
                        }

                        val pattern = "^\\d{2}/\\d{2}$"
                        if (formattedValue.matches(Regex(pattern)) || formattedValue.isEmpty()) {
                            dateText = formattedValue
                        }
                        dateText = formattedValue
                        textState =
                            TextFieldValue(text = dateText, selection = TextRange(dateText.length))
                        onDateValueChange(dateText)
                    }
                },
                label = { Text(text = "AY/YIL") },
                keyboardOptions = KeyboardOptions.Default.copy(
                    keyboardType = KeyboardType.Number, imeAction = ImeAction.Done
                ),
                keyboardActions = KeyboardActions(onDone = {
                    keyboardController?.hide()
                }),
                textStyle = MaterialTheme.typography.bodyMedium,
                singleLine = true
            )
        }
    }
}

fun maskedCardNumber(cardNo: String): String {
    val len = cardNo.length
    var maskedCardNo = ""
    if (len > 15) {
        val first4 = cardNo.substring(0, 4)

        val last4 = cardNo.substring(len - 4, len)

        val masked = "*".repeat(len - 8)

        maskedCardNo = first4 + masked + last4
    }

    return maskedCardNo
}
