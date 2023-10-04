package com.example.shoppingcomposemvi.util

import android.annotation.SuppressLint
import android.os.Build
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.util.Calendar

class PaymentValidator {
    companion object {
        fun execute(cardNumber: String, cardHolderName: String, cvv: String, expDate: String): Pair<Boolean, String> {
            val messages = mutableListOf<String>()

            when {
                !isValidCardNumber(cardNumber) -> messages.add("Geçersiz kart numarası")
                !isValidCardHolderName(cardHolderName) -> messages.add("Geçersiz kart sahibi adı")
                !isValidCVV(cvv) -> messages.add("Geçersiz CVV")
                !isValidExpirationDate(expDate) -> messages.add("Geçersiz son kullanma tarihi")
            }

            val isValid = messages.isEmpty()
            val message = messages.joinToString("\n")

            return Pair(isValid, message)
        }

        private fun isValidCardNumber(cardNumber: String): Boolean {
            val reversed = cardNumber.reversed()
            val len = reversed.length
            var oddSum = 0
            var evenSum = 0
            for (i in 0 until len) {
                val c = reversed[i]
                if (!Character.isDigit(c)) {
                    throw IllegalArgumentException("Not a digit: '$c'")
                }
                val digit = Character.digit(c, 10)
                if (i % 2 == 0) {
                    oddSum += digit
                } else {
                    evenSum += digit / 5 + (2 * digit) % 10
                }
            }
            return (oddSum + evenSum) % 10 == 0
        }

        private fun isValidCardHolderName(cardHolderName: String): Boolean {
            return cardHolderName.isNotBlank()
        }

        private fun isValidCVV(cvv: String): Boolean {
            return cvv.matches(Regex("\\d{3,4}"))
        }

        @SuppressLint("SimpleDateFormat")
        private fun isValidExpirationDate(expDate: String): Boolean {
            return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                val currentDate = LocalDate.now()
                val parts = expDate.split("/")
                if (parts.size != 2) {
                    false
                } else {
                    val month = parts[0].toIntOrNull() ?: return false
                    val year = parts[1].toIntOrNull() ?: return false
                    if (month < 1 || month > 12 || year < 0) {
                        false
                    } else {
                        val expirationDate = LocalDate.of(2000 + year, month, 1)
                        expirationDate.isAfter(currentDate)
                    }
                }
            } else {
                val currentDate = Calendar.getInstance().time
                val dateFormat = SimpleDateFormat("MM/yy")

                try {
                    val expirationDate = dateFormat.parse(expDate)
                    if (expirationDate != null) {
                        !expirationDate.before(currentDate)
                    } else {
                        false
                    }
                } catch (e: Exception) {
                    false
                }
            }
        }
    }
}