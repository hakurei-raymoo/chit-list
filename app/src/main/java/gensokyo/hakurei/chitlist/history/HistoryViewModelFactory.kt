package gensokyo.hakurei.chitlist.history

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import gensokyo.hakurei.chitlist.database.ShopDao

private const val TAG = "HistoryViewModelFactory"

/**
 * Provides the [ShopDao] to the [HistoryViewModel].
 */
class HistoryViewModelFactory(
    private val dataSource: ShopDao
) : ViewModelProvider.Factory {

    @Suppress("unchecked_cast")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(HistoryViewModel::class.java)) {
            return HistoryViewModel(dataSource) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
