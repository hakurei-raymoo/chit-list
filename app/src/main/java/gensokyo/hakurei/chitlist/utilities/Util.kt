package gensokyo.hakurei.chitlist.utilities

import android.annotation.SuppressLint
import android.util.Log
import android.view.View
import androidx.databinding.BindingAdapter
import androidx.databinding.InverseMethod
import gensokyo.hakurei.chitlist.R
import java.security.MessageDigest
import java.text.DecimalFormat
import java.text.SimpleDateFormat
import kotlin.math.min

private const val TAG = "Util"

fun String.hash() : String {
    val digest = MessageDigest.getInstance("SHA-1")
    val bytes = digest.digest(this.toByteArray(Charsets.UTF_8))
    return bytes.fold("", { str, it -> str + "%02x".format(it) })
}

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
    fun toItemId(string: String): Long? {
        val value = string.substring(0, min(string.length, 4)).toLongOrNull()
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
        Log.i(TAG, "removeDecimal called on $string -> $formatted")
        // TODO: Fix crash when using comma separator.
        return formatted.replace(Config.DECIMAL_SEPARATOR, "").toInt()
    }

    @JvmStatic
    fun addDecimal(value: Int): String {
        // Add leading zeroes up to currency separator.
        val string = value.toString().padStart(Config.DECIMAL_OFFSET + 1, '0')
//        Log.i(TAG, "addDecimal called on $value -> $string")

        val length = string.length
        // Insert currency separator at offset from right of string.
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
