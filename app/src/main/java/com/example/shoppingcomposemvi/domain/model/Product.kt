package com.example.shoppingcomposemvi.domain.model

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(tableName = "product")
data class Product (
    @PrimaryKey(autoGenerate = true)
    val id: Int? = null,
    val productName: String,
    val productPrice: Double,
    val productImageUrl: String,
    val productCount: Int
) : Parcelable