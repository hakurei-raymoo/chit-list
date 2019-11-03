package gensokyo.hakurei.chitlist.adapters

import android.content.res.Resources
import android.text.SpannableStringBuilder
import android.text.Spanned
import android.text.style.ForegroundColorSpan
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import gensokyo.hakurei.chitlist.utilities.Converter
import gensokyo.hakurei.chitlist.R
import gensokyo.hakurei.chitlist.database.TransactionWithChildren
import gensokyo.hakurei.chitlist.databinding.ListItemHistoryBinding

private const val TAG = "HistoryAdapter"

class HistoryAdapter(val clickListener: HistoryListener) :
    ListAdapter<TransactionWithChildren, HistoryAdapter.ViewHolder>(
        HistoryDiffCallback()
    ) {

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position)!!, clickListener)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(parent)
    }

    class ViewHolder private constructor(val binding: ListItemHistoryBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: TransactionWithChildren, clickListener: HistoryListener) {
            val res = itemView.context.resources
            binding.transaction = item
            binding.clickListener = clickListener
            binding.executePendingBindings()
//            binding.historyText.text = convertTransactionToString(item, res)
        }

        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ListItemHistoryBinding.inflate(layoutInflater, parent, false)
                return ViewHolder(binding)
            }
        }
    }
}

class HistoryDiffCallback : DiffUtil.ItemCallback<TransactionWithChildren>() {

    override fun areItemsTheSame(oldItem: TransactionWithChildren, newItem: TransactionWithChildren): Boolean {
        return oldItem.transactionId == newItem.transactionId
    }

    override fun areContentsTheSame(oldItem: TransactionWithChildren, newItem: TransactionWithChildren): Boolean {
        return oldItem == newItem
    }
}

class HistoryListener(val clickListener: (transactionId: Long) -> Unit) {
    fun onClick(transaction: TransactionWithChildren) = clickListener(transaction.transactionId)
}

private fun convertTransactionToString(item: TransactionWithChildren, resources: Resources): Spanned {
    SpannableStringBuilder().apply {
        append(item.item.name)
        append(" : $")
        append(Converter.addDecimal(item.amount))
        append("\n")
        append(resources.getString(R.string.time_equals))
        append(Converter.convertLongToDateStringShort(item.time))
        append("\n")
        append(resources.getString(R.string.creator_equals))
        if (item.creatorId == item.account.accountId) {
            append(resources.getString(R.string.you))
        } else {
            append("${item.creatorId}", ForegroundColorSpan(resources.getColor(R.color.colorAccent)), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        }
        append("\n")
        append(resources.getString(R.string.comments_equals))
        if (item.comments == "") {
            append(resources.getString(R.string.none))
        } else {
            append(item.comments)
        }
        return this
    }
}
