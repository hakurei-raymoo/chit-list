package gensokyo.hakurei.chitlist

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import gensokyo.hakurei.chitlist.database.Account

private const val TAG = "SharedViewModel"

class SharedViewModel : ViewModel() {
    private var _user: Account? = null
    val user
        get() = _user

    private val _cart = mutableListOf<Long>()
    val cart: List<Long>
        get() = _cart

    init {
        Log.i(TAG, "Init")
    }

    fun addItem(itemId: Long) {
        _cart.add(itemId)
        Log.i(TAG, "$user cart=$cart")

        // TODO: Create new transaction.
    }

    fun login(account: Account) {
        _user = account
        Log.i(TAG, "Logged in as: $user")
    }

    fun logout() {
        _user = null
        Log.i(TAG, "Logged out")
    }
}
