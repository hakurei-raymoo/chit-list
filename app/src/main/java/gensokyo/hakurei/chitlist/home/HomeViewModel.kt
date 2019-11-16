package gensokyo.hakurei.chitlist.home

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import gensokyo.hakurei.chitlist.database.*
import kotlinx.coroutines.*

private const val TAG = "HomeViewModel"

class HomeViewModel (
    val userId: Long,
    private val dataSource: ShopDao
) : ViewModel() {

    private val viewModelJob = Job()
    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)

    // The current user.
    private val _user = dataSource.getAccount(userId)
    val user: LiveData<Account>
        get() = _user

    // The users history.
    private val _history = dataSource.getHistory(userId)
    val history: LiveData<List<TransactionWithChildren>>
        get() = _history

    // Sum all transactions matching the users accountId.
    private val _balance = Transformations.map(history) { sumTransactions(it) }
    val balance: LiveData<Int>
        get() = _balance

    // List of items in cart and LiveData version of same list.
    private val _cartList = mutableListOf<Item>()
    private val _cart = MutableLiveData<List<Item>>()
    val cart: LiveData<List<Item>>
        get() = _cart

    // List of items available in the shop.
    private val _items = dataSource.getItems()
    val items
        get() = _items

    private val _navigateToLogin = MutableLiveData<Boolean>()
    val navigateToLogin: LiveData<Boolean>
        get() = _navigateToLogin

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

    private fun sumTransactions(transactions: List<TransactionWithChildren>): Int {
        var sum = 0
        transactions.forEach {
            sum += it.amount
        }
        return sum
    }

    fun onCheckoutClicked() {
        uiScope.launch{
            withContext(Dispatchers.IO) {
                Log.i(TAG, "cart=${cart.value}")
                insertCart(cart.value)

                // Ensure database inserts are done before navigating.
                _navigateToLogin.postValue(true)
            }
        }
    }

    private fun insertCart(cart: List<Item>?) {
        cart?.forEach {
            val transaction = Transaction(
                accountId = user.value?.accountId!!,
                creatorId = user.value?.accountId!!,
                itemId = it.itemId,
                amount = it.price,
                type = true
            )
            Log.i(TAG, "inserting: $transaction")
            dataSource.insert(transaction)
        }
    }

    fun onLoginNavigated() {
        _navigateToLogin.value = false
    }

    fun onHistoryClicked(transactionId: Long) {
        Log.i(TAG, "Clicked $transactionId")
    }

    fun changePassword(newPassword: String) {
        val account = user.value!!
        account.passwordHash = newPassword

        uiScope.launch {
            withContext(Dispatchers.IO) {
                Log.i(TAG, "updating: $account")
                dataSource.update(account)
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
        Log.i(TAG, "Cleared")
    }
}
