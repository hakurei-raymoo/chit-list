package gensokyo.hakurei.chitlist.transactiondetail

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import gensokyo.hakurei.chitlist.database.Transaction
import gensokyo.hakurei.chitlist.database.TransactionDao
import kotlinx.coroutines.*

private const val TAG = "TXDetailViewModel"

class TransactionDetailViewModel(private val transactionKey: Long = 0L, dataSource: TransactionDao) : ViewModel() {

    val database = dataSource

    private val viewModelJob = Job()

    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)

    private val transaction: LiveData<Transaction>
    // TODO: Remove after testing.
    val publicTransaction
        get() = transaction

    private val _navigateToTransactionsList = MutableLiveData<Boolean?>()
    val navigateToTransactionsList: LiveData<Boolean?>
        get() = _navigateToTransactionsList

    fun getTransaction() = transaction


    /**
     * Check transactionKey to either get existing Transaction or insert a new one.
     */
    init {
        if (transactionKey == -1L) {
            newTransaction()
            transaction = database.getLastTransaction()
        } else {
            transaction = database.getTransaction(transactionKey)
        }
    }

    private fun newTransaction() {
        uiScope.launch {
            withContext(Dispatchers.IO) {
                // Transaction must have valid ForeignKeys before insertion to table.
                val newTransaction = Transaction(accountId = 1L, itemId = 1L)
                Log.i(TAG, "Attempting $newTransaction")
                database.insert(newTransaction)
                Log.i(TAG, "Inserted $newTransaction")
            }
        }
    }

    fun onSubmitTransactionDetails() {
        uiScope.launch {
            update()
            _navigateToTransactionsList.value = true
        }
    }


    private suspend fun update() {
        withContext(Dispatchers.IO) {
            // TODO: Ensure accountId and itemId exist.
            database.update(transaction.value!!)
            Log.i(TAG, "Updated ${transaction.value!!}")
        }
    }

    fun doneNavigating() {
        _navigateToTransactionsList.value = null
    }

    fun onDeleteTransaction() {
        uiScope.launch {
            withContext(Dispatchers.IO) {
                database.delete(transaction.value!!)
                Log.i(TAG, "Deleted ${transaction.value!!}")
            }
            _navigateToTransactionsList.value = true
        }
    }

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }

}
