package gensokyo.hakurei.chitlist.viewmodels

import android.util.Log
import androidx.lifecycle.*
import gensokyo.hakurei.chitlist.database.AdminHomeDao
import kotlinx.coroutines.*

private const val TAG = "AdminHomeViewModel"

class AdminHomeViewModel(
    val userId: Long,
    private val dataSource: AdminHomeDao
) : ViewModel() {

    private var viewModelJob = Job()

    private val _accounts = dataSource.getAccounts()
    val accounts
        get() = _accounts

    private val _items = dataSource.getItems()
    val items
        get() = _items

    private val _transactions = dataSource.getTransactionsWithChildren()
    val transactions
        get() = _transactions

    private val _navigateToEditAccount = MutableLiveData<Long>()
    val navigateToEditAccount: LiveData<Long>
        get() = _navigateToEditAccount

    private val _navigateToEditItem = MutableLiveData<Long>()
    val navigateToEditItem: LiveData<Long>
        get() = _navigateToEditItem

    private val _navigateToEditTransaction = MutableLiveData<Long>()
    val navigateToEditTransaction: LiveData<Long>
        get() = _navigateToEditTransaction

    init {
        Log.i(TAG, "Init")
    }

    /* AccountsListFragment functions */
    fun onNewAccount() {
        _navigateToEditAccount.value = -1L
    }

    fun onAccountClicked(id: Long) {
        _navigateToEditAccount.value = id
    }

    fun onAccountNavigated() {
        _navigateToEditAccount.value = null
    }

    /* ItemsListFragment functions */
    fun onNewItem() {
        _navigateToEditItem.value = -1L
    }

    fun onItemClicked(id: Long) {
        _navigateToEditItem.value = id
    }

    fun onItemNavigated() {
        _navigateToEditItem.value = null
    }

    /* TransactionsListFragment functions */
    fun onNewTransaction() {
        Log.i(TAG, "_navigateToEditTransaction = ${_navigateToEditTransaction.value}")
        _navigateToEditTransaction.value = -1L
    }

    fun onTransactionClicked(id: Long) {
        _navigateToEditTransaction.value = id
    }

    fun onTransactionNavigated() {
        _navigateToEditTransaction.value = null
    }

    override fun onCleared() {
        Log.i(TAG, "Cleared")
        super.onCleared()
        viewModelJob.cancel()
    }
}
