package gensokyo.hakurei.chitlist.accountslist

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import gensokyo.hakurei.chitlist.database.AccountDao

private const val TAG = "AccountsListVMFactory"

/**
 * Provides the [AccountDao] to the [AccountsListViewModel].
 */
class AccountsListViewModelFactory(
    private val dataSource: AccountDao
) : ViewModelProvider.Factory {

    @Suppress("unchecked_cast")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AccountsListViewModel::class.java)) {
            return AccountsListViewModel(dataSource) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
