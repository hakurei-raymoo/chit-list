package gensokyo.hakurei.chitlist.transactionslist

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import gensokyo.hakurei.chitlist.database.TransactionDao
import kotlinx.coroutines.*

private const val TAG = "TXsListViewModel"

class TransactionsListViewModel(
    val database: TransactionDao, application: Application) : AndroidViewModel(application) {
    private var viewModelJob = Job()

    val transactions = database.getTransactions()

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
