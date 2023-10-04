package com.example.shoppingcomposemvi.presentation.contract

import com.example.shoppingcomposemvi.base.UiEvent
import com.example.shoppingcomposemvi.base.UiState
import com.example.shoppingcomposemvi.domain.model.Basket
import com.example.shoppingcomposemvi.domain.model.Product

class BasketContract {
    sealed class Event : UiEvent {
        data class IncreaseProductCount(var basket: Basket, var product: Product) : Event()
        data class DecreaseProductCount(var basket: Basket, var product: Product) : Event()
        object ClearBasket : Event()
        object OnFetchBasket : Event()
    }

    data class State(
        val basketState: BasketState
    ) : UiState

    sealed class BasketState {
        object Idle : BasketState()
        object Loading : BasketState()
        data class Success(val basket: Basket?) : BasketState()
        data class Error(val message: String?) : BasketState()
    }
}