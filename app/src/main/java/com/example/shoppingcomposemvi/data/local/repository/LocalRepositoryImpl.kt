package com.example.shoppingcomposemvi.data.local.repository

import com.example.shoppingcomposemvi.data.local.dao.BasketDao
import com.example.shoppingcomposemvi.data.local.dao.ProductDao
import com.example.shoppingcomposemvi.data.local.dao.TransactionsDao
import com.example.shoppingcomposemvi.domain.model.Basket
import com.example.shoppingcomposemvi.domain.model.Product
import com.example.shoppingcomposemvi.domain.model.Transactions
import com.example.shoppingcomposemvi.domain.repository.LocalRepository

class LocalRepositoryImpl(private val productDao: ProductDao, private val basketDao: BasketDao, private val transactionsDao: TransactionsDao) : LocalRepository {
    override fun getProductsFromRoom(): List<Product> = productDao.getProducts()

    override suspend fun getProductFromRoom(id: Int) = productDao.getProduct(id)

    override suspend fun addProductToRoom(product: Product) = productDao.addProduct(product)

    override suspend fun addProductsToRoom(products: List<Product>) = productDao.addProducts(products)

    override suspend fun updateProductInRoom(product: Product) = productDao.updateProduct(product)

    override suspend fun updateProductCountInRoom(productId: Int, newCount: Int) = productDao.updateProductCount(productId, newCount)

    override suspend fun deleteProductFromRoom(product: Product) = productDao.deleteProduct(product)

    override suspend fun deleteAllProductFromRoom() = productDao.deleteAllProducts()

    override fun getBasketFromRoom(): Basket = basketDao.getBasket()

    override suspend fun addBasketToRoom(basket: Basket) = basketDao.addBasket(basket)

    override suspend fun updateBasketInRoom(basket: Basket) = basketDao.updateBasket(basket)

    override suspend fun deleteBasketFromRoom() = basketDao.deleteBasket()

    override suspend fun updateBasketProductCountInRoom(basketId: Int, updatedProducts: List<Product>) = basketDao.updateBasketProducts(basketId, updatedProducts)

    override fun getTransactionsFromRoom(): List<Transactions> = transactionsDao.getTransactions()

    override suspend fun getTransactionFromRoom(id: Int) = transactionsDao.getTransaction(id)

    override suspend fun addTransactionToRoom(transactions: Transactions) = transactionsDao.addTransaction(transactions)

    override suspend fun updateTransactionInRoom(transactions: Transactions) = transactionsDao.updateTransaction(transactions)

    override suspend fun updateTransactionTxnTypeInRoom(transactionId: Int, newTxnType: Int, newTxnTime: String) = transactionsDao.updateTransactionTxnType(transactionId, newTxnType, newTxnTime)

    override suspend fun deleteTransactionsFromRoom() = transactionsDao.deleteTransaction()

}