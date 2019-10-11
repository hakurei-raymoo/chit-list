package gensokyo.hakurei.chitlist.home

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import gensokyo.hakurei.chitlist.database.LoginDao
import kotlinx.coroutines.Job

private const val TAG = "HomeViewModel"

class HomeViewModel(
    userKey: Long,
    private val database: LoginDao
) : ViewModel() {

    private var viewModelJob = Job()

    private val _login = database.getAccount(userKey)
    val login
        get() = _login

    init {
        Log.i(TAG, "Init")
    }

//    private val _navigateToAdmin = MutableLiveData<Boolean?>()
//    val navigateToAdmin
//        get() = _navigateToAdmin
//
//    fun onAdminClicked() {
//        _navigateToAdmin.value = true
//    }
//
//    fun onTransactionsAdmin() {
//        _navigateToAdmin.value = null
//    }

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }
}
