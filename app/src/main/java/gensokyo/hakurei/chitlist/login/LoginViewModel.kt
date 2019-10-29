package gensokyo.hakurei.chitlist.login

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import gensokyo.hakurei.chitlist.database.Account
import gensokyo.hakurei.chitlist.database.BareAccount
import gensokyo.hakurei.chitlist.database.LoginDao
import kotlinx.coroutines.*

private const val TAG = "LoginViewModel"

class LoginViewModel(
    private val database: LoginDao
) : ViewModel() {

    private var viewModelJob = Job()

    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)

    val enableInput = MutableLiveData<Boolean>(true)
    var loginAccount = MutableLiveData<String>("")
    var loginPassword = MutableLiveData<String>("")
    var account = MutableLiveData<Account>()

    // Autocomplete parameters
    val accounts = database.getBareAccounts()
    private var accountsList = mutableListOf<BareAccount>()
    private val _accountNames = mutableListOf<String>()
    val accountNames: List<String>
        get() = _accountNames

    private val _navigateToHome = MutableLiveData<Boolean>()
    val navigateToHome
        get() = _navigateToHome

    init {
        Log.i(TAG, "Init")
    }

    // Populate accounts and accountNames for use by AutoCompleteTextView.
    fun formatAccounts(accounts: List<BareAccount>) {
        accountsList = accounts.toMutableList()
        accountsList.sortWith(compareBy(String.CASE_INSENSITIVE_ORDER) {String.format("${it.firstName} ${it.lastName}")})
        Log.i(TAG, "accountsList=$accountsList")

        _accountNames.clear()
        accountsList.forEach {
            _accountNames.add(String.format("${it.firstName} ${it.lastName}"))
        }
        Log.i(TAG, "accountNames=$accountNames")
    }

    // Find index of account by matching full name with accounts list then return the id.
    private fun loginAccountToAccountId(loginAccount: String): Long {
        val accountIndex = accountsList.binarySearch {
            val fullName = String.format("${it.firstName} ${it.lastName}")
            String.CASE_INSENSITIVE_ORDER.compare(fullName, loginAccount)
        }

        if (accountIndex >= 0L) {
            return accountsList[accountIndex].accountId
        } else {
            return -1L
        }
    }

    fun onLoginClicked() {
        enableInput.value = false
        val accountId = loginAccountToAccountId(loginAccount.value!!)
        val passwordHash = loginPassword.value!! // TODO: Hash password.
        Log.i(TAG, "accountId=$accountId, passwordHash=$passwordHash")

        if (accountId == -1L) {
            Log.i(TAG, "Account not found in accounts list.")
            enableInput.value = true
        } else {
            uiScope.launch {
                withContext(Dispatchers.IO) {
                    account.postValue(database.getLogin(accountId, passwordHash))
                }
            }
        }

        // Clear the password text.
        loginPassword.value = ""
    }

    fun onNavigateToHome() {
        _navigateToHome.value = true
    }

    fun onHomeNavigated() {
        _navigateToHome.value = null

        // Reset credentials.
        account.value = null
        loginAccount.value = ""
        loginPassword.value = ""
    }

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }
}
