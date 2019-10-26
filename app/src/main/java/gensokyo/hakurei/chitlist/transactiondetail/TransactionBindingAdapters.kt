package gensokyo.hakurei.chitlist.transactiondetail

import android.widget.TextView
import androidx.databinding.BindingAdapter
import gensokyo.hakurei.chitlist.convertLongToDateStringExpanded

@BindingAdapter("timeFormattedExpanded")
fun TextView.setTimeFormattedExpanded(time: Long) {
    text = convertLongToDateStringExpanded(time)
}
