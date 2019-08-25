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

    fun getItem() = item


    init {
        item = database.getItem(itemKey)
    }


    private val _navigateToItemsList = MutableLiveData<Boolean?>()
    val navigateToItemsList: LiveData<Boolean?>
        get() = _navigateToItemsList

    fun onUpdateItemDetails() {
        uiScope.launch {
            // IO is a thread pool for running operations that access the disk, such as
            // our Room database.
            withContext(Dispatchers.IO) {
                val currentItem = database.get(itemKey)
                currentItem.name = item.value?.name!!
                currentItem.price = item.value?.price!!//.toInt()
                database.update(currentItem)
                Log.i(TAG, "updated $currentItem")
            }
            _navigateToItemsList.value = true
        }
    }

    fun doneNavigating() {
        _navigateToItemsList.value = null
    }

    fun onDeleteItem() {
        uiScope.launch {
            // IO is a thread pool for running operations that access the disk, such as
            // our Room database.
            withContext(Dispatchers.IO) {
                val currentItem = database.get(itemKey)
                database.delete(currentItem)
                Log.i(TAG, "deleted $currentItem")
            }
            _navigateToItemsList.value = true
        }
    }

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }

}
