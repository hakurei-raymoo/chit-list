package gensokyo.hakurei.chitlist.checkout

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import gensokyo.hakurei.chitlist.database.Account
import gensokyo.hakurei.chitlist.database.Item
import gensokyo.hakurei.chitlist.database.ShopDao
import gensokyo.hakurei.chitlist.database.Transaction
import kotlinx.coroutines.*

private const val TAG = "CheckoutViewModel"

class CheckoutViewModel(
    private val database: ShopDao
) : ViewModel() {

    private val viewModelJob = Job()

    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)

    private val _navigateToCheckout = MutableLiveData<Boolean>()
    val navigateToCheckout: LiveData<Boolean>
        get() = _navigateToCheckout

    init {
        Log.i(TAG, "Init")
    }

    fun checkout(account: Account, cart: List<Item>?) {
        if (cart != null) {
            if (cart.isNotEmpty()) {
                cart.forEach {
                    val transaction = Transaction(
                        accountId = account.accountId,
                        creatorId = account.accountId,
                        itemId = it.itemId,
                        amount = it.price
                    )
                    Log.i(TAG, "Checkout: $transaction")

                    uiScope.launch {
                        insert(transaction)
                    }
                }
            } else {
                Log.i(TAG, "Cart empty.")
            }
        } else {
            Log.i(TAG, "Cart null.")
        }
    }

    suspend fun insert(transaction: Transaction) {
        withContext(Dispatchers.IO) {
            database.insert(transaction)
        }
    }

    fun onCheckoutClicked() {
        _navigateToCheckout.value = true
    }

    fun onCheckoutNavigated() {
        _navigateToCheckout.value = null
    }

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }
}
