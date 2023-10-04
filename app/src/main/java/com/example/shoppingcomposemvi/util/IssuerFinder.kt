package com.example.shoppingcomposemvi.util

class IssuerFinder {

    companion object {
        fun detect(number: String): Constants.CardIssuer = when {
            isVisa(number) -> Constants.CardIssuer.VISA
            isMastercard(number) -> Constants.CardIssuer.MASTERCARD
            else -> Constants.CardIssuer.OTHER
        }

        private fun isVisa(number: String) = number.isNotEmpty() && number.first() == '4'

        private fun isMastercard(number: String) = number.length >= 2 && number.substring(0, 2).toIntOrNull() in 51 until 56

    }

}