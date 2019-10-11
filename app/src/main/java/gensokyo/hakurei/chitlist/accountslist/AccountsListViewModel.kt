package gensokyo.hakurei.chitlist.accountslist

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import gensokyo.hakurei.chitlist.database.AccountDao
import kotlinx.coroutines.*

private const val TAG = "AccountsListViewModel"

class AccountsListViewModel(
    private val database: AccountDao
) : ViewModel() {
    private var viewModelJob = Job()

    private val _accounts = database.getAccounts()
    val accounts
        get() = _accounts

    private val _navigateToEditAccount = MutableLiveData<Long>()
    val navigateToEditAccount
        get() = _navigateToEditAccount

    init {
        Log.i(TAG, "Init")
    }

    fun onNewAccount() {
        // TODO: Remove new account magic number (-1L).
        _navigateToEditAccount.value = -1L
    }

    fun onAccountClicked(id: Long) {
        _navigateToEditAccount.value = id
    }

    fun onAccountNavigated() {
        _navigateToEditAccount.value = null
    }

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }
}
