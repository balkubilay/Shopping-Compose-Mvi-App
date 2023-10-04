package com.example.shoppingcomposemvi.domain.use_case.process_payment

import android.annotation.SuppressLint
import com.example.shoppingcomposemvi.data.di.IoDispatcher
import com.example.shoppingcomposemvi.domain.model.Transactions
import com.example.shoppingcomposemvi.domain.repository.LocalRepository
import com.example.shoppingcomposemvi.util.Constants
import com.example.shoppingcomposemvi.util.Resource
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import java.text.SimpleDateFormat
import java.util.Date
import javax.inject.Inject
import kotlin.random.Random

class ProcessPaymentUseCase @Inject constructor(
    private val localRepository: LocalRepository,
    @IoDispatcher private val dispatcher: CoroutineDispatcher
) {
    @SuppressLint("SimpleDateFormat")
    fun executeProcessPayment(maskedCardNo: String): Flow<Resource<Transactions>> = flow {
        emit(Resource.Loading())
        try {
            val products = localRepository.getProductsFromRoom()

            for (product in products) {
                if (product.productCount > 0) {
                    localRepository.updateProductCountInRoom(product.id!!, 0)
                }
            }
            val orderId = Random.nextLong(100000, 999999)
            val formattedDate = SimpleDateFormat("dd/MM/yyyy HH:mm").format(Date())
            val basket = localRepository.getBasketFromRoom()
            val transaction = Transactions(txnType = Constants.TXN_TYPE_SALE, orderId = orderId, txnTime = formattedDate ,basket = basket, maskedCardNo = maskedCardNo, totalAmount = basket.totalAmount)
            localRepository.addTransactionToRoom(transaction)
            localRepository.deleteBasketFromRoom()
            emit(Resource.Success(transaction))
        } catch (e: Exception) {
            println("executeProcessPayment : ${e.localizedMessage}")
            emit(Resource.Error("Bekelenmedik bir sorun oluştu. HK: 07"))
        }
    }.flowOn(dispatcher)
}