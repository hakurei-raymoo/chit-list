package gensokyo.hakurei.chitlist.home

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import gensokyo.hakurei.chitlist.database.AccountDao

private const val TAG = "HomeViewModelFactory"

/**
 * Provides the [AccountDao] and context to the [HomeViewModel].
 */
class HomeViewModelFactory(
    private val dataSource: AccountDao,
    private val application: Application
) : ViewModelProvider.Factory {

    @Suppress("unchecked_cast")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(HomeViewModel::class.java)) {
            return HomeViewModel(dataSource, application) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
