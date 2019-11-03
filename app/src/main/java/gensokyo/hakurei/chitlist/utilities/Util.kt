package gensokyo.hakurei.chitlist.utilities

import android.annotation.SuppressLint
import android.util.Log
import android.view.View
import androidx.databinding.BindingAdapter
import androidx.databinding.InverseMethod
import gensokyo.hakurei.chitlist.R
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
        Log.i(TAG, "removeDecimal called on $value")
        val float = value.toFloatOrNull() ?: return 0

        val formatted: String
        formatted = when (CURRENCY_SEPARATOR_OFFSET) {
            3 -> "%.3f".format(float)
            2 -> "%.2f".format(float)
            1 -> "%.1f".format(float)
            else -> float.toString()
        }
        Log.i(TAG, "formatted=$formatted")

        return formatted.replace(CURRENCY_SEPARATOR_SYMBOL, "").toInt()
    }

    @JvmStatic
    fun addDecimal(value: Int): String {
//        Log.i(TAG, "addDecimal called on $value")
        // Add leading zeroes up to currency separator.
        val string = value.toString().padStart(CURRENCY_SEPARATOR_OFFSET + 1, '0')
        val length = string.length
        // Insert currency separator at offset from right of string.
        return StringBuilder(string).insert(length - CURRENCY_SEPARATOR_OFFSET,
            CURRENCY_SEPARATOR_SYMBOL
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
        return SimpleDateFormat("EEEE MMM-dd-yyyy' Time 'HH:mm:ss.SSS")
            .format(systemTime).toString()
    }

    @JvmStatic
    @SuppressLint("SimpleDateFormat")
    fun convertLongToDateStringShort(systemTime: Long): String {
        return SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
            .format(systemTime).toString()
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

fun getDrawableId(resourceName: String): Int {
    try {
        val res = R.drawable::class.java
        val field = res.getField(resourceName)
        return field.getInt(null)
    } catch (e: Exception) {
        Log.e(TAG, "Failure to get drawable id.", e)
        return -1
    }
}
