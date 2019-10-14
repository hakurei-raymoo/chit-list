package gensokyo.hakurei.chitlist.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import gensokyo.hakurei.chitlist.SharedViewModel
import gensokyo.hakurei.chitlist.database.ShopDao

private const val TAG = "SharedViewModelFactory"

/**
 * Provides the [ShopDao] to the [SharedViewModel].
 */
class SharedViewModelFactory(
    private val dataSource: ShopDao
) : ViewModelProvider.Factory {

    @Suppress("unchecked_cast")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SharedViewModel::class.java)) {
            return SharedViewModel() as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
