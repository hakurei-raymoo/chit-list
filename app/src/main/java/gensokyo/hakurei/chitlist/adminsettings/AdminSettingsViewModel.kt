package gensokyo.hakurei.chitlist.adminsettings

import android.app.Application
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import gensokyo.hakurei.chitlist.convertLongToDateStringExpanded
import gensokyo.hakurei.chitlist.database.AdminDao
import kotlinx.coroutines.*
import java.io.File

private const val TAG = "AdminSettingsViewModel"

class AdminSettingsViewModel(
    private val database: AdminDao,
    val application: Application
) : ViewModel() {

    private var viewModelJob = Job()

    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)

    var path = MutableLiveData<String>("")

    private val _exportReturn = MutableLiveData<String>()
    val exportReturn: LiveData<String>
        get() = _exportReturn

    private val _exitApp = MutableLiveData<Boolean>()
    val exitApp: LiveData<Boolean>
        get() = _exitApp

    init {
        Log.i(TAG, "Init")
    }

    fun returnedExport() {
        _exportReturn.value = null
    }

    fun onExportBalancesClicked() {
        uiScope.launch {
            withContext(Dispatchers.IO) {
                // Get data to write.
                val accounts = database.getAccounts()

                // Setup file.
                val filename = "balances.csv"
                val file = File(application.filesDir, filename)
                Log.i(TAG, "path=${file.path}")
                file.printWriter().use { out ->
                    out.println("account_id," +
                            "first_name," +
                            "last_name," +
                            "location," +
                            "contact_number," +
                            "email_address")
                    accounts.forEach {
                        out.println(
                            "${it.accountId}," +
                                    "${it.firstName}," +
                                    "${it.lastName}," +
                                    "${it.location}," +
                                    "${it.contactNumber}," +
                                    "${it.emailAddress}"
                        )
                    }
                    out.println("Export completed on " +
                            convertLongToDateStringExpanded(System.currentTimeMillis()))
                }

                _exportReturn.postValue("${accounts.size} accounts exported to $filename")
            }
        }
    }

    fun onExportItemsClicked() {
        uiScope.launch {
            withContext(Dispatchers.IO) {
                // Get data to write.
                val items = database.getItems()

                // Setup file.
                val filename = "items.csv"
                val file = File(application.filesDir, filename)
                Log.i(TAG, "path=${file.path}")
                file.printWriter().use { out ->
                    out.println("item_id,name,price")
                    items.forEach {
                        out.println(
                            "${it.itemId},${it.name},${it.price}"
                        )
                    }
                    out.println("Export completed on " +
                            convertLongToDateStringExpanded(System.currentTimeMillis()))
                }

                _exportReturn.postValue("${items.size} items exported to $filename")
            }
        }
    }

    fun onExportTransactionsClicked() {
        uiScope.launch {
            withContext(Dispatchers.IO) {
                // Get data to write.
                val transactions = database.getTransactionsWithChildren()

                // Setup file.
                val filename = "transactions.csv"
                val file = File(application.filesDir, filename)
                Log.i(TAG, "path=${file.path}")
                file.printWriter().use { out ->
                    out.println("transaction_id," +
                            "time," +
                            "account." +
                            "account_id," +
                            "account.first_name," +
                            "account.last_name," +
                            "item.name," +
                            "amount," +
                            "comments")
                    transactions.forEach {
                        out.println(
                            "${it.transactionId}," +
                                    "${it.time}," +
                                    "${it.account.accountId}," +
                                    "${it.account.firstName}," +
                                    "${it.account.lastName}," +
                                    "${it.item.name}," +
                                    "${it.amount}," +
                                    "${it.comments}"
                        )
                    }
                    out.println("Export completed on " +
                            convertLongToDateStringExpanded(System.currentTimeMillis()))
                }

                _exportReturn.postValue("${transactions.size} transactions exported to $filename")
            }
        }
    }

    fun onBackupDatabaseClicked() {
    }

    fun onRestoreDatabaseClicked() {
    }

    fun onExitAppClicked() {
        _exitApp.value = true
    }

    fun onAppExited() {
        _exitApp.value = null
    }

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }
}
