package gensokyo.hakurei.chitlist.transactiondetail

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import gensokyo.hakurei.chitlist.database.TransactionDao

private const val TAG = "TXDetailVMFactory"

/**
 * Provides the key for the transaction and the TransactionDao to the ViewModel.
 */
class TransactionDetailViewModelFactory(
    private val transactionKey: Long,
    private val dataSource: TransactionDao) : ViewModelProvider.Factory {
    @Suppress("unchecked_cast")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        Log.i(TAG, "create called with transactionKey=$transactionKey")

        if (modelClass.isAssignableFrom(TransactionDetailViewModel::class.java)) {
            return TransactionDetailViewModel(transactionKey, dataSource) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}