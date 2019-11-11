package gensokyo.hakurei.chitlist.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
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
