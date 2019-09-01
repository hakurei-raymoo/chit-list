package gensokyo.hakurei.chitlist.itemslist

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import gensokyo.hakurei.chitlist.database.ItemDao

private const val TAG = "ItemsListVMFactory"

/**
 * Provides the [ItemDao] and context to the [ItemsListViewModel].
 */
class ItemsListViewModelFactory(
    private val dataSource: ItemDao,
    private val application: Application
) : ViewModelProvider.Factory {

    @Suppress("unchecked_cast")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ItemsListViewModel::class.java)) {
            return ItemsListViewModel(dataSource, application) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
