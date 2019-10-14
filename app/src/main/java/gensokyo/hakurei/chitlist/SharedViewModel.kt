package gensokyo.hakurei.chitlist

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import gensokyo.hakurei.chitlist.database.Account
import gensokyo.hakurei.chitlist.database.Item

private const val TAG = "SharedViewModel"

class SharedViewModel : ViewModel() {

    private var _user: Account? = null
    val user
        get() = _user

    var balance = MutableLiveData<Int>()

    private val _items = mutableListOf<Item>()
    private val _cart = MutableLiveData<List<Item>>()
    val cart: LiveData<List<Item>>
        get()  =_cart

    init {
        Log.i(TAG, "Init")
    }

    fun addItem(item: Item) {
        _items.add(item)
        _cart.value = _items
    }

    fun removeItem(item: Item) {
        _items.remove(item)
        _cart.value = _items
    }

    fun login(account: Account) {
        _user = account
        _items.clear()
        _cart.value = null
        Log.i(TAG, "Logged in as: $user")
    }

    fun logout() {
        _user = null
        Log.i(TAG, "Logged out")
    }
}
