package gensokyo.hakurei.chitlist.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import gensokyo.hakurei.chitlist.database.LoginDao

private const val TAG = "LoginViewModelFactory"

/**
 * Provides the [LoginDao] to the [LoginViewModel].
 */
class LoginViewModelFactory(
    private val dataSource: LoginDao
) : ViewModelProvider.Factory {

    @Suppress("unchecked_cast")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(LoginViewModel::class.java)) {
            return LoginViewModel(dataSource) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
