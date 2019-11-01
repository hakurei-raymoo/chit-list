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

    // List of items in cart and LiveData version of same list.
    private val _cartList = mutableListOf<Item>()
    private val _cart = MutableLiveData<List<Item>>()
    val cart: LiveData<List<Item>>
        get()  =_cart

    private val _balance = MutableLiveData<Int>()
    val balance: LiveData<Int>
        get() = _balance

    init {
        Log.i(TAG, "Init")
    }

    fun addItem(item: Item) {
        _cartList.add(item)
        _cart.value = _cartList
    }

    fun removeItem(item: Item) {
        _cartList.remove(item)
        _cart.value = _cartList
    }

    fun login(account: Account) {
        _user = account
        _cartList.clear()
        _cart.value = null
        _balance.value = null
        Log.i(TAG, "Logged in as: $user")
    }

    fun setBalance(balance: Int) {
        _balance.postValue(balance)
    }
}
