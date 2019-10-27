package gensokyo.hakurei.chitlist.history

import android.util.Log
import androidx.lifecycle.*
import gensokyo.hakurei.chitlist.database.ShopDao
import gensokyo.hakurei.chitlist.database.TransactionWithChildren
import kotlinx.coroutines.*

private const val TAG = "HistoryViewModel"

class HistoryViewModel(
    private val database: ShopDao
) : ViewModel() {

    private val viewModelJob = Job()

    private val _accountId = MutableLiveData<Long>()
    val accountId: LiveData<Long>
        get() = _accountId

    private val _balance = MutableLiveData<Int>()
    val balance: LiveData<Int>
        get() = _balance

    // Gets data when accountId is changed.
    private var _history = Transformations.switchMap(accountId) { database.getHistory(it) }
    val history: LiveData<List<TransactionWithChildren>>?
        get()  =_history

    init {
        Log.i(TAG, "Init")
    }

    fun updateHistory(accountId: Long) {
        _accountId.value = accountId
    }

    fun updateBalance() {
        var sum = 0
        history?.value?.forEach {
            sum += it.amount
        }
        _balance.postValue(sum)
    }

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }
}
