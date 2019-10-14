package gensokyo.hakurei.chitlist.checkout

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import gensokyo.hakurei.chitlist.database.ShopDao
import gensokyo.hakurei.chitlist.shop.ShopViewModel

private const val TAG = "CheckoutVMFactory"

/**
 * Provides the [ShopDao] to the [CheckoutViewModel].
 */
class CheckoutViewModelFactory(
    private val dataSource: ShopDao
) : ViewModelProvider.Factory {

    @Suppress("unchecked_cast")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(CheckoutViewModel::class.java)) {
            return CheckoutViewModel(dataSource) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
