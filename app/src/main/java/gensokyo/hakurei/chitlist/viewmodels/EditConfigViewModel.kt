package gensokyo.hakurei.chitlist.viewmodels

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import gensokyo.hakurei.chitlist.utilities.Config

private const val TAG = "EditConfigViewModel"

class EditConfigViewModel : ViewModel() {
    val appName = MutableLiveData<String>("Chit List")
    val databaseName = MutableLiveData<String>("chit-db")
    val balanceCap = MutableLiveData(1000)
    val decimalSeparator = MutableLiveData<String>(".")
    val decimalOffset = MutableLiveData(2)

    init {
        Config.read()
        appName.value = Config.APP_NAME
        databaseName.value = Config.DATABASE_NAME
        balanceCap.value = Config.BALANCE_CAP
        decimalSeparator.value = Config.DECIMAL_SEPARATOR
        decimalOffset.value = Config.DECIMAL_OFFSET
    }

    fun write() {
        Log.i(TAG, "APP_NAME=${appName.value}")
        Log.i(TAG, "DATABASE_NAME=${databaseName.value}")
        Log.i(TAG, "BALANCE_CAP=${balanceCap.value}")
        Log.i(TAG, "DECIMAL_SEPARATOR=${decimalSeparator.value}")
        Log.i(TAG, "DECIMAL_OFFSET=${decimalOffset.value}")
        Config.properties.apply {
            put("app.name", appName.value)
            put("database.name", databaseName.value)
            put("balance.cap", balanceCap.value.toString())
            put("decimal.separator", decimalSeparator.value)
            put("decimal.offset", decimalOffset.value.toString())
        }
        Config.write()
    }

    override fun onCleared() {
        Log.i(TAG, "Cleared")
        super.onCleared()
    }
}
