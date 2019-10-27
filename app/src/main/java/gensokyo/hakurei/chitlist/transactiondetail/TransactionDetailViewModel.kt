package gensokyo.hakurei.chitlist.transactiondetail

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import gensokyo.hakurei.chitlist.database.*
import kotlinx.coroutines.*

private const val TAG = "TXDetailViewModel"

class TransactionDetailViewModel(
    transactionKey: Long,
    private val database: TransactionDao
) : ViewModel() {

    private val viewModelJob = Job()

    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)

    private val _transaction: LiveData<Transaction>
    val transaction
        get() = _transaction

    var linkedAccount = MutableLiveData<BareAccount>()
    private var _linkedItem = MutableLiveData<Item>()
    val linkedItem: LiveData<Item>
        get() = _linkedItem

    private val _navigateToTransactionsList = MutableLiveData<Boolean?>()
    val navigateToTransactionsList: LiveData<Boolean?>
        get() = _navigateToTransactionsList

    /**
     * Check transactionKey to either get existing Transaction or insert a new one.
     */
    init {
        Log.i(TAG, "Init")
        if (transactionKey == -1L) {
            newTransaction()
            _transaction = database.getLastTransaction()
        } else {
            _transaction = database.getTransaction(transactionKey)
        }
    }

    private fun newTransaction() {
        uiScope.launch {
            withContext(Dispatchers.IO) {
                // TODO: Add creator of transaction.
                // Transaction must have valid ForeignKeys before insertion to table.
                val newTransaction = Transaction(accountId = 1L, itemId = 1L, comments="")
                Log.i(TAG, "Attempting $newTransaction")
                database.insert(newTransaction)
                Log.i(TAG, "Inserted $newTransaction")
            }
        }
    }

    fun onUpdateClicked() {
        uiScope.launch {
            update()
            _navigateToTransactionsList.value = true
        }
    }

    private suspend fun update() {
        withContext(Dispatchers.IO) {
            // TODO: Throw error if accountId or itemId do not exist.
            database.update(transaction.value!!)
            Log.i(TAG, "Updated ${transaction.value!!}")
        }
    }

    fun doneNavigating() {
        _navigateToTransactionsList.value = null
    }

    fun onDeleteClicked() {
        uiScope.launch {
            withContext(Dispatchers.IO) {
                database.delete(transaction.value!!)
                Log.i(TAG, "Deleted ${transaction.value!!}")
            }
            _navigateToTransactionsList.value = true
        }
    }

    fun updateAccountEditHelperText(accountId: Long?) {
        if (accountId != null) {
            uiScope.launch {
                withContext(Dispatchers.IO) {
                    linkedAccount.postValue(database.getAccount(accountId))
                }
            }
        }
    }

    fun updateItemEditHelperText(itemId: Long?) {
        if (itemId != null) {
            uiScope.launch {
                withContext(Dispatchers.IO) {
                    _linkedItem.postValue(database.getItem(itemId))
                }
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }

}
