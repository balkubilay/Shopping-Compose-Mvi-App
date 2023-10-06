package com.example.shoppingcomposemvi.domain.use_case.get_products

import com.example.shoppingcomposemvi.data.di.IoDispatcher
import com.example.shoppingcomposemvi.domain.model.Product
import com.example.shoppingcomposemvi.domain.repository.LocalRepository
import com.example.shoppingcomposemvi.util.Resource
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class GetProductUseCase @Inject constructor(
    private val localRepository: LocalRepository,
    @IoDispatcher private val dispatcher: CoroutineDispatcher
) {
    fun executeGetProducts(): Flow<Resource<List<Product>>> = flow {
        emit(Resource.Loading())
        val products = localRepository.getProductsFromRoom()
        if (products.isNotEmpty()) {
            emit(Resource.Success(products))
        } else {
            try {
                val productList = listOf(
                    Product(
                        productName = "Kağıt",
                        productPrice = 80.61,
                        productImageUrl = "https://iis-akakce.akamaized.net/p.z?%2F%2Fm%2Emedia%2Damazon%2Ecom%2Fimages%2FI%2F51kXoH9OpVL%2E%5FSL500%5F%2Ejpg",
                        productCount = 0
                    ),
                    Product(
                        productName = "Silgi",
                        productPrice = 10.00,
                        productImageUrl = "https://cdn.akakce.com/z/faber-castell/faber-castell-5500187170-f-c-2-li-sinav-si.jpg",
                        productCount = 0
                    ),
                    Product(
                        productName = "Defter",
                        productPrice = 40.00,
                        productImageUrl = "https://iis-akakce.akamaized.net/p.z?%2F%2Fcdn%2Epazarama%2Ecom%2Fasset%2F1364386972526%2Fimages%2Fgptachromo100ypa4spirallippkapakdefterkareli4l%2D1%2Ejpg",
                        productCount = 0
                    ),
                    Product(
                        productName = "Kitap",
                        productPrice = 50.82,
                        productImageUrl = "https://iis-akakce.akamaized.net/p.z?%2F%2Fi%2Edr%2Ecom%2Etr%2Fcache%2F600x600%2D0%2Foriginals%2F0002017247001%2D1%2Ejpg",
                        productCount = 0
                    ),
                    Product(
                        productName = "Kalem",
                        productPrice = 9.58,
                        productImageUrl = "https://cdn.akakce.com/z/faber-castell/faber-castell-grip-1347-0-7-mm-versatil-kalem.jpg",
                        productCount = 0
                    )
                )
                localRepository.addProductsToRoom(products = productList)
                if (localRepository.getProductsFromRoom().isNotEmpty()) {
                    emit(Resource.Success(localRepository.getProductsFromRoom()))
                } else {
                    emit(Resource.Error("Ürün listesi boş"))
                }
            } catch (e: Exception) {
                println("executeGetProducts ${e.localizedMessage}")
                emit(Resource.Error("Bekelenmedik bir sorun oluştu. HK: 04"))
            }
        }
    }.catch { e ->
        println("executeGetProducts1 ${e.localizedMessage}")
        emit(Resource.Error("Bekelenmedik bir sorun oluştu. HK: 05"))
    }.flowOn(dispatcher)


    fun executeUpdateAndGetProduct(productId: Int, newProductCount: Int): Flow<Resource<Product>> =
        flow {
            try {
                localRepository.updateProductCountInRoom(productId, newProductCount)

                val updatedProduct = localRepository.getProductFromRoom(productId)
                emit(Resource.Success(updatedProduct))
            } catch (e: Exception) {
                println("executeUpdateAndGetProduct ${e.localizedMessage}")
                emit(Resource.Error("Bekelenmedik bir sorun oluştu. HK: 06"))
            }
        }.flowOn(dispatcher)
}