package com.example.shoppingcomposemvi.domain.use_case.get_basket

import com.example.shoppingcomposemvi.data.di.IoDispatcher
import com.example.shoppingcomposemvi.domain.model.Basket
import com.example.shoppingcomposemvi.domain.model.Product
import com.example.shoppingcomposemvi.domain.repository.LocalRepository
import com.example.shoppingcomposemvi.util.Resource
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class GetBasketUseCase @Inject constructor(
    private val localRepository: LocalRepository,
    @IoDispatcher private val dispatcher: CoroutineDispatcher
) {
    fun executeGetBasket(): Flow<Resource<Basket>> = flow {
        emit(Resource.Loading())

        try {
            val products = localRepository.getProductsFromRoom()
            val updatedProducts = mutableListOf<Product>()

            for (product in products) {
                if (product.productCount > 0) {
                    updatedProducts.add(product)
                }
            }
            if (updatedProducts.isNotEmpty()) {
                localRepository.deleteBasketFromRoom()
                val updatedBasket = Basket(0, updatedProducts)
                localRepository.addBasketToRoom(updatedBasket)
                emit(Resource.Success(localRepository.getBasketFromRoom()))
            } else {
                emit(Resource.Error("Sepetinizde ürün bulunmuyor!"))
            }
        } catch (e: Exception) {
            println("executeGetBasket ${e.localizedMessage}")
            emit(Resource.Error("Bekelenmedik bir sorun oluştu. HK: 01"))
        }
    }.flowOn(dispatcher)

    fun executeClearBasket(): Flow<Resource<Basket>> = flow {
        emit(Resource.Loading())

        val products = localRepository.getProductsFromRoom()

        try {
            for (product in products) {
                if (product.productCount > 0) {
                    localRepository.updateProductCountInRoom(product.id!!, 0)
                }
            }
            localRepository.deleteBasketFromRoom()
            emit(Resource.Success(localRepository.getBasketFromRoom()))
        } catch (e: Exception) {
            println("executeClearBasket ${e.localizedMessage}")
            emit(Resource.Error("Bekelenmedik bir sorun oluştu. HK: 02"))
        }
    }.flowOn(dispatcher)

    fun executeUpdateAndGetBasket(
        basketId: Int,
        productId: Int,
        newProductCount: Int
    ): Flow<Resource<Basket>> = flow {
        try {
            localRepository.updateProductCountInRoom(productId, newProductCount)

            val products = localRepository.getProductsFromRoom()
            val updatedProducts = mutableListOf<Product>()

            for (product in products) {
                if (product.productCount > 0) {
                    updatedProducts.add(product)
                }
            }
            localRepository.updateBasketProductCountInRoom(basketId, updatedProducts)
            if (updatedProducts.isNotEmpty()) {
                emit(Resource.Success(localRepository.getBasketFromRoom()))
            } else {
                emit(Resource.Error("Sepetinizde ürün bulunmuyor!"))
            }
        } catch (e: Exception) {
            println("executeUpdateAndGetBasket ${e.localizedMessage}")
            emit(Resource.Error("Bekelenmedik bir sorun oluştu. HK: 03"))
        }
    }.flowOn(dispatcher)
}