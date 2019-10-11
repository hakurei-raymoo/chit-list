package gensokyo.hakurei.chitlist.itemslist

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import gensokyo.hakurei.chitlist.database.ItemDao
import kotlinx.coroutines.*

private const val TAG = "ItemsListViewModel"

class ItemsListViewModel(
    val database: ItemDao, application: Application) : AndroidViewModel(application) {
    private var viewModelJob = Job()

    private val _items = database.getItems()
    val items
        get() = _items

    private val _navigateToEditItem = MutableLiveData<Long>()
    val navigateToEditItem
        get() = _navigateToEditItem

    fun onNewItem() {
        // TODO: Remove new item magic number (-1L).
        _navigateToEditItem.value = -1L
    }

    fun onEditItemClicked(id: Long) {
        _navigateToEditItem.value = id
    }

    fun onEditItemNavigated() {
        _navigateToEditItem.value = null
    }

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }
}
