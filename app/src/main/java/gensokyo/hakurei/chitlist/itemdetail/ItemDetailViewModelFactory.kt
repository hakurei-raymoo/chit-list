package gensokyo.hakurei.chitlist.itemdetail

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import gensokyo.hakurei.chitlist.database.Item
import gensokyo.hakurei.chitlist.database.ItemDao

private const val TAG = "ItemDetailVMFactory"

/**
 * Provides the key for the [Item] and the [ItemDao] to the [ItemDetailViewModel].
 */
class ItemDetailViewModelFactory(
    private val itemKey: Long,
    private val dataSource: ItemDao
) : ViewModelProvider.Factory {
    @Suppress("unchecked_cast")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        Log.i(TAG, "create called with itemKey=$itemKey")

        if (modelClass.isAssignableFrom(ItemDetailViewModel::class.java)) {
            return ItemDetailViewModel(itemKey, dataSource) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}