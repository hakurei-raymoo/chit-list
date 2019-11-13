package gensokyo.hakurei.chitlist.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import gensokyo.hakurei.chitlist.database.Account
import gensokyo.hakurei.chitlist.database.BareAccountWithHistory
import gensokyo.hakurei.chitlist.databinding.ListItemAccountBinding

private const val TAG = "AccountAdapter"

class AccountAdapter(val clickListener: AccountListener) :
    ListAdapter<BareAccountWithHistory, AccountAdapter.ViewHolder>(AccountDiffCallback()) {

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position)!!, clickListener)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(parent)
    }

    class ViewHolder private constructor(val binding: ListItemAccountBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: BareAccountWithHistory, clickListener: AccountListener) {
            var balance = 0
            item.history.forEach { balance += it.amount }
            binding.account = item
            binding.balance = balance
            binding.clickListener = clickListener
            binding.executePendingBindings()
        }

        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ListItemAccountBinding.inflate(layoutInflater, parent, false)
                return ViewHolder(binding)
            }
        }
    }
}

class AccountDiffCallback : DiffUtil.ItemCallback<BareAccountWithHistory>() {

    override fun areItemsTheSame(oldItem: BareAccountWithHistory, newItem: BareAccountWithHistory): Boolean {
        return oldItem.accountId == newItem.accountId
    }

    override fun areContentsTheSame(oldItem: BareAccountWithHistory, newItem: BareAccountWithHistory): Boolean {
        return oldItem == newItem
    }
}

class AccountListener(val clickListener: (accountId: Long) -> Unit) {
    fun onClick(account: BareAccountWithHistory) = clickListener(account.accountId)
}
