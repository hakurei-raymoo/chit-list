package gensokyo.hakurei.chitlist.userslist

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import gensokyo.hakurei.chitlist.database.User
import gensokyo.hakurei.chitlist.database.UserDao
import kotlinx.coroutines.*

private const val TAG = "UsersListViewModel"

class UsersListViewModel(
    val database: UserDao, application: Application) : AndroidViewModel(application) {
    private var viewModelJob = Job()

    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)

    val users = database.getAllUsers()

    private val _navigateToEditUser = MutableLiveData<Long>()
    val navigateToEditUser
        get() = _navigateToEditUser

    fun onNewUser() {
        uiScope.launch {
            val newUser = User()
            insert(newUser)
        }
    }

    private suspend fun insert(user: User) {
        withContext(Dispatchers.IO) {
            database.insert(user)
        }
    }

    fun onEditUserClicked(id: Long) {
        _navigateToEditUser.value = id
    }

    fun onEditUserNavigated() {
        _navigateToEditUser.value = null
    }

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }
}
