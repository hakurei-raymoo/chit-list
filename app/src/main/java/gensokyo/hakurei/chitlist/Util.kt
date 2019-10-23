package gensokyo.hakurei.chitlist

import android.annotation.SuppressLint
import android.util.Log
import android.view.View
import androidx.databinding.BindingAdapter
import androidx.databinding.InverseMethod
import java.security.MessageDigest
import java.text.SimpleDateFormat

private const val TAG = "Util"

fun String.hash() : String {
    val digest = MessageDigest.getInstance("SHA-1")
    val bytes = digest.digest(this.toByteArray(Charsets.UTF_8))
    return bytes.fold("", { str, it -> str + "%02x".format(it) })
}

object Converter {
    @JvmStatic
    @InverseMethod("addDecimal")
    fun removeDecimal(value: String): Int {
//        Log.i(TAG, "removeDecimal called on $value")
        val float = value.toFloat()
        val formatted: String
        when (CURRENCY_SEPARATOR_OFFSET) {
            3 -> formatted = "%.3f".format(float)
            2 -> formatted = "%.2f".format(float)
            1 -> formatted = "%.1f".format(float)
            else -> formatted = float.toString()
        }
        return formatted.replace(CURRENCY_SEPARATOR_SYMBOL, "").toInt()
    }
//        var int = 0
//        // Location of separator or -1 if not found.
//        val separatorOffset = value.indexOf(CURRENCY_SEPARATOR_SYMBOL)
//        // Number of zeroes to add.
//        val paddingLength = separatorOffset + CURRENCY_SEPARATOR_OFFSET - value.length
//        // Number of zeroes to add or default if not found.
//        val actualPadding = if (paddingLength < 0) CURRENCY_SEPARATOR_OFFSET else paddingLength
//        when (actualPadding) {
//            0 -> int = value.replace(CURRENCY_SEPARATOR_SYMBOL, "").removeDecimal()
//            1 -> int = (value.replace(CURRENCY_SEPARATOR_SYMBOL, "") + "0").removeDecimal()
//            2 -> int = (value.replace(CURRENCY_SEPARATOR_SYMBOL, "") + "00").removeDecimal()
//            3 -> int = (value.replace(CURRENCY_SEPARATOR_SYMBOL, "") + "000").removeDecimal()
//        }
//        return int

    @JvmStatic
    fun addDecimal(value: Int): String {
//        Log.i(TAG, "addDecimal called on $value")
        // Add leading zeroes up to currency separator.
        val string = value.toString().padStart(CURRENCY_SEPARATOR_OFFSET + 1, '0')
        val length = string.length
        // Insert currency separator at offset from right of string.
        return StringBuilder(string).insert(length - CURRENCY_SEPARATOR_OFFSET, CURRENCY_SEPARATOR_SYMBOL).toString()
    }
}

@BindingAdapter("isGone")
fun bindIsGone(view: View, isGone: Boolean) {
    view.visibility = if (isGone) {
        View.GONE
    } else {
        View.VISIBLE
    }
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
@SuppressLint("SimpleDateFormat")
fun convertLongToDateStringExpanded(systemTime: Long): String {
    return SimpleDateFormat("EEEE MMM-dd-yyyy' Time 'HH:mm:ss.SSS")
        .format(systemTime).toString()
}

@SuppressLint("SimpleDateFormat")
fun convertLongToDateStringShort(systemTime: Long): String {
    return SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss")
        .format(systemTime).toString()
}
