package com.example.shoppingcomposemvi.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.shoppingcomposemvi.domain.model.Product

@Dao
interface ProductDao {

    @Query("SELECT * FROM product")
    fun getProducts(): List<Product>

    @Query("SELECT * FROM product WHERE id = :id")
    suspend fun getProduct(id: Int): Product

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addProduct(product: Product)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addProducts(products: List<Product>)

    @Update
    suspend fun updateProduct(product: Product)

    @Query("UPDATE product SET productCount = :newCount WHERE id = :productId")
    suspend fun updateProductCount(productId: Int, newCount: Int)

    @Delete
    suspend fun deleteProduct(product: Product)

    @Query("DELETE FROM product")
    suspend fun deleteAllProducts()
}