package gensokyo.hakurei.chitlist.accountdetail

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import gensokyo.hakurei.chitlist.database.Account
import gensokyo.hakurei.chitlist.database.AccountDao
import kotlinx.coroutines.*

private const val TAG = "AccountDetailViewModel"

class AccountDetailViewModel(private val accountKey: Long = 0L, dataSource: AccountDao) : ViewModel() {

    val database = dataSource

    private val viewModelJob = Job()

    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)

    private val account: LiveData<Account>

    fun getAccount() = account


    init {
        account = database.getAccount(accountKey)
    }


    private val _navigateToAccountsList = MutableLiveData<Boolean?>()
    val navigateToAccountsList: LiveData<Boolean?>
        get() = _navigateToAccountsList

    fun onUpdateAccountDetails() {
        uiScope.launch {
            // IO is a thread pool for running operations that access the disk, such as
            // our Room database.
            withContext(Dispatchers.IO) {
                val currentAccount = database.get(accountKey)
                currentAccount.firstName = account.value?.firstName!!
                currentAccount.lastName = account.value?.lastName!!
                // TODO: Hash password.
                currentAccount.passwordHash = account.value?.passwordHash!!//.hash
                database.update(currentAccount)
                Log.i(TAG, "updated $currentAccount")
            }
            _navigateToAccountsList.value = true
        }
    }

    fun doneNavigating() {
        _navigateToAccountsList.value = null
    }

    fun onDeleteAccount() {
        uiScope.launch {
            // IO is a thread pool for running operations that access the disk, such as
            // our Room database.
            withContext(Dispatchers.IO) {
                val currentAccount = database.get(accountKey)
                database.delete(currentAccount)
                Log.i(TAG, "deleted $currentAccount")
            }
            _navigateToAccountsList.value = true
        }
    }

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }

}
