package com.example.shoppingcomposemvi.presentation.viewmodel

import androidx.lifecycle.viewModelScope
import com.example.shoppingcomposemvi.base.BaseViewModel
import com.example.shoppingcomposemvi.domain.use_case.process_payment.ProcessPaymentUseCase
import com.example.shoppingcomposemvi.presentation.contract.PaymentContract
import com.example.shoppingcomposemvi.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PaymentViewModel @Inject constructor(private val processPaymentUseCase: ProcessPaymentUseCase) :
    BaseViewModel<PaymentContract.Event, PaymentContract.State>() {


    override fun createInitialState(): PaymentContract.State {
        return PaymentContract.State(paymentState = PaymentContract.PaymentState.Idle)
    }

    override fun handleEvent(event: PaymentContract.Event) {
        when (event) {
            is PaymentContract.Event.CompletePayment -> {
                completePayment(event.maskedCardNo)
            }
        }
    }

    private fun completePayment(maskedCardNo: String) {
        viewModelScope.launch {
            processPaymentUseCase.executeProcessPayment(maskedCardNo)
                .collect {
                    when (it) {
                        is Resource.Success -> {
                            it.data?.let { transaction ->
                                setState { copy(paymentState = PaymentContract.PaymentState.Success(transaction)) }
                            }
                        }

                        is Resource.Loading -> {
                            setState { copy(paymentState = PaymentContract.PaymentState.Loading) }
                        }

                        is Resource.Error -> {
                            setState { copy(paymentState = PaymentContract.PaymentState.Error("Ödeme işlemi başarısız.")) }
                        }
                    }
                }
        }
    }
}