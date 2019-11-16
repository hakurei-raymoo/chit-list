package gensokyo.hakurei.chitlist.utilities

import android.annotation.SuppressLint
import android.util.Log
import androidx.databinding.InverseMethod
import java.text.DecimalFormat
import java.text.SimpleDateFormat
import kotlin.math.min

private const val TAG = "Converter"

object Converter {
    @JvmStatic
    @InverseMethod("toAccountName")
    fun toAccountId(string: String): Long {
        val value = string.substring(0, min(string.length, 4)).toLongOrNull() ?: -1
        Log.i(TAG, "toAccountId called on $string -> $value")
        return value
    }

    @JvmStatic
    fun toAccountName(value: Long): String {
        val string = String.format("%04d", value)
        Log.i(TAG, "toAccountName called on $value -> $string")
        return string
    }

    @JvmStatic
    @InverseMethod("toItemName")
    fun toItemId(string: String): Long {
        val value = string.substring(0, min(string.length, 4)).toLongOrNull() ?: -1
        Log.i(TAG, "toItemId called on $string -> $value")
        return value
    }

    @JvmStatic
    fun toItemName(value: Long?): String {
        val string = String.format("%04d", value)
        Log.i(TAG, "toItemName called on $value -> $string")
        return string
    }

    @JvmStatic
    @InverseMethod("addDecimal")
    fun removeDecimal(string: String): Int {
        val float = string.toFloatOrNull() ?: return 0

        val formatted: String
        formatted = when (Config.DECIMAL_OFFSET) {
            3 -> DecimalFormat("#.000").format(float)
            2 -> DecimalFormat("#.00").format(float)
            1 -> DecimalFormat("#.0").format(float)
            else -> float.toString()
        }

        // TODO: Fix behaviour when using comma separators.
        Log.i(TAG, "removeDecimal called on $string -> $formatted")
        return formatted.replace(Config.DECIMAL_SEPARATOR, "").toInt()
    }

    @JvmStatic
    fun addDecimal(value: Int): String {
        // Add leading zeroes up to currency separator.
        val string = value.toString().padStart(Config.DECIMAL_OFFSET + 1, '0')
        Log.i(TAG, "addDecimal called on $value -> $string")

        // Insert currency separator at offset from right of string.
        val length = string.length
        return StringBuilder(string).insert(length - Config.DECIMAL_OFFSET,
            Config.DECIMAL_SEPARATOR
        ).toString()
    }

    /**
     * Take the Long milliseconds returned by the system and stored in Room,
     * and convert it to a nicely formatted string for display.
     *
     * EEEE - Display the long letter version of the weekday
     * MMM - Display the letter abbreviation of the month
     * dd-yyyy - day in month and full year numerically
     * HH:mm - Hours and minutes in 24hr format
     */
    @JvmStatic
    @SuppressLint("SimpleDateFormat")
    fun convertLongToDateStringExpanded(systemTime: Long): String {
        return SimpleDateFormat("EEEE MMM dd yyyy' Time 'HH:mm:ss.SSS")
            .format(systemTime).toString()
    }

    @JvmStatic
    @SuppressLint("SimpleDateFormat")
    fun convertLongToDateStringShort(systemTime: Long): String {
        return SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
            .format(systemTime).toString()
    }
}
