package gensokyo.hakurei.chitlist.itemslist

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import gensokyo.hakurei.chitlist.database.Item
import gensokyo.hakurei.chitlist.databinding.ListItemItemBinding

private const val TAG = "ItemAdaptor"

class ItemAdaptor(val clickListener: ItemListener) : ListAdapter<Item, ItemAdaptor.ViewHolder>(ItemDiffCallback()) {


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
//        val res = holder.itemView.context.resources

        holder.bind(getItem(position)!!, clickListener)

        holder.binding.idText.text = item.itemId.toString()
        holder.binding.nameText.text = item.name
        holder.binding.priceText.text = item.price.toString()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(parent)
    }

    class ViewHolder private constructor(val binding: ListItemItemBinding) : RecyclerView.ViewHolder(binding.root){

        fun bind(item: Item, clickListener: ItemListener) {
            binding.item = item
            binding.clickListener = clickListener
            binding.executePendingBindings()
        }

        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ListItemItemBinding.inflate(layoutInflater, parent, false)
                return ViewHolder(binding)
            }
        }
    }
}

class ItemDiffCallback : DiffUtil.ItemCallback<Item>() {

    override fun areItemsTheSame(oldItem: Item, newItem: Item): Boolean {
        return oldItem.itemId == newItem.itemId
    }

    override fun areContentsTheSame(oldItem: Item, newItem: Item): Boolean {
        return oldItem == newItem
    }
}

class ItemListener(val clickListener: (itemId: Long) -> Unit) {
    fun onClick(item: Item) = clickListener(item.itemId)
}
