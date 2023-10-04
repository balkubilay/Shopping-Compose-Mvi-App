package com.example.shoppingcomposemvi.domain.model

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(tableName = "basket")
data class Basket(
    @PrimaryKey(autoGenerate = true)
    val id: Int? = null,
    val products: List<Product>
) : Parcelable {
    val totalAmount: Double
        get() = products.sumOf { it.productPrice * it.productCount }
}
