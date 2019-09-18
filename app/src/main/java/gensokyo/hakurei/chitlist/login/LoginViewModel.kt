package gensokyo.hakurei.chitlist.login

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import gensokyo.hakurei.chitlist.database.AccountDao
import kotlinx.coroutines.Job


private const val TAG = "LoginViewModel"

class LoginViewModel(
    val database: AccountDao, application: Application
) : AndroidViewModel(application) {

    private var viewModelJob = Job()

    val accounts = database.getAccounts()

    private val _navigateToHome = MutableLiveData<Long>()
    val navigateToHome
        get() = _navigateToHome

    fun onLoginClicked(id: Long) {
        _navigateToHome.value = id
    }

    fun onHomeNavigated() {
        _navigateToHome.value = null
    }

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }
}
