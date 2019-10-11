package gensokyo.hakurei.chitlist.transactionslist

import android.app.Application
import android.util.Log
import androidx.lifecycle.*
import gensokyo.hakurei.chitlist.database.Account
import gensokyo.hakurei.chitlist.database.Transaction
import gensokyo.hakurei.chitlist.database.TransactionDao
import kotlinx.coroutines.*

private const val TAG = "TXsListViewModel"

class TransactionsListViewModel(
    val database: TransactionDao, application: Application) : AndroidViewModel(application) {

    private var viewModelJob = Job()

    private val _transactions = database.getTransactionsWithChildren()
    val transactions
        get() = _transactions

    private val _navigateToEditTransaction = MutableLiveData<Long>()
    val navigateToEditTransaction
        get() = _navigateToEditTransaction

    fun onNewTransaction() {
        // TODO: Remove new transaction magic number (-1L).
        _navigateToEditTransaction.value = -1L
    }

    fun onEditTransactionClicked(id: Long) {
        _navigateToEditTransaction.value = id
    }

    fun onEditTransactionNavigated() {
        _navigateToEditTransaction.value = null
    }

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }
}
