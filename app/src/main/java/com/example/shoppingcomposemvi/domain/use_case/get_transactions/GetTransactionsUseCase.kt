package com.example.shoppingcomposemvi.domain.use_case.get_transactions

import android.annotation.SuppressLint
import com.example.shoppingcomposemvi.data.di.IoDispatcher
import com.example.shoppingcomposemvi.domain.model.Transactions
import com.example.shoppingcomposemvi.domain.repository.LocalRepository
import com.example.shoppingcomposemvi.util.Resource
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import java.text.SimpleDateFormat
import java.util.Date
import javax.inject.Inject

class GetTransactionsUseCase @Inject constructor(
    private val localRepository: LocalRepository,
    @IoDispatcher private val dispatcher: CoroutineDispatcher
) {
   fun executeGetTransactions() : Flow<Resource<List<Transactions>>> = flow {
       emit(Resource.Loading())
       try {
           if (localRepository.getTransactionsFromRoom().isNotEmpty()){
               emit(Resource.Success(localRepository.getTransactionsFromRoom()))
           } else {
               emit(Resource.Error("İşlem bulunamadı."))
           }
       } catch (e: Exception) {
           println("executeGetBasket: ${e.localizedMessage}")
           emit(Resource.Error("Bekelenmedik bir sorun oluştu. HK: 08"))
       }
   }.flowOn(dispatcher)

    @SuppressLint("SimpleDateFormat")
    fun executeVoidTransaction(transactionId: Int, newTxnType: Int) : Flow<Resource<Transactions>> = flow {
        emit(Resource.Loading())
        try {
            val formattedDate = SimpleDateFormat("dd/MM/yyyy HH:mm").format(Date())

            localRepository.updateTransactionTxnTypeInRoom(transactionId = transactionId, newTxnType = newTxnType, newTxnTime = formattedDate)

            val updatedTransaction = localRepository.getTransactionFromRoom(transactionId)
            emit(Resource.Success(updatedTransaction))
        } catch (e: Exception) {
            println("executeVoidTransaction: ${e.localizedMessage}")
            emit(Resource.Error("Bekelenmedik bir sorun oluştu. HK: 09"))
        }
    }.flowOn(dispatcher)
}