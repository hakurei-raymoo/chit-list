package gensokyo.hakurei.chitlist.transactionslist

import android.app.Application
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import gensokyo.hakurei.chitlist.database.TransactionDao

private const val TAG = "TXsListVMFactory"

/**
 * Provides the TransactionDao and context to the ViewModel.
 */
class TransactionsListViewModelFactory(
    private val dataSource: TransactionDao,
    private val application: Application) : ViewModelProvider.Factory {

    @Suppress("unchecked_cast")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        Log.i(TAG, "create called")

        if (modelClass.isAssignableFrom(TransactionsListViewModel::class.java)) {
            return TransactionsListViewModel(dataSource, application) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
