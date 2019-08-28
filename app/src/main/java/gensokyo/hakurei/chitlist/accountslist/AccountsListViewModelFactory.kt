package gensokyo.hakurei.chitlist.accountslist

import android.app.Application
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import gensokyo.hakurei.chitlist.database.AccountDao

private const val TAG = "AccountsListVMFactory"

/**
 * Provides the AccountDao and context to the ViewModel.
 */
class AccountsListViewModelFactory(
    private val dataSource: AccountDao,
    private val application: Application) : ViewModelProvider.Factory {

    @Suppress("unchecked_cast")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        Log.i(TAG, "create called")

        if (modelClass.isAssignableFrom(AccountsListViewModel::class.java)) {
            return AccountsListViewModel(dataSource, application) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
