package gensokyo.hakurei.chitlist.viewmodels

import android.app.Application
import android.net.Uri
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.sqlite.db.SimpleSQLiteQuery
import gensokyo.hakurei.chitlist.utilities.Converter
import gensokyo.hakurei.chitlist.utilities.DATABASE_NAME
import gensokyo.hakurei.chitlist.database.AdminSettingsDao
import gensokyo.hakurei.chitlist.database.AppDatabase
import kotlinx.coroutines.*
import java.io.*

private const val TAG = "AdminSettingsViewModel"

class AdminSettingsViewModel(
    private val database: AdminSettingsDao,
    val application: Application
) : ViewModel() {

    private var viewModelJob = Job()

    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)

    private val _exitApp = MutableLiveData<Boolean>()
    val exitApp: LiveData<Boolean>
        get() = _exitApp

    private val _returnMessage = MutableLiveData<String>()
    val returnMessage: LiveData<String>
        get() = _returnMessage

    init {
        Log.i(TAG, "Init")
        // TODO: Add scheduled backups.
    }

    fun messageReturned() {
        _returnMessage.value = null
    }

    // TODO: Sum balances.
    fun exportBalances(uri: Uri) {
        try {
            uiScope.launch {
                withContext(Dispatchers.IO) {
                    // Get data to write.
                    val accounts = database.getAccounts()

                    application.contentResolver.openFileDescriptor(uri, "w")?.use {
                        // use{} lets the document provider know you're done by automatically closing the stream
                        PrintWriter(FileOutputStream(it.fileDescriptor)).use { out ->
                            out.println(
                                "account_id," +
                                "first_name," +
                                "last_name," +
                                "location," +
                                "contact_number," +
                                "email_address"
                            )
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
                        }
                    }
                    _returnMessage.postValue("${accounts.size} accounts exported to $uri")
                }
            }
        } catch (e: FileNotFoundException) {
            e.printStackTrace()
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    fun exportItems(uri: Uri) {
        try {
            uiScope.launch {
                withContext(Dispatchers.IO) {
                    // Get data to write.
                    val items = database.getItems()

                    application.contentResolver.openFileDescriptor(uri, "w")?.use {
                        // use{} lets the document provider know you're done by automatically closing the stream
                        PrintWriter(FileOutputStream(it.fileDescriptor)).use { out ->
                            out.println("item_id,name,price")
                            items.forEach {
                                out.println(
                                    "${it.itemId},${it.name},${it.price}"
                                )
                            }
                        }
                    }
                    _returnMessage.postValue("${items.size} items exported to $uri")
                }
            }
        } catch (e: FileNotFoundException) {
            e.printStackTrace()
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    fun exportTransactions(uri: Uri) {
        // TODO: Allow user to select columns to export.
        try {
            uiScope.launch {
                withContext(Dispatchers.IO) {
                    // Get data to write.
                    val transactions = database.getTransactionsWithChildren()

                    application.contentResolver.openFileDescriptor(uri, "w")?.use {
                        // use{} lets the document provider know you're done by automatically closing the stream
                        PrintWriter(FileOutputStream(it.fileDescriptor)).use { out ->
                            out.println(
                                "transaction_id," +
                                "time," +
                                "account," +
                                "creator," +
                                "item," +
                                "amount," +
                                "comments"
                            )
                            transactions.forEach {
                                out.println(
                                    "${it.transactionId}," +
                                    "${Converter.convertLongToDateStringShort(it.time)}," +
                                    "${it.account.firstName} " +
                                    "${it.account.lastName}," +
                                    "${it.creator.firstName} " +
                                    "${it.creator.lastName}," +
                                    "${it.item.name}," +
                                    "${Converter.addDecimal(it.amount)}," +
                                    "${it.comments}"
                                )
                            }
                        }
                    }
                    _returnMessage.postValue("${transactions.size} transactions exported to $uri")
                }
            }
        } catch (e: FileNotFoundException) {
            e.printStackTrace()
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    fun backupDatabase(uri: Uri) {
        uiScope.launch {
            withContext(Dispatchers.IO) {
                // Finish writing to database.
                database.checkpoint(SimpleSQLiteQuery("pragma wal_checkpoint(full)"))

                // Copy database file.
                val inputStream = application.getDatabasePath(DATABASE_NAME).inputStream()
                val outputStream = application.contentResolver.openOutputStream(uri)
                Log.i(TAG, "inStream=${inputStream}")
                Log.i(TAG, "outStream=${outputStream}")
                inputStream.use { input ->
                    outputStream?.use { output ->
                        input.copyTo(output)
                    }
                }
                _returnMessage.postValue("$DATABASE_NAME backed up to $uri")
            }
        }
    }

    fun restoreDatabase(uri: Uri) {
        uiScope.launch {
            withContext(Dispatchers.IO) {
                // Finish writing to database.
                database.checkpoint(SimpleSQLiteQuery("pragma wal_checkpoint(full)"))
                AppDatabase.getInstance(application).close()

                // Copy database file.
                val inputStream = application.contentResolver.openInputStream(uri)
                val outputStream = application.getDatabasePath(DATABASE_NAME).outputStream()
                Log.i(TAG, "inStream=${inputStream}")
                Log.i(TAG, "outStream=${outputStream}")
                inputStream.use { input ->
                    outputStream.use { output ->
                        input?.copyTo(output)
                    }
                }

                _returnMessage.postValue("$DATABASE_NAME restored. App will now close.")

                // Wait before exiting so messages can be displayed.
                Thread.sleep(1000L)
                _exitApp.postValue(true)
            }
        }
    }

    fun exitApp() {
        _exitApp.value = true
    }

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }
}
