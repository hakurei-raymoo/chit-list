package gensokyo.hakurei.chitlist.utilities

import android.util.Log
import android.view.View
import androidx.databinding.BindingAdapter
import gensokyo.hakurei.chitlist.R
import java.security.MessageDigest

private const val TAG = "Util"

fun String.hash() : String {
    val digest = MessageDigest.getInstance("SHA-1")
    val bytes = digest.digest(this.toByteArray(Charsets.UTF_8))
    return bytes.fold("", { str, it -> str + "%02x".format(it) })
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
