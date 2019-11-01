package gensokyo.hakurei.chitlist.login

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
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
    val loginAccount = MutableLiveData<String>("")
    val loginPassword = MutableLiveData<String>("")

    // AutoCompleteTextView adapter list.
    private val _accountsList = Transformations.map(database.getBareAccounts()) { accounts ->
        formatAccounts(accounts)
    }
    val accountsList: LiveData<List<String>>
        get() = _accountsList

    // Account that is passed to SharedViewModel as current user.
    private val _credentials = MutableLiveData<Account>()
    val credentials: LiveData<Account>
        get() = _credentials

    private val _navigateToHome = MutableLiveData<Boolean>()
    val navigateToHome: LiveData<Boolean>
        get() = _navigateToHome

    init {
        Log.i(TAG, "Init")
    }

    /* Convert List<BareAccount> to List<String> for AutoCompeteTextView. */
    private fun formatAccounts(accounts: List<BareAccount>): List<String> {
        val mutableList = accounts.toMutableList()
        val stringList = mutableListOf<String>()
        mutableList.sortWith(compareBy(String.CASE_INSENSITIVE_ORDER) {String.format("%04d: %s %s", it.accountId, it.firstName, it.lastName)})
        Log.i(TAG, "mutableList=$mutableList")

        mutableList.forEach {
            stringList.add(String.format("%04d: %s %s", it.accountId, it.firstName, it.lastName))
        }
        Log.i(TAG, "stringList=$stringList")
        return stringList
    }

    /* Returns the first four characters of the string as a long. */
    private fun loginStringToAccountId(string: String): Long? {
        return string.substring(0..3).toLongOrNull()
    }

    private fun tryLogin(accountId: Long, passwordHash: String) {
        Log.i(TAG, "Trying credentials: account_id=$accountId, password_hash=$passwordHash")

        uiScope.launch {
            withContext(Dispatchers.IO) {
                _credentials.postValue(database.getLogin(accountId, passwordHash))
            }
        }
    }

    fun onLoginClicked() {
        enableInput.value = false
        val accountId = loginStringToAccountId(loginAccount.value!!)
        val passwordHash = loginPassword.value!! // TODO: Hash password.
        if (accountId != null) {
            tryLogin(accountId, passwordHash)
        }

        // Re-enable input.
        loginPassword.value = ""
        enableInput.value = true
    }

    fun onNavigateToHome() {
        _navigateToHome.value = true
    }

    fun onHomeNavigated() {
        _navigateToHome.value = null

        // Clear credentials.
        _credentials.value = null
        loginAccount.value = ""
        loginPassword.value = ""
    }

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }
}
