package gensokyo.hakurei.chitlist.itemdetail

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import gensokyo.hakurei.chitlist.database.Item
import gensokyo.hakurei.chitlist.database.ItemDao
import kotlinx.coroutines.*

private const val TAG = "ItemDetailViewModel"

class ItemDetailViewModel(
    itemKey: Long = 0L,
    private val database: ItemDao
) : ViewModel() {

    private val viewModelJob = Job()

    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)

    private val _item: LiveData<Item>
    val item
        get() = _item

    private val _navigateToItemsList = MutableLiveData<Boolean?>()
    val navigateToItemsList: LiveData<Boolean?>
        get() = _navigateToItemsList

    /**
     * Check itemKey to either get existing Item or insert a new one.
     */
    init {
        Log.i(TAG, "Init")

        if (itemKey == -1L) {
            newItem()
            _item = database.getLastItem()
        } else {
            _item = database.getItem(itemKey)
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

    fun onUpdateClicked() {
        uiScope.launch {
            update()
            _navigateToItemsList.value = true
        }
    }

    private suspend fun update() {
        withContext(Dispatchers.IO) {
            database.update(item.value!!)
            Log.i(TAG, "Updated ${item.value!!}")
        }
    }

    fun doneNavigating() {
        _navigateToItemsList.value = null
    }

    fun onDeleteClicked() {
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
