package com.example.shoppingcomposemvi.domain.repository

import com.example.shoppingcomposemvi.domain.model.Basket
import com.example.shoppingcomposemvi.domain.model.Product
import com.example.shoppingcomposemvi.domain.model.Transactions


interface LocalRepository {
    fun getProductsFromRoom(): List<Product>

    suspend fun getProductFromRoom(id: Int): Product

    suspend fun addProductToRoom(product: Product)

    suspend fun addProductsToRoom(products: List<Product>)

    suspend fun updateProductInRoom(product: Product)

    suspend fun updateProductCountInRoom(productId: Int, newCount: Int)

    suspend fun deleteProductFromRoom(product: Product)

    suspend fun deleteAllProductFromRoom()

    fun getBasketFromRoom(): Basket

    suspend fun addBasketToRoom(basket: Basket)

    suspend fun updateBasketInRoom(basket: Basket)

    suspend fun deleteBasketFromRoom()

    suspend fun updateBasketProductCountInRoom(basketId: Int, updatedProducts: List<Product>)

    fun getTransactionsFromRoom(): List<Transactions>

    suspend fun getTransactionFromRoom(id: Int): Transactions

    suspend fun addTransactionToRoom(transactions: Transactions)

    suspend fun updateTransactionInRoom(transactions: Transactions)

    suspend fun updateTransactionTxnTypeInRoom(transactionId: Int, newTxnType: Int, newTxnTime: String)

    suspend fun deleteTransactionsFromRoom()

}