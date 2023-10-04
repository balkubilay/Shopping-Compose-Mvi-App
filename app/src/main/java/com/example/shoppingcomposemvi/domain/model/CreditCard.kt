package com.example.shoppingcomposemvi.domain.model

import com.example.shoppingcomposemvi.R
import com.example.shoppingcomposemvi.util.Constants.CardIssuer
import com.example.shoppingcomposemvi.util.IssuerFinder

data class CreditCard(
    var cardNo: String = "",
    var expirationDate: String = "",
    var cardHolderName: String = "",
    var cvv: String = "000",
    var cardEntity: String = ""
) {
    val logoCardIssuer = when(IssuerFinder.detect(cardNo)) {
        CardIssuer.VISA -> R.drawable.logo_visa
        CardIssuer.MASTERCARD -> R.drawable.logo_mastercard
        CardIssuer.OTHER -> R.drawable.baseline_credit_card_24
    }
}


