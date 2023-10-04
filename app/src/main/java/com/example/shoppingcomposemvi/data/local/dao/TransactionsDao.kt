package com.example.shoppingcomposemvi.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.shoppingcomposemvi.domain.model.Transactions

@Dao
interface TransactionsDao {
    @Query("SELECT * FROM transactions")
    fun getTransactions(): List<Transactions>

    @Query("SELECT * FROM transactions WHERE id = :id")
    suspend fun getTransaction(id: Int): Transactions

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addTransaction(transactions: Transactions)

    @Update
    suspend fun updateTransaction(transactions: Transactions)

    @Query("UPDATE transactions SET txnType = :newTxnType, txnTime = :newTxnTime WHERE id = :transactionsId")
    suspend fun updateTransactionTxnType(transactionsId: Int, newTxnType: Int, newTxnTime: String)

    @Query("DELETE FROM transactions")
    suspend fun deleteTransaction()
}