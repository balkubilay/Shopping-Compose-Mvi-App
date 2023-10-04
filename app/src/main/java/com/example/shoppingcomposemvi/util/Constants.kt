package com.example.shoppingcomposemvi.util

object Constants {
    const val TXN_TYPE_SALE = 1
    const val TXN_TYPE_VOID = 0
    const val ROOM_DB_NAME = "room_db"

    enum class BottomBar {
        MAIN,
        BASKET,
        TRANSACTIONS
    }

    enum class CardIssuer {
        VISA,
        MASTERCARD,
        OTHER
    }
}

