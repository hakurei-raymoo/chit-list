package gensokyo.hakurei.chitlist.accountdetail

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import gensokyo.hakurei.chitlist.database.Account
import gensokyo.hakurei.chitlist.database.AccountDao

private const val TAG = "AccountDetailVMFactory"

/**
 * Provides the key for the [Account] and the [AccountDao] to the [AccountDetailViewModel].
 */
class AccountDetailViewModelFactory(
    private val accountKey: Long,
    private val dataSource: AccountDao) : ViewModelProvider.Factory {
    @Suppress("unchecked_cast")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        Log.i(TAG, "create called with accountKey=$accountKey")

        if (modelClass.isAssignableFrom(AccountDetailViewModel::class.java)) {
            return AccountDetailViewModel(accountKey, dataSource) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}