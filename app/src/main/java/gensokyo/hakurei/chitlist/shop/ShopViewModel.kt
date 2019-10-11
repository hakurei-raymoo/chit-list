package gensokyo.hakurei.chitlist.shop

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import gensokyo.hakurei.chitlist.database.ShopDao
import kotlinx.coroutines.*

private const val TAG = "ShopViewModel"

class ShopViewModel(
    private val database: ShopDao,
    application: Application
) : AndroidViewModel(application) {
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
