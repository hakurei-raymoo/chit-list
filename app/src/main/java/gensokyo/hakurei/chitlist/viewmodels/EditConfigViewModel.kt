package gensokyo.hakurei.chitlist.viewmodels

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import gensokyo.hakurei.chitlist.utilities.Config

private const val TAG = "EditConfigViewModel"

class EditConfigViewModel : ViewModel() {
    val appSubtitle = MutableLiveData<String>("Chit List")
    val databaseName = MutableLiveData<String>("chit-db")
    val balanceCap = MutableLiveData(1000)
    val decimalSeparator = MutableLiveData<String>(".")
    val decimalOffset = MutableLiveData(2)

    init {
        Config.read()
        appSubtitle.value = Config.APP_SUBTITLE
        databaseName.value = Config.DATABASE_NAME
        balanceCap.value = Config.BALANCE_CAP
        decimalSeparator.value = Config.DECIMAL_SEPARATOR
        decimalOffset.value = Config.DECIMAL_OFFSET
        Log.i(TAG, "Init")
    }

    fun write() {
        Log.i(TAG, "appSubtitle=${appSubtitle.value}")
        Log.i(TAG, "databaseName=${databaseName.value}")
        Log.i(TAG, "balanceCap=${balanceCap.value}")
        Log.i(TAG, "decimalSeparator=${decimalSeparator.value}")
        Log.i(TAG, "decimalOffset=${decimalOffset.value}")
        Config.properties.apply {
            setProperty("app.subtitle", appSubtitle.value)
            setProperty("database.name", databaseName.value)
            setProperty("balance.cap", balanceCap.value.toString())
            setProperty("decimal.separator", decimalSeparator.value)
            setProperty("decimal.offset", decimalOffset.value.toString())
        }
        Config.store()
    }

    override fun onCleared() {
        Log.i(TAG, "Cleared")
        super.onCleared()
    }
}
