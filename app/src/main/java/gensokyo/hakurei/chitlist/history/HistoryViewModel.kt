package gensokyo.hakurei.chitlist.history

import android.util.Log
import androidx.lifecycle.*
import gensokyo.hakurei.chitlist.database.ShopDao
import gensokyo.hakurei.chitlist.database.Transaction
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

    // Get all transactions matching the users accountId.
    private val _history = Transformations.switchMap(accountId) { database.getHistory(it) }
    val history: LiveData<List<TransactionWithChildren>>
        get()  =_history

    // Sum all transactions matching the users accountId.
    private val _balance = Transformations.map(history) { sumTransactions(it) }
    val balance: LiveData<Int>
        get() = _balance

    init {
        Log.i(TAG, "Init")
    }

    fun updateHistory(accountId: Long) {
        _accountId.value = accountId
    }

    private fun sumTransactions(transactions: List<TransactionWithChildren>): Int {
        var sum = 0
        transactions.forEach {
            sum += it.amount
        }
        return sum
    }

    fun onHistoryClicked(transactionId: Long) {

    }

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }
}
