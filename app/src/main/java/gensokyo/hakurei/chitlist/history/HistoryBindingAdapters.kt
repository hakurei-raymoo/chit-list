package gensokyo.hakurei.chitlist.history

import android.widget.TextView
import androidx.databinding.BindingAdapter
import gensokyo.hakurei.chitlist.convertLongToDateStringShort

@BindingAdapter("timeFormattedShort")
fun TextView.setTimeFormattedShort(time: Long) {
    text = convertLongToDateStringShort(time)
}
