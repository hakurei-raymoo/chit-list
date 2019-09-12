package gensokyo.hakurei.chitlist.transactionslist

import android.app.Application
import android.util.Log
import androidx.lifecycle.*
import gensokyo.hakurei.chitlist.database.Account
import gensokyo.hakurei.chitlist.database.Transaction
import gensokyo.hakurei.chitlist.database.TransactionDao
import kotlinx.coroutines.*

private const val TAG = "TXsListViewModel"

class TransactionsListViewModel(
    val database: TransactionDao, application: Application) : AndroidViewModel(application) {
    private var viewModelJob = Job()

    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)

    // Autocomplete parameters
    private var accounts = listOf<Account>()
    val accountNames = mutableListOf<String>()

    // Search parameters
    val searchString = MutableLiveData("")
    private var searchId = MutableLiveData<Long?>(null)
    private var displaySearch = MutableLiveData(false)
    val publicDisplaySearch
        get() = displaySearch

    // Transaction lists
    val displayTransactions = MediatorLiveData<List<Transaction>>()
    private val allTransactions = database.getTransactions()
    private val accountTransactions: LiveData<List<Transaction>> =
        Transformations.switchMap(displaySearch) {
            Log.i(TAG, "accountTransactions transformation performed for ${searchId.value}")
            database.getTransactionsForAccount(searchId.value!!) // TODO: Remove not-null assertion.
        }

    private val _navigateToEditTransaction = MutableLiveData<Long>()
    val navigateToEditTransaction
        get() = _navigateToEditTransaction

    init {
        displayTransactions.addSource(allTransactions) { result -> displayTransactions.value = result }
        formatAccounts()
    }

    private fun formatAccounts() {
        uiScope.launch {
            withContext(Dispatchers.IO) {
                accounts = database.getAccounts()
                accounts.forEach {
                    accountNames.add(String.format("${it.firstName} ${it.lastName}"))
                }
                Log.i(TAG, "accountNames=$accountNames")
            }
        }
    }

    fun onSearch() {
        // Reset search.
        searchId = MutableLiveData(null)

        // Iterate to find index in name list.
        // Then get accountId at the same index in accounts list.
        // Relies on accounts and accountNames mirroring each other.
        var accountIndex = 0
        accountNames.forEach {
            if (searchString.value == it) {
                val accountId = accounts[accountIndex].id
                searchId = MutableLiveData(accountId)
                Log.i(TAG, "Matched name at index: $accountIndex, account=${accounts[accountIndex]}")
            }
            accountIndex++
        }

        // Select which LiveData source to expose.
        if (searchId.value != null) {
            // If valid account to query.
            Log.i(TAG, "Searching for ${searchId.value} ...")

            // If account transactions query is not displayed, display results.
            if (displaySearch.value == false) {
                displayTransactions.removeSource(allTransactions)
                displayTransactions.addSource(accountTransactions) { result -> displayTransactions.value = result }
            }
            displaySearch.value = true
        } else {
            Log.i(TAG, "Unable to convert ${searchId.value} to Long.")

            // If all transactions query is not displayed, display results.
            if (displaySearch.value == true) {
                displayTransactions.removeSource(accountTransactions)
                displayTransactions.addSource(allTransactions) { result -> displayTransactions.value = result }
            }
            displaySearch.value = false
        }
    }

    fun onNewTransaction() {
        // TODO: Remove new transaction magic number (-1L).
        _navigateToEditTransaction.value = -1L
    }

    fun onEditTransactionClicked(id: Long) {
        _navigateToEditTransaction.value = id
    }

    fun onEditTransactionNavigated() {
        _navigateToEditTransaction.value = null
    }

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }
}
