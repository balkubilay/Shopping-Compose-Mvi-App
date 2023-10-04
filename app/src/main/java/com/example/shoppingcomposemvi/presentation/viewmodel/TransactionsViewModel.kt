package com.example.shoppingcomposemvi.presentation.viewmodel

import androidx.lifecycle.viewModelScope
import com.example.shoppingcomposemvi.base.BaseViewModel
import com.example.shoppingcomposemvi.domain.model.Transactions
import com.example.shoppingcomposemvi.domain.use_case.get_transactions.GetTransactionsUseCase
import com.example.shoppingcomposemvi.presentation.contract.TransactionsContract
import com.example.shoppingcomposemvi.util.Constants
import com.example.shoppingcomposemvi.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TransactionsViewModel @Inject constructor(private val getTransactionsUseCase: GetTransactionsUseCase) :
    BaseViewModel<TransactionsContract.Event, TransactionsContract.State>() {

    private var transactions: List<Transactions>? = emptyList()


    override fun createInitialState(): TransactionsContract.State {
        return TransactionsContract.State(transactionsState = TransactionsContract.TransactionsState.Idle)
    }

    override fun handleEvent(event: TransactionsContract.Event) {
        when (event) {
            is TransactionsContract.Event.OnFetchTransactions -> {
                getTransactions()
            }

            is TransactionsContract.Event.VoidTransaction -> {
                voidTransaction(event.selectedItemId)
            }
        }
    }

    private fun getTransactions() {
        viewModelScope.launch {
            getTransactionsUseCase.executeGetTransactions().collect {
                when (it) {
                    is Resource.Success -> {
                        it.data.let { transactionsList ->
                            transactions = transactionsList
                        }
                        setState {
                            copy(
                                transactionsState = TransactionsContract.TransactionsState.Success(
                                    transactions = transactions ?: emptyList()
                                )
                            )
                        }
                    }

                    is Resource.Loading -> {
                        setState { copy(transactionsState = TransactionsContract.TransactionsState.Loading) }
                    }

                    is Resource.Error -> {
                        setState {
                            copy(
                                transactionsState = TransactionsContract.TransactionsState.Error(
                                    message = it.message ?: "Error"
                                )
                            )
                        }
                    }
                }
            }
        }
    }

    private fun voidTransaction(transactionId: Int) {
        viewModelScope.launch {
            getTransactionsUseCase.executeVoidTransaction(transactionId, Constants.TXN_TYPE_VOID).collect {
                when (it) {
                    is Resource.Success -> {
                        it.data?.let { updatedTransaction ->
                            transactions = updateOrAddTransactions(transactions, updatedTransaction)
                            setState {
                                copy(
                                    transactionsState = TransactionsContract.TransactionsState.Success(
                                        transactions = transactions!!
                                    )
                                )
                            }
                        }
                    }

                    is Resource.Loading -> {
                        setState { copy(transactionsState = TransactionsContract.TransactionsState.Loading) }
                    }

                    is Resource.Error -> {
                        setState {
                            copy(
                                transactionsState = TransactionsContract.TransactionsState.Error(
                                    message = it.message ?: "Error"
                                )
                            )
                        }
                    }
                }
            }
        }
    }
}

private fun updateOrAddTransactions(
    transactionsList: List<Transactions>?,
    transaction: Transactions
): List<Transactions> {
    val updatedList = transactionsList?.toMutableList() ?: mutableListOf()
    val index = updatedList.indexOfFirst { it.id == transaction.id }
    if (index != -1) {
        updatedList[index] = transaction
    } else {
        updatedList.add(transaction)
    }
    return updatedList
}
