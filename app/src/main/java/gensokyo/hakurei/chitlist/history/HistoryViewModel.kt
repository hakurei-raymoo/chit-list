package gensokyo.hakurei.chitlist.history

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import gensokyo.hakurei.chitlist.database.ShopDao
import gensokyo.hakurei.chitlist.database.TransactionWithChildren
import kotlinx.coroutines.*

private const val TAG = "HistoryViewModel"

class HistoryViewModel(
    private val database: ShopDao
) : ViewModel() {

    private val viewModelJob = Job()

    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)

    private val _accountId = MutableLiveData<Long>()
    val accountId: LiveData<Long>
        get() = _accountId

    private val _balance = MutableLiveData<Int>()
    val balance: LiveData<Int>
        get() = _balance

    private var _history = MediatorLiveData<List<TransactionWithChildren>>()
    val history: LiveData<List<TransactionWithChildren>>
        get()  =_history

    init {
        Log.i(TAG, "Init")
    }

    fun updateAccount(accountId: Long) {
        _accountId.value = accountId
    }

    fun updateHistory() {
        _history.addSource(database.getTransactions(accountId.value!!)) {
            _history.value = it
        }
        Log.i(TAG, "transactions=${history.value}")
    }

    fun updateBalance() {
        var sum = 0
        history.value?.forEach {
            sum += it.amount
        }
        _balance.postValue(sum)
        Log.i(TAG, "balance=${balance.value}")
    }

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }
}
