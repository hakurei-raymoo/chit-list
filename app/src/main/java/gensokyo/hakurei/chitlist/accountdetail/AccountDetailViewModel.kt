package gensokyo.hakurei.chitlist.accountdetail

import android.util.Log
import androidx.lifecycle.*
import gensokyo.hakurei.chitlist.database.Account
import gensokyo.hakurei.chitlist.database.AccountDao
import kotlinx.coroutines.*

private const val TAG = "AccountDetailViewModel"

class AccountDetailViewModel(
    private val accountKey: Long,
    private val dataSource: AccountDao
) : ViewModel() {

    private val viewModelJob = Job()

    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)

    // Check accountKey to either get Account from database or insert a new one.
    private var _account =
        if (accountKey == -1L) {
            MutableLiveData<Account>(Account())
        } else {
            dataSource.getAccount(accountKey)
        }
    val account: LiveData<Account>
        get() = _account

    private val _navigateToAccountsList = MutableLiveData<Boolean?>()
    val navigateToAccountsList: LiveData<Boolean?>
        get() = _navigateToAccountsList

    init {
        Log.i(TAG, "Init")
    }

    fun onUpdateClicked() {
        uiScope.launch {
            update()
            _navigateToAccountsList.value = true
        }
    }

    private suspend fun update() {
        withContext(Dispatchers.IO) {
            if (accountKey == -1L) {
                dataSource.insert(account.value!!)
                Log.i(TAG, "Inserted ${account.value!!}")
            } else {
                // TODO: Throw error if last admin account disabled.
                dataSource.update(account.value!!)
                Log.i(TAG, "Updated ${account.value!!}")
            }
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
