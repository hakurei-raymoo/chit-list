package gensokyo.hakurei.chitlist.transactionslist

import android.util.Log
import androidx.lifecycle.*
import gensokyo.hakurei.chitlist.database.TransactionDao
import kotlinx.coroutines.*

private const val TAG = "TXsListViewModel"

class TransactionsListViewModel(
    private val database: TransactionDao
) : ViewModel() {

    private var viewModelJob = Job()

    private val _transactions = database.getTransactionsWithChildren()
    val transactions
        get() = _transactions

    private val _navigateToEditTransaction = MutableLiveData<Long>()
    val navigateToEditTransaction: LiveData<Long>
        get() = _navigateToEditTransaction

    init {
        Log.i(TAG, "Init")
    }

    fun onNewTransaction() {
        Log.i(TAG, "_navigateToEditTransaction = ${_navigateToEditTransaction.value}")
        _navigateToEditTransaction.value = -1L
    }

    fun onTransactionClicked(id: Long) {
        _navigateToEditTransaction.value = id
    }

    fun onTransactionNavigated() {
        _navigateToEditTransaction.value = null
    }

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }
}
