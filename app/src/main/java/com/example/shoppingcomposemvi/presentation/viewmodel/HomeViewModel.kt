package com.example.shoppingcomposemvi.presentation.viewmodel

import androidx.lifecycle.viewModelScope
import com.example.shoppingcomposemvi.base.BaseViewModel
import com.example.shoppingcomposemvi.domain.model.Product
import com.example.shoppingcomposemvi.domain.use_case.get_products.GetProductUseCase
import com.example.shoppingcomposemvi.presentation.contract.HomeContract
import com.example.shoppingcomposemvi.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(private val getProductUseCase: GetProductUseCase) :
    BaseViewModel<HomeContract.Event, HomeContract.State>() {

    private var products: List<Product>? = emptyList()

    override fun createInitialState(): HomeContract.State {
        return HomeContract.State(homeState = HomeContract.HomeState.Idle)
    }

    override fun handleEvent(event: HomeContract.Event) {
        when (event) {
            is HomeContract.Event.OnFetchProducts -> {
                getProducts()
            }

            is HomeContract.Event.IncreaseProductCount -> {
                val updatedCount = event.product.productCount + 1
                getUpdatedProducts(event.product.id!!, updatedCount)
            }

            is HomeContract.Event.DecreaseProductCount -> {
                val updatedCount = event.product.productCount - 1
                getUpdatedProducts(event.product.id!!, updatedCount)
            }
        }    }

    private fun getProducts() {
        viewModelScope.launch {
            getProductUseCase.executeGetProducts().collect {
                when (it) {
                    is Resource.Success -> {
                        it.data.let { productList ->
                            products = productList
                        }
                        setState { copy(homeState = HomeContract.HomeState.Success(products = products ?: emptyList())) }
                    }

                    is Resource.Loading -> {
                        setState { copy(homeState = HomeContract.HomeState.Loading) }
                    }

                    is Resource.Error -> {
                        setState { copy(homeState = HomeContract.HomeState.Error(message = it.message?: "Error"))  }
                    }
                }
            }
        }
    }

    private fun getUpdatedProducts(productId: Int, newProductCount: Int) {
        viewModelScope.launch {
            getProductUseCase.executeUpdateAndGetProduct(productId, newProductCount).collect {
                when (it) {
                    is Resource.Success -> {
                        it.data?.let { updatedProduct ->
                            products = updateOrAddProduct(products, updatedProduct)
                            setState { copy(homeState = HomeContract.HomeState.Success(products = products!!)) }
                        }
                    }

                    is Resource.Loading -> {
                        setState { copy(homeState = HomeContract.HomeState.Loading) }
                    }

                    is Resource.Error -> {
                        setState { copy(homeState = HomeContract.HomeState.Error(message = it.message?: "Error"))  }
                    }
                }
            }
        }
    }

    private fun updateOrAddProduct(
        productList: List<Product>?,
        updatedProduct: Product
    ): List<Product> {
        val updatedList = productList?.toMutableList() ?: mutableListOf()
        val index = updatedList.indexOfFirst { it.id == updatedProduct.id }
        if (index != -1) {
            updatedList[index] = updatedProduct
        } else {
            updatedList.add(updatedProduct)
        }
        return updatedList
    }
}