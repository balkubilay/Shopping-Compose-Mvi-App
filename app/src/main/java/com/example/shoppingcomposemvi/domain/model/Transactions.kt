package com.example.shoppingcomposemvi.domain.model

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(tableName = "transactions")
data class Transactions (
    @PrimaryKey(autoGenerate = true)
    val id: Int? = null,
    val txnType: Int,
    val orderId: Long,
    val txnTime: String,
    val basket: Basket,
    val maskedCardNo: String,
    val totalAmount: Double
) : Parcelable
