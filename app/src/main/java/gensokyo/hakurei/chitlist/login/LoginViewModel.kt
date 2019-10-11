package gensokyo.hakurei.chitlist.login

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import gensokyo.hakurei.chitlist.database.Account
import gensokyo.hakurei.chitlist.database.AccountDao
import gensokyo.hakurei.chitlist.database.BareAccount
import kotlinx.coroutines.*


private const val TAG = "LoginViewModel"

class LoginViewModel(
    val database: AccountDao, application: Application
) : AndroidViewModel(application) {

    private var viewModelJob = Job()

    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)

    val enableInput = MutableLiveData<Boolean>(true)
    var loginAccount = MutableLiveData<String>("")
    var loginPassword = MutableLiveData<String>("")
    var account = MutableLiveData<Account>()

    // Autocomplete parameters
    private var accounts = mutableListOf<BareAccount>()
    private val _accountNames = mutableListOf<String>()
    val accountNames: List<String>
        get() = _accountNames

    private val _navigateToHome = MutableLiveData<Long>()
    val navigateToHome
        get() = _navigateToHome

    init {
        formatAccounts()
    }

    // Populate accounts and accountNames for use by AutoCompleteTextView.
    private fun formatAccounts() {
        uiScope.launch {
            withContext(Dispatchers.IO) {
                accounts = database.getBareAccounts().toMutableList()
                accounts.sortBy {it.firstName}
                Log.i(TAG, "accounts=$accounts")

                accounts.forEach {
                    _accountNames.add(String.format("${it.firstName} ${it.lastName}"))
                }
                Log.i(TAG, "accountNames=$accountNames")
            }
        }
    }

    // Find index of account by matching full name with accounts list then return the id.
    private fun loginAccountToAccountId(loginAccount: String): Long {
        val accountIndex = accounts.binarySearch {
            val fullName = String.format("${it.firstName} ${it.lastName}")
            String.CASE_INSENSITIVE_ORDER.compare(fullName, loginAccount)
        }

        if (accountIndex >= 0L) {
            return accounts[accountIndex].accountId
        } else {
            return -1L
        }
    }

    fun onLoginClicked() {
        val accountId = loginAccountToAccountId(loginAccount.value!!)
        val passwordHash = loginPassword.value!! // TODO: Hash password.
        Log.i(TAG, "accountId=$accountId, passwordHash=$passwordHash")

        if (accountId == -1L) {
            Log.i(TAG, "Account not found.")
        } else {
            uiScope.launch {
                withContext(Dispatchers.IO) {
                    account.postValue(database.getLogin(accountId, passwordHash))
                }
            }
        }
    }

    fun checkCredentials() {
        if (account.value == null) {
            loginPassword.value = ""
            Log.i(TAG, "Authentication failed.")
        } else {
            _navigateToHome.value = account.value?.accountId
            Log.i(TAG, "Authentication passed.")

            // Reset credentials.
            account.value = null
            loginAccount.value = ""
            loginPassword.value = ""
        }
    }

    fun onHomeNavigated() {
        _navigateToHome.value = null
    }

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }
}
