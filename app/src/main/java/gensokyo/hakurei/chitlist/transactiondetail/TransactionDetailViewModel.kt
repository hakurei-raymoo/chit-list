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

    fun getTransaction() = transaction


    init {
        transaction = database.getTransaction(transactionKey)
    }


    private val _navigateToTransactionsList = MutableLiveData<Boolean?>()
    val navigateToTransactionsList: LiveData<Boolean?>
        get() = _navigateToTransactionsList

    fun onUpdateTransactionDetails() {
        uiScope.launch {
            // IO is a thread pool for running operations that access the disk, such as
            // our Room database.
            withContext(Dispatchers.IO) {
                val currentTransaction = database.get(transactionKey)
                currentTransaction.accountId = transaction.value?.accountId!!
                currentTransaction.itemId = transaction.value?.itemId!!
                currentTransaction.comments = transaction.value?.comments!!
                database.update(currentTransaction)
                Log.i(TAG, "updated $currentTransaction")
            }
            _navigateToTransactionsList.value = true
        }
    }

    fun doneNavigating() {
        _navigateToTransactionsList.value = null
    }

    fun onDeleteTransaction() {
        uiScope.launch {
            // IO is a thread pool for running operations that access the disk, such as
            // our Room database.
            withContext(Dispatchers.IO) {
                val currentTransaction = database.get(transactionKey)
                database.delete(currentTransaction)
                Log.i(TAG, "deleted $currentTransaction")
            }
            _navigateToTransactionsList.value = true
        }
    }

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }

}
