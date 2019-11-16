package gensokyo.hakurei.chitlist.transactionslist

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Filter
import androidx.recyclerview.widget.RecyclerView
import gensokyo.hakurei.chitlist.databinding.ListItemTransactionBinding
import android.util.Log
import gensokyo.hakurei.chitlist.database.TransactionWithChildren

private const val TAG = "TXAdapter"

class TransactionAdapter(val clickListener: TransactionListener): RecyclerView.Adapter<TransactionAdapter.ViewHolder>() {
    var data =  listOf<TransactionWithChildren>()
    var filteredData =  listOf<TransactionWithChildren>()

    override fun getItemCount() = filteredData.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(filteredData[position], clickListener)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(
            parent
        )
    }

    fun getFilter(): Filter {

        return object : Filter() {
            override fun performFiltering(charSequence: CharSequence): FilterResults {
                val accountName = charSequence.toString()

                // Match on names and add to filtered list.
                Log.i(TAG, "Searching for: $accountName")
                val filteredList = mutableListOf<TransactionWithChildren>()

                data.forEach {
                    val fullName = it.account.firstName + " " + it.account.lastName
                    if (fullName.contains(accountName, true)) {
//                        Log.i(TAG, "Added: $it")
                        filteredList.add(it)
                    }
                }
                filteredData = filteredList

                val filterResults = FilterResults()
                filterResults.values = filteredData
                return filterResults
            }

            @Suppress("unchecked_cast")
            override fun publishResults(charSequence: CharSequence, filterResults: FilterResults) {
                filteredData = filterResults.values as MutableList<TransactionWithChildren>
                notifyDataSetChanged()
            }
        }
    }

    class ViewHolder private constructor(val binding: ListItemTransactionBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(transaction: TransactionWithChildren, clickListener: TransactionListener) {
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

class TransactionListener(val clickListener: (transactionId: Long) -> Unit) {
    fun onClick(transaction: TransactionWithChildren) = clickListener(transaction.transactionId)
}
