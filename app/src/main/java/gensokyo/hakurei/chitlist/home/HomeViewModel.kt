package gensokyo.hakurei.chitlist.home

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import gensokyo.hakurei.chitlist.database.AccountDao
import kotlinx.coroutines.Job


private const val TAG = "HomeViewModel"

class HomeViewModel(private val accountKey: Long,
                    val database: AccountDao, application: Application
) : AndroidViewModel(application) {

    private var viewModelJob = Job()

    private val _account = database.getAccount(accountKey)
    val account
        get() = _account

    val accounts = database.getAccounts()

    private val _navigateToHome = MutableLiveData<Long>()
    val navigateToHome
        get() = _navigateToHome

    fun onHomeClicked(id: Long) {
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
