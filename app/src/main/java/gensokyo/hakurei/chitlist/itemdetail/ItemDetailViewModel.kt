package gensokyo.hakurei.chitlist.itemdetail

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import gensokyo.hakurei.chitlist.database.Item
import gensokyo.hakurei.chitlist.database.ItemDao
import kotlinx.coroutines.*

private const val TAG = "ItemDetailViewModel"

class ItemDetailViewModel(private val itemKey: Long = 0L, dataSource: ItemDao) : ViewModel() {

    val database = dataSource

    private val viewModelJob = Job()

    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)

    private val item: LiveData<Item>
    // TODO: Remove after testing.
    val publicItem
        get() = item

    private val _navigateToItemsList = MutableLiveData<Boolean?>()
    val navigateToItemsList: LiveData<Boolean?>
        get() = _navigateToItemsList

    fun getItem() = item


    /**
     * Check itemKey to either get existing Item or insert a new one.
     */
    init {
        if (itemKey == -1L) {
            newItem()
            item = database.getLastItem()
        } else {
            item = database.getItem(itemKey)
        }
    }

    private fun newItem() {
        uiScope.launch {
            withContext(Dispatchers.IO) {
                val newItem = Item()
                database.insert(newItem)
                Log.i(TAG, "Inserted $newItem")
            }
        }
    }

    fun onSubmitItemDetails() {
        uiScope.launch {
            update()
            _navigateToItemsList.value = true
        }
    }

    private suspend fun update() {
        withContext(Dispatchers.IO) {
            // TODO: Transform price between cents to dollars.
            database.update(item.value!!)
            Log.i(TAG, "Updated ${item.value!!}")
        }
    }

    fun doneNavigating() {
        _navigateToItemsList.value = null
    }

    fun onDeleteItem() {
        uiScope.launch {
            withContext(Dispatchers.IO) {
                database.delete(item.value!!)
                Log.i(TAG, "Deleted ${item.value!!}")
            }
            _navigateToItemsList.value = true
        }
    }

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }

}
