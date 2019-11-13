package gensokyo.hakurei.chitlist.utilities

import android.content.Context
import android.util.Log
import java.io.*
import java.util.*

private const val TAG = "Config"

private const val PROPERTIES_FILE = "chit_list.properties"

private const val DEFAULT_APP_SUBTITLE = ""
private const val DEFAULT_DATABASE_NAME = "chit-db"
private const val DEFAULT_BALANCE_CAP = "10000"
private const val DEFAULT_DECIMAL_SEPARATOR = "."
private const val DEFAULT_DECIMAL_OFFSET = "2"

object Config {
    lateinit var properties: Properties
    lateinit var file: File

    var APP_SUBTITLE = DEFAULT_APP_SUBTITLE
    var DATABASE_NAME = DEFAULT_DATABASE_NAME
    var BALANCE_CAP = DEFAULT_BALANCE_CAP.toInt()
    var DECIMAL_SEPARATOR = DEFAULT_DECIMAL_SEPARATOR
    var DECIMAL_OFFSET = DEFAULT_DECIMAL_OFFSET.toInt()

    fun init(context: Context) {
        val defaults = Properties()
        defaults.apply {
            setProperty("app.subtitle", DEFAULT_APP_SUBTITLE)
            setProperty("database.name", DEFAULT_DATABASE_NAME)
            setProperty("balance.cap", DEFAULT_BALANCE_CAP)
            setProperty("decimal.separator", DEFAULT_DECIMAL_SEPARATOR)
            setProperty("decimal.offset", DEFAULT_DECIMAL_OFFSET)
        }
        properties = Properties(defaults)
        file = File(context.filesDir, PROPERTIES_FILE)
        if (file.exists()) {
            read()
        } else {
            store()
        }
    }

    fun store() {
        FileOutputStream(file).use {
            properties.store(it, "Chit List")

            Log.i(TAG, "Stored to $file")
            properties.forEach { (k, v) ->
                Log.i(TAG, "$k=$v")
            }
        }
    }

    fun read() {
        FileInputStream(file).use {
            properties.load(it)

            APP_SUBTITLE = properties.getProperty("app.subtitle", DEFAULT_APP_SUBTITLE)
            DATABASE_NAME = properties.getProperty("database.name", DEFAULT_DATABASE_NAME)
            BALANCE_CAP = properties.getProperty("balance.cap", DEFAULT_BALANCE_CAP).toInt()
            DECIMAL_SEPARATOR = properties.getProperty("decimal.separator", DEFAULT_DECIMAL_SEPARATOR)
            DECIMAL_OFFSET = properties.getProperty("decimal.offset", DEFAULT_DECIMAL_OFFSET).toInt()

            Log.i(TAG, "Loaded from $file")
            properties.forEach { (k, v) ->
                Log.i(TAG, "$k=$v")
            }
        }
    }
}
