package gensokyo.hakurei.chitlist.utilities

import android.content.Context
import android.util.Log
import java.io.*
import java.util.*

private const val TAG = "Config"

object Config {
    lateinit var file: File
    val properties = Properties()

    private var _appName = "Chit List"
    val appName: String
        get() = _appName

    private var _databaseName = "chit-db"
    val databaseName: String
        get() = _databaseName

    private var _shopLimit = 1000
    val shopLimit: Int
        get() = _shopLimit

    private var _currencySeparator = "."
    val currencySeparator: String
        get() = _currencySeparator

    private var _currencyOffset = 2
    val currencyOffset: Int
        get() = _currencyOffset

    fun init(context: Context) {
        file = File(context.filesDir, "chit_list.properties")
    }

    fun write() {
        properties.apply {
            put("app.name", "Chit List")
            put("database.name", "chit-db")
            put("shop.limit", "5000")
            put("currency.separator", ".")
            put("currency.offset", "2")
        }

        val fileOutputStream = FileOutputStream(file)
        properties.store(fileOutputStream, "Chit List")
    }

    fun read() {
        try {
            val fileInputStream = FileInputStream(file).use {
                properties.load(it)

                _appName = properties.getProperty("app.name")
                Log.i(TAG, "appName=$appName")
                _databaseName = properties.getProperty("database.name")
                Log.i(TAG, "databaseName=$databaseName")
                _shopLimit = properties.getProperty("shop.limit").toInt()
                Log.i(TAG, "shopLimit=$shopLimit")
                _currencySeparator = properties.getProperty("currency.separator")
                Log.i(TAG, "currencySeparator=$currencySeparator")
                _currencyOffset = properties.getProperty("currency.offset").toInt()
                Log.i(TAG, "currencyOffset=$currencyOffset")
            }
        } catch (e: FileNotFoundException) {
            Log.i(TAG, "$e")
        }
    }
}
