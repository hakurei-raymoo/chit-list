package gensokyo.hakurei.chitlist.accountdetail

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import gensokyo.hakurei.chitlist.database.Account
import gensokyo.hakurei.chitlist.database.AccountDao
import kotlinx.coroutines.*

private const val TAG = "AccountDetailViewModel"

class AccountDetailViewModel(
    accountKey: Long = 0L,
    private val dataSource: AccountDao
) : ViewModel() {

    private val database = dataSource

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
        Log.i(TAG, "Init")
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

    fun onUpdateClicked() {
        uiScope.launch {
            update()
            _navigateToAccountsList.value = true
        }
    }

    private suspend fun update() {
        withContext(Dispatchers.IO) {
            // TODO: Throw error if name is same as an existing name.
            // TODO: Throw error if last admin account disabled.
            database.update(account.value!!)
            Log.i(TAG, "Updated ${account.value!!}")
        }
    }

    fun onBackClicked() {
        _navigateToAccountsList.value = true
    }

    fun doneNavigating() {
        _navigateToAccountsList.value = null
    }

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }
}
