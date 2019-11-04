package gensokyo.hakurei.chitlist.transactiondetail

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import gensokyo.hakurei.chitlist.database.*
import kotlinx.coroutines.*

private const val TAG = "TXDetailViewModel"

class TransactionDetailViewModel(
    private val creatorId: Long,
    private val transactionKey: Long,
    private val dataSource: TransactionDao
) : ViewModel() {

    private val viewModelJob = Job()

    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)

    // Check transactionKey to either get Transaction from database or insert a new one.
    private var _transaction =
        if (transactionKey == -1L) {
            MutableLiveData<Transaction>(Transaction(accountId = creatorId, creatorId = creatorId, itemId = 1L))
        } else {
            dataSource.getTransaction(transactionKey)
        }
    val transaction: LiveData<Transaction>
        get() = _transaction

    val accounts = dataSource.getBareAccounts()
    val items = dataSource.getBareItems()

    private var _linkedAccount = MutableLiveData<BareAccount>()
    val linkedAccount: LiveData<BareAccount>
        get() = _linkedAccount

    private var _linkedCreator = MutableLiveData<BareAccount>()
    val linkedCreator: LiveData<BareAccount>
        get() = _linkedCreator

    private var _linkedItem = MutableLiveData<BareItem>()
    val linkedItem: LiveData<BareItem>
        get() = _linkedItem

    // Enable/disable the submit button.
    private var _enableInput = MutableLiveData<Boolean>()
    val enableInput: LiveData<Boolean>
        get() = _enableInput

    private val _navigateToTransactionsList = MutableLiveData<Boolean?>()
    val navigateToTransactionsList: LiveData<Boolean?>
        get() = _navigateToTransactionsList

    init {
        Log.i(TAG, "Init")
    }

    fun onUpdateClicked() {
        if (linkedAccount.value == null || linkedCreator.value == null || linkedItem.value == null) {
            Log.i(TAG, "Invalid input.")
        } else {
            uiScope.launch {
                update()
                _navigateToTransactionsList.value = true
            }
        }
    }

    private suspend fun update() {
        withContext(Dispatchers.IO) {
            if (transactionKey == -1L) {
                dataSource.insert(transaction.value!!)
                Log.i(TAG, "Inserted ${transaction.value!!}")
            } else {
                dataSource.update(transaction.value!!)
                Log.i(TAG, "Updated ${transaction.value!!}")
            }
        }
    }

    fun onBackClicked() {
        _navigateToTransactionsList.value = true
    }

    fun doneNavigating() {
        _navigateToTransactionsList.value = null
    }

    fun updateAccountEditHelperText(accountId: Long?) {
        // Clear value if input cannot be cast to Long. Else perform database lookup.
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

    fun updateCreatorEditHelperText(creatorId: Long?) {
        // Clear value if input cannot be cast to Long. Else perform database lookup.
        if (creatorId == null) {
            _linkedCreator.postValue(null)
        } else {
            uiScope.launch {
                withContext(Dispatchers.IO) {
                    _linkedCreator.postValue(dataSource.getAccount(creatorId))
                }
            }
        }
    }

    fun updateItemEditHelperText(itemId: Long?) {
        // Clear value if input cannot be cast to Long. Else perform database lookup.
        if (itemId == null) {
            _linkedItem.postValue(null)
        } else {
            uiScope.launch {
                withContext(Dispatchers.IO) {
                    _linkedItem.postValue(dataSource.getItem(itemId))
                }
            }
        }
    }

    // Switch when all ForeignKey inputs are not null.
    fun updateEnableInput() {
        _enableInput.postValue(linkedAccount.value != null && linkedCreator.value != null && linkedItem.value != null)
    }

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }
}
