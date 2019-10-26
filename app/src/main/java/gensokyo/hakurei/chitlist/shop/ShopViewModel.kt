package gensokyo.hakurei.chitlist.shop

import android.util.Log
import androidx.lifecycle.ViewModel
import gensokyo.hakurei.chitlist.database.ShopDao
import kotlinx.coroutines.*

private const val TAG = "ShopViewModel"

class ShopViewModel(
    private val database: ShopDao
) : ViewModel() {
    private var viewModelJob = Job()

    private val _items = database.getItems()
    val items
        get() = _items

    init {
        Log.i(TAG, "Init")
    }

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }
}
