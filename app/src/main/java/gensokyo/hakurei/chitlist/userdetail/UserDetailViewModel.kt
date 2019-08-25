package gensokyo.hakurei.chitlist.userdetail

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import gensokyo.hakurei.chitlist.database.User
import gensokyo.hakurei.chitlist.database.UserDao
import kotlinx.coroutines.*

private const val TAG = "UserDetailViewModel"

class UserDetailViewModel(private val userKey: Long = 0L, dataSource: UserDao) : ViewModel() {

    val database = dataSource

    private val viewModelJob = Job()

    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)

    private val user: LiveData<User>

    fun getUser() = user


    init {
        user = database.getUserWithId(userKey)
    }


    private val _navigateToUsersList = MutableLiveData<Boolean?>()
    val navigateToUsersList: LiveData<Boolean?>
        get() = _navigateToUsersList

    fun onUpdateUserDetails() {
        uiScope.launch {
            // IO is a thread pool for running operations that access the disk, such as
            // our Room database.
            withContext(Dispatchers.IO) {
                val currentUser = database.get(userKey)
                currentUser.firstName = user.value?.firstName!!
                currentUser.lastName = user.value?.lastName!!
                currentUser.defAuthHash = user.value?.defAuthHash!!//.hash
                database.update(currentUser)
                Log.i(TAG, "updated $currentUser")
            }
            _navigateToUsersList.value = true
        }
    }

    fun doneNavigating() {
        _navigateToUsersList.value = null
    }

    fun onDeleteUser() {
        uiScope.launch {
            // IO is a thread pool for running operations that access the disk, such as
            // our Room database.
            withContext(Dispatchers.IO) {
                val currentUser = database.get(userKey)
                database.delete(currentUser)
                Log.i(TAG, "deleted $currentUser")
            }
            _navigateToUsersList.value = true
        }
    }

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }

}
