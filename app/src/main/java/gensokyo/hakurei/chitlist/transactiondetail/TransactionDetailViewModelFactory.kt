package gensokyo.hakurei.chitlist.transactiondetail

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import gensokyo.hakurei.chitlist.database.Transaction
import gensokyo.hakurei.chitlist.database.TransactionDao

private const val TAG = "TXDetailVMFactory"

/**
 * Provides the key for the [Transaction] and the [TransactionDao] to the [TransactionDetailViewModel].
 */
class TransactionDetailViewModelFactory(
    private val transactionKey: Long,
    private val dataSource: TransactionDao
) : ViewModelProvider.Factory {
    @Suppress("unchecked_cast")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(TransactionDetailViewModel::class.java)) {
            Log.i(TAG, "TransactionDetailViewModel created with transactionKey=$transactionKey")
            return TransactionDetailViewModel(transactionKey, dataSource) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}