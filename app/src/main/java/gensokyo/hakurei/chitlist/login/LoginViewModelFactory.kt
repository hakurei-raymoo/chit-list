package gensokyo.hakurei.chitlist.login

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import gensokyo.hakurei.chitlist.database.AccountDao

private const val TAG = "LoginViewModelFactory"

/**
 * Provides the [AccountDao] and context to the [LoginViewModel].
 */
class LoginViewModelFactory(
    private val dataSource: AccountDao,
    private val application: Application
) : ViewModelProvider.Factory {

    @Suppress("unchecked_cast")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(LoginViewModel::class.java)) {
            return LoginViewModel(dataSource, application) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
