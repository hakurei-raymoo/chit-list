package gensokyo.hakurei.chitlist.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import gensokyo.hakurei.chitlist.database.LoginDao

private const val TAG = "HomeViewModelFactory"

/**
 * Provides the [LoginDao] to the [HomeViewModel].
 */
class HomeViewModelFactory(
    private val userKey: Long,
    private val dataSource: LoginDao
) : ViewModelProvider.Factory {

    @Suppress("unchecked_cast")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(HomeViewModel::class.java)) {
            return HomeViewModel(userKey, dataSource) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
