package gensokyo.hakurei.chitlist.utilities

import android.content.Context
import android.util.Log
import java.io.*
import java.util.*

private const val TAG = "Config"

private const val DEFAULT_APP_NAME = "Chit List"
private const val DEFAULT_DATABASE_NAME = "chit-db"
private const val DEFAULT_SHOP_LIMIT = 10000
private const val DEFAULT_DECIMAL_SEPARATOR = "."
private const val DEFAULT_DECIMAL_OFFSET = 2

object Config {
    lateinit var file: File
    val properties = Properties()

    private var _appName = DEFAULT_APP_NAME
    val appName: String
        get() = _appName

    private var _databaseName = DEFAULT_DATABASE_NAME
    val databaseName: String
        get() = _databaseName

    private var _shopLimit = DEFAULT_SHOP_LIMIT
    val shopLimit: Int
        get() = _shopLimit

    private var _decimalSeparator = DEFAULT_DECIMAL_SEPARATOR
    val decimalSeparator: String
        get() = _decimalSeparator

    private var _decimalOffset = DEFAULT_DECIMAL_OFFSET
    val decimalOffset: Int
        get() = _decimalOffset

    fun init(context: Context) {
        file = File(context.filesDir, "chit_list.properties")
    }

    private fun applyDefault() {
        properties.apply {
            put("app.name", DEFAULT_APP_NAME)
            put("database.name", DEFAULT_DATABASE_NAME)
            put("shop.limit", DEFAULT_SHOP_LIMIT)
            put("decimal.separator", DEFAULT_DECIMAL_SEPARATOR)
            put("decimal.offset", DEFAULT_DECIMAL_OFFSET)
        }
    }

    fun write() {
        val fileOutputStream = FileOutputStream(file)
        properties.store(fileOutputStream, "Chit List")

        Log.i(TAG, "Stored to $fileOutputStream")
        properties.forEach { (k, v) ->
            Log.i(TAG, "$k=$v")
        }
    }

    fun read() {
        try {
            val fileInputStream = FileInputStream(file).use {
                properties.load(it)

                _appName = properties.getProperty("app.name") ?: DEFAULT_APP_NAME
                _databaseName = properties.getProperty("database.name") ?: DEFAULT_DATABASE_NAME
                _shopLimit = properties.getProperty("shop.limit").toIntOrNull() ?: DEFAULT_SHOP_LIMIT
                _decimalSeparator = properties.getProperty("decimal.separator") ?: DEFAULT_DECIMAL_SEPARATOR
                _decimalOffset = properties.getProperty("decimal.offset").toIntOrNull() ?: DEFAULT_DECIMAL_OFFSET

                Log.i(TAG, "Loaded from $it")
                properties.forEach { (k, v) ->
                    Log.i(TAG, "$k=$v")
                }
            }
        } catch (e: FileNotFoundException) {
            Log.i(TAG, "$e")
            applyDefault()
        }
    }
}
