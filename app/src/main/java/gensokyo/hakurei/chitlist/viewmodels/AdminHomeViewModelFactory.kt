package gensokyo.hakurei.chitlist.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import gensokyo.hakurei.chitlist.database.AdminHomeDao

private const val TAG = "AdminHomeVMFactory"

/**
 * Provides the key for the user and the [AdminHomeDao] to the [AdminHomeViewModel].
 */
class AdminHomeViewModelFactory(
    private val userId: Long,
    private val dataSource: AdminHomeDao
) : ViewModelProvider.Factory {
    @Suppress("unchecked_cast")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AdminHomeViewModel::class.java)) {
            Log.i(TAG, "AdminHomeViewModel created with userId=$userId")
            return AdminHomeViewModel(userId, dataSource) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}