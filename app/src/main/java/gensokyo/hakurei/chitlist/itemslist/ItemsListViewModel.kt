package gensokyo.hakurei.chitlist.itemslist

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import gensokyo.hakurei.chitlist.database.ItemDao
import kotlinx.coroutines.*

private const val TAG = "ItemsListViewModel"

class ItemsListViewModel(
    private val database: ItemDao
) : ViewModel() {
    private var viewModelJob = Job()

    private val _items = database.getItems()
    val items
        get() = _items

    private val _navigateToEditItem = MutableLiveData<Long>()
    val navigateToEditItem: LiveData<Long>
        get() = _navigateToEditItem

    init {
        Log.i(TAG, "Init")
    }

    fun onNewItem() {
        _navigateToEditItem.value = -1L
    }

    fun onItemClicked(id: Long) {
        _navigateToEditItem.value = id
    }

    fun onItemNavigated() {
        _navigateToEditItem.value = null
    }

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }
}
