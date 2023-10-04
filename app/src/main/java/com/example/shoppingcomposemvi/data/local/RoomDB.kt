package com.example.shoppingcomposemvi.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.shoppingcomposemvi.data.local.dao.BasketDao
import com.example.shoppingcomposemvi.data.local.dao.ProductDao
import com.example.shoppingcomposemvi.data.local.dao.TransactionsDao
import com.example.shoppingcomposemvi.domain.model.Basket
import com.example.shoppingcomposemvi.domain.model.Product
import com.example.shoppingcomposemvi.domain.model.Transactions
import com.example.shoppingcomposemvi.domain.model.converter.BasketConverter
import com.example.shoppingcomposemvi.domain.model.converter.ProductsConverter

@Database(entities = [Product::class, Basket::class, Transactions::class], version = 1)
@TypeConverters(ProductsConverter::class, BasketConverter::class)
abstract class RoomDB: RoomDatabase() {
    abstract val productDao: ProductDao
    abstract val basketDao: BasketDao
    abstract val transactionsDao: TransactionsDao
}