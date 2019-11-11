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
    private val itemKey: Long,
    private val dataSource: ItemDao
) : ViewModel() {

    private val viewModelJob = Job()
    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)

    val isNew = (itemKey == -1L)

    // Check itemKey to either get Item from database or insert a new one.
    private var _item =
        if (isNew) {
            MutableLiveData<Item>(Item())
        } else {
            dataSource.getItem(itemKey)
        }
    val item: LiveData<Item>
        get() = _item

    private val _navigateToItemsList = MutableLiveData<Boolean?>()
    val navigateToItemsList: LiveData<Boolean?>
        get() = _navigateToItemsList
    
    init {
        Log.i(TAG, "Init")
    }

    fun onCreateClicked() {
        uiScope.launch {
            create()
            _navigateToItemsList.value = true
        }
    }

    fun onUpdateClicked() {
        uiScope.launch {
            update()
            _navigateToItemsList.value = true
        }
    }

    private suspend fun create() {
        withContext(Dispatchers.IO) {
            dataSource.insert(item.value!!)
            Log.i(TAG, "Inserted ${item.value!!}")
        }
    }

    private suspend fun update() {
        withContext(Dispatchers.IO) {
            dataSource.update(item.value!!)
            Log.i(TAG, "Updated ${item.value!!}")
        }
    }

    fun onCancelClicked() {
        _navigateToItemsList.value = true
    }

    fun doneNavigating() {
        _navigateToItemsList.value = null
    }

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }
}
