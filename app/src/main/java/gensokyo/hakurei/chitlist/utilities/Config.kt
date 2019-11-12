package gensokyo.hakurei.chitlist.utilities

import android.content.Context
import android.util.Log
import java.io.*
import java.util.*

private const val TAG = "Config"

private const val DEFAULT_APP_NAME = "Chit List"
private const val DEFAULT_DATABASE_NAME = "chit-db"
private const val DEFAULT_BALANCE_CAP = 10000
private const val DEFAULT_DECIMAL_SEPARATOR = "."
private const val DEFAULT_DECIMAL_OFFSET = 2

object Config {
    lateinit var file: File
    val properties = Properties()

    var APP_NAME = DEFAULT_APP_NAME
    var DATABASE_NAME = DEFAULT_DATABASE_NAME
    var BALANCE_CAP = DEFAULT_BALANCE_CAP
    var DECIMAL_SEPARATOR = DEFAULT_DECIMAL_SEPARATOR
    var DECIMAL_OFFSET = DEFAULT_DECIMAL_OFFSET

    fun init(context: Context) {
        file = File(context.filesDir, "chit_list.properties")
    }

    private fun applyDefault() {
        properties.apply {
            put("app.name", DEFAULT_APP_NAME)
            put("database.name", DEFAULT_DATABASE_NAME)
            put("balance.cap", DEFAULT_BALANCE_CAP)
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

                APP_NAME = properties.getProperty("app.name") ?: DEFAULT_APP_NAME
                DATABASE_NAME = properties.getProperty("database.name") ?: DEFAULT_DATABASE_NAME
                BALANCE_CAP = properties.getProperty("balance.cap").toIntOrNull() ?: DEFAULT_BALANCE_CAP
                DECIMAL_SEPARATOR = properties.getProperty("decimal.separator") ?: DEFAULT_DECIMAL_SEPARATOR
                DECIMAL_OFFSET = properties.getProperty("decimal.offset").toIntOrNull() ?: DEFAULT_DECIMAL_OFFSET

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
