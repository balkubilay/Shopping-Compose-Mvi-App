package com.example.shoppingcomposemvi.presentation.viewmodel

import androidx.lifecycle.viewModelScope
import com.example.shoppingcomposemvi.base.BaseViewModel
import com.example.shoppingcomposemvi.domain.use_case.get_basket.GetBasketUseCase
import com.example.shoppingcomposemvi.presentation.contract.BasketContract
import com.example.shoppingcomposemvi.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BasketViewModel @Inject constructor(private val getBasketUseCase: GetBasketUseCase) :
    BaseViewModel<BasketContract.Event, BasketContract.State>() {

    override fun createInitialState(): BasketContract.State {
        return BasketContract.State(basketState = BasketContract.BasketState.Idle)
    }

    override fun handleEvent(event: BasketContract.Event) {
        when (event) {
            is BasketContract.Event.OnFetchBasket -> {
                getBasket()
            }

            is BasketContract.Event.IncreaseProductCount -> {
                val updatedCount = event.product.productCount + 1
                updateAndGetBasket(event.basket.id!!, event.product.id!!, updatedCount)
            }

            is BasketContract.Event.DecreaseProductCount -> {
                val updatedCount = event.product.productCount - 1
                updateAndGetBasket(event.basket.id!!, event.product.id!!, updatedCount)
            }

            is BasketContract.Event.ClearBasket -> {
                clearBasket()
            }
        }
    }

    private fun getBasket() {
        viewModelScope.launch {
            getBasketUseCase.executeGetBasket().collect {
                when (it) {
                    is Resource.Success -> {
                        setState { copy(basketState = BasketContract.BasketState.Success(basket = it.data)) }
                    }

                    is Resource.Loading -> {
                        setState { copy(basketState = BasketContract.BasketState.Loading) }
                    }

                    is Resource.Error -> {
                        setState { copy(basketState = BasketContract.BasketState.Error(message = it.message?: "Error")) }
                    }
                }
            }
        }
    }

    private fun clearBasket() {
        viewModelScope.launch {
            getBasketUseCase.executeClearBasket().collect {
                when (it) {
                    is Resource.Success -> {
                        setState { copy(basketState = BasketContract.BasketState.Error(message = "Sepetinizde ürün bulunmuyor!")) }
                    }

                    is Resource.Loading -> {
                        setState { copy(basketState = BasketContract.BasketState.Loading) }
                    }

                    is Resource.Error -> {
                        setState { copy(basketState = BasketContract.BasketState.Error(message = it.message?: "Error")) }
                    }
                }
            }
        }
    }

    private fun updateAndGetBasket(basketId: Int, productId: Int, newProductCount: Int) {
        viewModelScope.launch {
            getBasketUseCase.executeUpdateAndGetBasket(basketId, productId, newProductCount)
                .collect {
                    when (it) {
                        is Resource.Success -> {
                            setState { copy(basketState = BasketContract.BasketState.Success(basket = it.data)) }
                        }

                        is Resource.Loading -> {
                            setState { copy(basketState = BasketContract.BasketState.Loading) }
                        }

                        is Resource.Error -> {
                            setState { copy(basketState = BasketContract.BasketState.Error(message = it.message?: "Error")) }
                        }
                    }
                }
        }
    }
}