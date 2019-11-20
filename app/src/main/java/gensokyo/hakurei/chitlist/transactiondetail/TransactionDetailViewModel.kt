package gensokyo.hakurei.chitlist.transactiondetail

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import gensokyo.hakurei.chitlist.database.*
import kotlinx.coroutines.*

private const val TAG = "TXDetailViewModel"

class TransactionDetailViewModel(
    private val creatorId: Long,
    private val transactionKey: Long,
    private val dataSource: TransactionDao
) : ViewModel() {

    private val viewModelJob = Job()
    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)

    private var _transaction = dataSource.getTransaction(transactionKey)
    val transaction: LiveData<Transaction>
        get() = _transaction

    private val _navigateToTransactionsList = MutableLiveData<Boolean?>()
    val navigateToTransactionsList: LiveData<Boolean?>
        get() = _navigateToTransactionsList

    init {
        Log.i(TAG, "Init")
    }

    fun onUpdateClicked() {
        uiScope.launch {
            update()
            _navigateToTransactionsList.value = true
        }
    }

    private suspend fun update() {
        withContext(Dispatchers.IO) {
            dataSource.update(transaction.value!!)
            Log.i(TAG, "Updated ${transaction.value!!}")
        }
    }

    fun onCancelClicked() {
        _navigateToTransactionsList.value = true
    }

    fun doneNavigating() {
        _navigateToTransactionsList.value = null
    }

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }
}
