package gensokyo.hakurei.chitlist.transactionslist

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import gensokyo.hakurei.chitlist.database.TransactionDao

private const val TAG = "TXsListVMFactory"

/**
 * Provides the [TransactionDao] and context to the [TransactionsListViewModel].
 */
class TransactionsListViewModelFactory(
    private val dataSource: TransactionDao,
    private val application: Application
) : ViewModelProvider.Factory {

    @Suppress("unchecked_cast")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(TransactionsListViewModel::class.java)) {
            return TransactionsListViewModel(dataSource, application) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
