package gensokyo.hakurei.chitlist.userslist

import android.app.Application
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import gensokyo.hakurei.chitlist.database.UserDao

private const val TAG = "UsersListVMFactory"

/**
 * Provides the UserDao and context to the ViewModel.
 */
class UsersListViewModelFactory(
    private val dataSource: UserDao,
    private val application: Application) : ViewModelProvider.Factory {

    @Suppress("unchecked_cast")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        Log.i(TAG, "create called")

        if (modelClass.isAssignableFrom(UsersListViewModel::class.java)) {
            return UsersListViewModel(dataSource, application) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
