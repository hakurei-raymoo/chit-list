package gensokyo.hakurei.chitlist.transactionslist

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import gensokyo.hakurei.chitlist.database.Transaction
import gensokyo.hakurei.chitlist.databinding.ListItemTransactionBinding

private const val TAG = "TXAdaptor"

class TransactionAdaptor(val clickListener: TransactionListener) :
    ListAdapter<Transaction, TransactionAdaptor.ViewHolder>(TransactionDiffCallback()) {

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position)!!, clickListener)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(parent)
    }

    class ViewHolder private constructor(val binding: ListItemTransactionBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(transaction: Transaction, clickListener: TransactionListener) {
            binding.transaction = transaction
            binding.clickListener = clickListener
            binding.executePendingBindings()
        }

        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ListItemTransactionBinding.inflate(layoutInflater, parent, false)
                return ViewHolder(binding)
            }
        }
    }
}

class TransactionDiffCallback : DiffUtil.ItemCallback<Transaction>() {

    override fun areItemsTheSame(oldItem: Transaction, newItem: Transaction): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Transaction, newItem: Transaction): Boolean {
        return oldItem == newItem
    }
}

class TransactionListener(val clickListener: (transactionId: Long) -> Unit) {
    fun onClick(transaction: Transaction) = clickListener(transaction.id)
}
