package com.example.shoppingcomposemvi.presentation.contract

import com.example.shoppingcomposemvi.base.UiEvent
import com.example.shoppingcomposemvi.base.UiState
import com.example.shoppingcomposemvi.domain.model.Transactions

class TransactionsContract {
    sealed class Event : UiEvent {
        object OnFetchTransactions : Event()
        data class VoidTransaction(var selectedItemId: Int) : Event()
    }

    data class State(
        val transactionsState: TransactionsState
    ) : UiState

    sealed class TransactionsState {
        object Idle : TransactionsState()
        object Loading : TransactionsState()
        data class Success(val transactions: List<Transactions>) : TransactionsState()
        data class Error(val message: String?) : TransactionsState()
    }
}