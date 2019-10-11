package gensokyo.hakurei.chitlist.transactiondetail

import android.widget.TextView
import androidx.databinding.BindingAdapter
import gensokyo.hakurei.chitlist.convertLongToDateString

@BindingAdapter("timeFormatted")
fun TextView.setTimeFormatted(time: Long) {
    text = convertLongToDateString(time)
}
