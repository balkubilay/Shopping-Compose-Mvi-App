package com.example.shoppingcomposemvi.presentation.contract

import com.example.shoppingcomposemvi.base.UiEvent
import com.example.shoppingcomposemvi.base.UiState
import com.example.shoppingcomposemvi.domain.model.Transactions

class PaymentContract {
    sealed class Event : UiEvent {
        data class CompletePayment(val maskedCardNo: String) : Event()
    }

    data class State(
        val paymentState: PaymentState
    ) : UiState

    sealed class PaymentState {
        object Idle : PaymentState()
        object Loading : PaymentState()
        data class Success(val transaction: Transactions) : PaymentState()
        data class Error(val message: String?) : PaymentState()
    }
}