package gensokyo.hakurei.chitlist.transactionslist

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import gensokyo.hakurei.chitlist.database.Transaction
import gensokyo.hakurei.chitlist.database.TransactionDao
import kotlinx.coroutines.*

private const val TAG = "TXsListViewModel"

class TransactionsListViewModel(
    val database: TransactionDao, application: Application) : AndroidViewModel(application) {
    private var viewModelJob = Job()

    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)

    val transactions = database.getTransactions()

    private val _navigateToEditTransaction = MutableLiveData<Long>()
    val navigateToEditTransaction
        get() = _navigateToEditTransaction

    fun onNewTransaction() {
        uiScope.launch {
            val newTransaction = Transaction()
            // TODO: Launch detail screen instead of adding to list.
            newTransaction.accountId = 1
            newTransaction.itemId = 1
            insert(newTransaction)
        }
    }

    private suspend fun insert(transaction: Transaction) {
        withContext(Dispatchers.IO) {
            database.insert(transaction)
        }
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
