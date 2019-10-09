package gensokyo.hakurei.chitlist.login

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import gensokyo.hakurei.chitlist.database.Account
import gensokyo.hakurei.chitlist.database.AccountDao
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
//    val accounts = database.getAccounts()

    private val _navigateToHome = MutableLiveData<Long>()
    val navigateToHome
        get() = _navigateToHome

    fun onLoginClicked() {
//        enableInput.value = false // Disable input while attempting authentication.

        val accountId = loginAccount.value?.toLongOrNull() // TODO: Convert name to accountId.
        val passwordHash = loginPassword.value!! // TODO: Hash password.
        Log.i(TAG, "accountId=$accountId, passwordHash=$passwordHash")

        if (accountId == null) {
            Log.i(TAG, "Invalid credentials input.")
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
            Log.i(TAG, "Authentication failed.")
            loginPassword.value = ""
        } else {
            Log.i(TAG, "Authentication passed.")
            _navigateToHome.value = account.value?.accountId
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
