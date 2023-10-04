package com.example.shoppingcomposemvi.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.shoppingcomposemvi.domain.model.Basket
import com.example.shoppingcomposemvi.domain.model.Product

@Dao
interface BasketDao {

    @Query("SELECT * FROM basket")
    fun getBasket(): Basket

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addBasket(basket: Basket)

    @Update
    suspend fun updateBasket(basket: Basket)

    @Query("DELETE FROM basket")
    suspend fun deleteBasket()

    @Query("UPDATE basket SET products = :updatedProducts WHERE id = :basketId")
    suspend fun updateBasketProducts(basketId: Int, updatedProducts: List<Product>)
}