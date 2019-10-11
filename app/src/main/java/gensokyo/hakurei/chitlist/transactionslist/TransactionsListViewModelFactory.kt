package gensokyo.hakurei.chitlist.transactionslist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import gensokyo.hakurei.chitlist.database.TransactionDao

private const val TAG = "TXsListVMFactory"

/**
 * Provides the [TransactionDao] to the [TransactionsListViewModel].
 */
class TransactionsListViewModelFactory(
    private val dataSource: TransactionDao
) : ViewModelProvider.Factory {

    @Suppress("unchecked_cast")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(TransactionsListViewModel::class.java)) {
            return TransactionsListViewModel(dataSource) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
