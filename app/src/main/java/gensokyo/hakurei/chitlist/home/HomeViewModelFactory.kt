package gensokyo.hakurei.chitlist.home

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import gensokyo.hakurei.chitlist.database.ShopDao

private const val TAG = "HomeViewModelFactory"

/**
 * Provides the key for the user and the [ShopDao] to the [HomeViewModel].
 */
class HomeViewModelFactory(
    private val userId: Long,
    private val dataSource: ShopDao
) : ViewModelProvider.Factory {
    @Suppress("unchecked_cast")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(HomeViewModel::class.java)) {
            Log.i(TAG, "HomeViewModel created with userId=$userId")
            return HomeViewModel(userId, dataSource) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}