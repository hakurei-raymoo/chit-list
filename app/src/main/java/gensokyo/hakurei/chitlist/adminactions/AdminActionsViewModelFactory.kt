package gensokyo.hakurei.chitlist.adminactions

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import gensokyo.hakurei.chitlist.database.AdminActionsDao

private const val TAG = "AdminActionsViewModelFactory"

/**
 * Provides the [AdminActionsDao] and context to the [AdminActionsViewModel].
 */
class AdminActionsViewModelFactory(
    private val dataSource: AdminActionsDao,
    private val application: Application
) : ViewModelProvider.Factory {

    @Suppress("unchecked_cast")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AdminActionsViewModel::class.java)) {
            return AdminActionsViewModel(dataSource, application) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
