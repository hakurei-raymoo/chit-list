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

    val isNew = (accountKey == -1L)

    // Check accountKey to either get Account from database or insert a new one.
    private var _account =
        if (isNew) {
            MutableLiveData<Account>(Account())
        } else {
            dataSource.getAccount(accountKey)
        }
    val account: LiveData<Account>
        get() = _account

    // Check if last enabled admin account.
    private val _lastAdmin = Transformations.map(dataSource.getAdminAccounts()) {
        Log.i(TAG, "size=${it.size}, admins=$it")
        (it.size == 1) && (it.first().accountId == accountKey) }
    val lastAdmin: LiveData<Boolean>
    get() = _lastAdmin

    private val _navigateToAccountsList = MutableLiveData<Boolean?>()
    val navigateToAccountsList: LiveData<Boolean?>
        get() = _navigateToAccountsList

    init {
        Log.i(TAG, "Init")
    }

    fun onCreateClicked() {
        uiScope.launch {
            create()
            _navigateToAccountsList.value = true
        }
    }

    fun onUpdateClicked() {
        uiScope.launch {
            update()
            _navigateToAccountsList.value = true
        }
    }

    private suspend fun create() {
        withContext(Dispatchers.IO) {
            dataSource.insert(account.value!!)
            Log.i(TAG, "Inserted ${account.value!!}")
        }
    }

    private suspend fun update() {
        withContext(Dispatchers.IO) {
            dataSource.update(account.value!!)
            Log.i(TAG, "Updated ${account.value!!}")
        }
    }

    fun onCancelClicked() {
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
