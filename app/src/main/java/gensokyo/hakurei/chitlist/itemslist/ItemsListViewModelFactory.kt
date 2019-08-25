package gensokyo.hakurei.chitlist.itemslist

import android.app.Application
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import gensokyo.hakurei.chitlist.database.ItemDao

private const val TAG = "ItemsListVMFactory"

/**
 * Provides the ItemDao and context to the ViewModel.
 */
class ItemsListViewModelFactory(
    private val dataSource: ItemDao,
    private val application: Application) : ViewModelProvider.Factory {

    @Suppress("unchecked_cast")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        Log.i(TAG, "create called")

        if (modelClass.isAssignableFrom(ItemsListViewModel::class.java)) {
            return ItemsListViewModel(dataSource, application) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
