package gensokyo.hakurei.chitlist.shop

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import gensokyo.hakurei.chitlist.database.ShopDao

private const val TAG = "ShopVMFactory"

/**
 * Provides the [ShopDao] to the [ShopViewModel].
 */
class ShopViewModelFactory(
    private val dataSource: ShopDao
) : ViewModelProvider.Factory {

    @Suppress("unchecked_cast")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ShopViewModel::class.java)) {
            return ShopViewModel(dataSource) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
