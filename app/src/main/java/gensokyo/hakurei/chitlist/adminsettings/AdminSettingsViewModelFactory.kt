package gensokyo.hakurei.chitlist.adminsettings

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import gensokyo.hakurei.chitlist.database.AdminDao

private const val TAG = "AdminSettingsViewModelFactory"

/**
 * Provides the [AdminDao] and context to the [AdminSettingsViewModel].
 */
class AdminSettingsViewModelFactory(
    private val dataSource: AdminDao,
    private val application: Application
) : ViewModelProvider.Factory {

    @Suppress("unchecked_cast")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AdminSettingsViewModel::class.java)) {
            return AdminSettingsViewModel(dataSource, application) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
