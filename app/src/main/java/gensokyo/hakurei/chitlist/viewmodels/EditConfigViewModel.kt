package gensokyo.hakurei.chitlist.viewmodels

import android.util.Log
import androidx.lifecycle.LiveData
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
        appName.value = Config.appName
        databaseName.value = Config.databaseName
        balanceCap.value = Config.shopLimit
        decimalSeparator.value = Config.decimalSeparator
        decimalOffset.value = Config.decimalOffset
    }

    fun write() {
        Log.i(TAG, "appName=${appName.value}")
        Log.i(TAG, "databaseName=${databaseName.value}")
        Log.i(TAG, "balanceCap=${balanceCap.value}")
        Log.i(TAG, "decimalSeparator=${decimalSeparator.value}")
        Log.i(TAG, "decimalOffset=${decimalOffset.value}")
        Config.properties.apply {
            put("app.name", appName.value)
            put("database.name", databaseName.value)
            put("shop.limit", balanceCap.value.toString())
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
