package gensokyo.hakurei.chitlist.payaccount

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import gensokyo.hakurei.chitlist.database.Transaction
import gensokyo.hakurei.chitlist.database.TransactionDao

private const val TAG = "PayAccountVMFactory"

/**
 * Provides the key for the [Transaction] and the [TransactionDao] to the [PayAccountViewModel].
 */
class PayAccountViewModelFactory(
    private val creatorId: Long,
    private val dataSource: TransactionDao
) : ViewModelProvider.Factory {
    @Suppress("unchecked_cast")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(PayAccountViewModel::class.java)) {
            Log.i(TAG, "PayAccountViewModel created with creatorId=$creatorId")
            return PayAccountViewModel(creatorId, dataSource) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}