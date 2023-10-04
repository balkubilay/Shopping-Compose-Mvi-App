package com.example.shoppingcomposemvi.presentation.contract

import com.example.shoppingcomposemvi.base.UiEvent
import com.example.shoppingcomposemvi.base.UiState
import com.example.shoppingcomposemvi.domain.model.Product

class HomeContract {
    sealed class Event : UiEvent {
        object OnFetchProducts : Event()
        data class IncreaseProductCount(var product: Product) : Event()
        data class DecreaseProductCount(var product: Product) : Event()
    }

    data class State(
        val homeState: HomeState
    ) : UiState

    sealed class HomeState {
        object Idle : HomeState()
        object Loading : HomeState()
        data class Success(val products: List<Product>) : HomeState()
        data class Error(val message: String?) : HomeState()
    }
}