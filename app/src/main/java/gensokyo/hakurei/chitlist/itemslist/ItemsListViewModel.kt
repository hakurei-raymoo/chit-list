package gensokyo.hakurei.chitlist.itemslist

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import gensokyo.hakurei.chitlist.database.Item
import gensokyo.hakurei.chitlist.database.ItemDao
import kotlinx.coroutines.*

private const val TAG = "ItemsListViewModel"

class ItemsListViewModel(
    val database: ItemDao, application: Application) : AndroidViewModel(application) {
    private var viewModelJob = Job()

    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)

    val items = database.getItems()

    private val _navigateToEditItem = MutableLiveData<Long>()
    val navigateToEditItem
        get() = _navigateToEditItem

    fun onNewItem() {
        uiScope.launch {
            val newItem = Item()
            insert(newItem)
        }
    }

    private suspend fun insert(item: Item) {
        withContext(Dispatchers.IO) {
            database.insert(item)
        }
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
