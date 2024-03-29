package gensokyo.hakurei.chitlist.editconfig

import android.net.Uri
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import gensokyo.hakurei.chitlist.utilities.Config

private const val TAG = "EditConfigViewModel"

class EditConfigViewModel : ViewModel() {
    val appSubtitle = MutableLiveData<String>("Chit List")
    val appLogo = MutableLiveData<String>("@android:drawable/sym_def_app_icon")
    val databaseName = MutableLiveData<String>("chit-db")
    val balanceCap = MutableLiveData(1000)
    val decimalSeparator = MutableLiveData<String>(".")
    val decimalOffset = MutableLiveData(2)

    init {
        Config.read()
        appSubtitle.value = Config.APP_SUBTITLE
        appLogo.value = Config.APP_LOGO
        databaseName.value = Config.DATABASE_NAME
        balanceCap.value = Config.BALANCE_CAP
        decimalSeparator.value = Config.DECIMAL_SEPARATOR
        decimalOffset.value = Config.DECIMAL_OFFSET
        Log.i(TAG, "Init")
    }

    fun changeAppLogo(uri: Uri) {
        appLogo.value = uri.toString()
    }

    fun write() {
        Log.i(TAG, "appSubtitle=${appSubtitle.value}")
        Log.i(TAG, "appLogo=${appLogo.value}")
        Log.i(TAG, "databaseName=${databaseName.value}")
        Log.i(TAG, "balanceCap=${balanceCap.value}")
        Log.i(TAG, "decimalSeparator=${decimalSeparator.value}")
        Log.i(TAG, "decimalOffset=${decimalOffset.value}")
        Config.properties.apply {
            setProperty("app.subtitle", appSubtitle.value)
            setProperty("app.logo", appLogo.value)
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
