package gensokyo.hakurei.chitlist.accountdetail

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import gensokyo.hakurei.chitlist.database.Account
import gensokyo.hakurei.chitlist.database.AccountDao
import kotlinx.coroutines.*

private const val TAG = "AccountDetailViewModel"

class AccountDetailViewModel(accountKey: Long = 0L, dataSource: AccountDao) :
    ViewModel() {

    val database = dataSource

    private val viewModelJob = Job()

    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)

    private val _account: LiveData<Account>
    val account
        get() = _account

    private val _navigateToAccountsList = MutableLiveData<Boolean?>()
    val navigateToAccountsList: LiveData<Boolean?>
        get() = _navigateToAccountsList

    /**
     * Check accountKey to either get existing Account or insert a new one.
     */
    init {
        if (accountKey == -1L) {
            newAccount()
            _account = database.getLastAccount()
        } else {
            _account = database.getAccount(accountKey)
        }
    }

    private fun newAccount() {
        uiScope.launch {
            withContext(Dispatchers.IO) {
                val newAccount = Account()
                database.insert(newAccount)
                Log.i(TAG, "Inserted $newAccount")
            }
        }
    }

    fun onSubmitAccountDetails() {
        uiScope.launch {
            update()
            _navigateToAccountsList.value = true
        }
    }

    private suspend fun update() {
        withContext(Dispatchers.IO) {
            // TODO: Hash password.
            database.update(account.value!!)
            Log.i(TAG, "Updated ${account.value!!}")
        }
    }

    fun doneNavigating() {
        _navigateToAccountsList.value = null
    }

    fun onDeleteAccount() {
        uiScope.launch {
            withContext(Dispatchers.IO) {
                database.delete(account.value!!)
                Log.i(TAG, "Deleted ${account.value!!}")
            }
            _navigateToAccountsList.value = true
        }
    }

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }

}
