package gensokyo.hakurei.chitlist.transactiondetail

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import gensokyo.hakurei.chitlist.utilities.formatAccounts
import gensokyo.hakurei.chitlist.utilities.formatItems
import gensokyo.hakurei.chitlist.database.*
import kotlinx.coroutines.*
import kotlin.math.min

private const val TAG = "TXDetailViewModel"

class TransactionDetailViewModel(
    private val creatorId: Long,
    private val transactionKey: Long,
    private val dataSource: TransactionDao
) : ViewModel() {

    private val viewModelJob = Job()
    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)

    val isNew = (transactionKey == -1L)

    // Check transactionKey to either get Transaction from database or insert a new one.
    private var _transaction =
        if (isNew) {
            MutableLiveData<Transaction>(Transaction(accountId = creatorId, creatorId = creatorId, itemId = 1L))
        } else {
            dataSource.getTransaction(transactionKey)
        }
    val transaction: LiveData<Transaction>
        get() = _transaction

    // Accounts AutoCompleteTextView adapter list.
    private val _accountsList = Transformations.map(dataSource.getBareAccounts()) { accounts ->
        formatAccounts(accounts)
    }
    val accountsList: LiveData<List<String>>
        get() = _accountsList

    // Items AutoCompleteTextView adapter list.
    private val _itemsList = Transformations.map(dataSource.getBareItems()) { items ->
        formatItems(items)
    }
    val itemsList: LiveData<List<String>>
        get() = _itemsList

    private var _linkedAccount = MutableLiveData<BareAccount>()
    val linkedAccount: LiveData<BareAccount>
        get() = _linkedAccount

    private var _linkedItem = MutableLiveData<BareItem>()
    val linkedItem: LiveData<BareItem>
        get() = _linkedItem

    // Disable the update button on invalid input.
    private var _enableUpdate = MutableLiveData<Boolean>()
    val enableUpdate: LiveData<Boolean>
        get() = _enableUpdate

    private val _navigateToTransactionsList = MutableLiveData<Boolean?>()
    val navigateToTransactionsList: LiveData<Boolean?>
        get() = _navigateToTransactionsList

    init {
        Log.i(TAG, "Init")
    }

    fun onCreateClicked() {
        if (linkedAccount.value == null) {
            Log.i(TAG, "Invalid account_id.")
        } else if (linkedItem.value == null) {
            Log.i(TAG, "Invalid item_id.")
        } else {
            uiScope.launch {
                create()
                _navigateToTransactionsList.value = true
            }
        }
    }

    fun onUpdateClicked() {
        if (linkedAccount.value == null) {
            Log.i(TAG, "Invalid account_id.")
        } else if (linkedItem.value == null) {
            Log.i(TAG, "Invalid item_id.")
        } else {
            uiScope.launch {
                update()
                _navigateToTransactionsList.value = true
            }
        }
    }

    private suspend fun create() {
        withContext(Dispatchers.IO) {
            dataSource.insert(transaction.value!!)
            Log.i(TAG, "Inserted ${transaction.value!!}")
        }
    }

    private suspend fun update() {
        withContext(Dispatchers.IO) {
            dataSource.update(transaction.value!!)
            Log.i(TAG, "Updated ${transaction.value!!}")
        }
    }

    fun onCancelClicked() {
        _navigateToTransactionsList.value = true
    }

    fun doneNavigating() {
        _navigateToTransactionsList.value = null
    }

    fun updateLinkedAccount(string: String) {
        val accountId = string.substring(0, min(string.length, 4)).toLongOrNull()
        // Clear value if input cannot be cast to Long. Else perform database lookup.
        Log.i(TAG, "updateLinkedAccount=$accountId")
        if (accountId == null) {
            _linkedAccount.postValue(null)
        } else {
            uiScope.launch {
                withContext(Dispatchers.IO) {
                    val account = dataSource.getAccount(accountId)
                    _linkedAccount.postValue(account)
                }
            }
        }
    }

    fun updateLinkedItem(string: String) {
        val itemId = string.substring(0, min(string.length, 4)).toLongOrNull()
        // Clear value if input cannot be cast to Long. Else perform database lookup.
        Log.i(TAG, "updateLinkedItem=$itemId")
        if (itemId == null) {
            _linkedItem.postValue(null)
        } else {
            uiScope.launch {
                withContext(Dispatchers.IO) {
                    val item = dataSource.getItem(itemId)
                    _linkedItem.postValue(item)
                }
            }
        }
    }

    // Switch when all ForeignKey inputs are not null.
    fun updateEnableInput() {
//        _enableUpdate.postValue(linkedAccount.value != null && linkedCreator.value != null && linkedItem.value != null)
        _enableUpdate.postValue(linkedAccount.value != null && linkedItem.value != null)
    }

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }
}
