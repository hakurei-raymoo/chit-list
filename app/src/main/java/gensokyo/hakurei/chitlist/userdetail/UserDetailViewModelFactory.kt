package gensokyo.hakurei.chitlist.userdetail

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import gensokyo.hakurei.chitlist.database.UserDao

private const val TAG = "UserDetailVMFactory"

/**
 * Provides the key for the user and the UserDao to the ViewModel.
 */
class UserDetailViewModelFactory(
    private val userKey: Long,
    private val dataSource: UserDao) : ViewModelProvider.Factory {
    @Suppress("unchecked_cast")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        Log.i(TAG, "create called with userKey=$userKey")

        if (modelClass.isAssignableFrom(UserDetailViewModel::class.java)) {
            return UserDetailViewModel(userKey, dataSource) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}