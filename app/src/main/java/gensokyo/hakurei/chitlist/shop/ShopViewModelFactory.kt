package gensokyo.hakurei.chitlist.shop

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import gensokyo.hakurei.chitlist.database.ShopDao

private const val TAG = "ShopVMFactory"

/**
 * Provides the [ShopDao] to the [ShopViewModel].
 */
class ShopViewModelFactory(
    private val dataSource: ShopDao,
    private val application: Application
) : ViewModelProvider.Factory {

    @Suppress("unchecked_cast")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ShopViewModel::class.java)) {
            return ShopViewModel(dataSource, application) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
