package gensokyo.hakurei.chitlist.accountslist

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import gensokyo.hakurei.chitlist.database.AccountDao
import kotlinx.coroutines.*

private const val TAG = "AccountsListViewModel"

class AccountsListViewModel(
    val database: AccountDao, application: Application) : AndroidViewModel(application) {
    private var viewModelJob = Job()

    val accounts = database.getAccounts()

    private val _navigateToEditAccount = MutableLiveData<Long>()
    val navigateToEditAccount
        get() = _navigateToEditAccount

    fun onNewAccount() {
        // TODO: Remove new account magic number (-1L).
        _navigateToEditAccount.value = -1L
    }

    fun onEditAccountClicked(id: Long) {
        _navigateToEditAccount.value = id
    }

    fun onEditAccountNavigated() {
        _navigateToEditAccount.value = null
    }

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }
}
