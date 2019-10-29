package gensokyo.hakurei.chitlist.checkout

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import gensokyo.hakurei.chitlist.database.Item
import gensokyo.hakurei.chitlist.databinding.ListItemCheckoutBinding
import gensokyo.hakurei.chitlist.getDrawableId

private const val TAG = "CheckoutAdapter"

class CheckoutAdapter(val clickListener: CheckoutListener) :
    ListAdapter<Item, CheckoutAdapter.ViewHolder>(CheckoutDiffCallback()) {

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position)!!, clickListener)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(parent)
    }

    class ViewHolder private constructor(val binding: ListItemCheckoutBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: Item, clickListener: CheckoutListener) {
            binding.item = item
            binding.clickListener = clickListener
            binding.executePendingBindings()
            // Get the image id and set it.
            val name = item.image
            val id = getDrawableId(name)
            val defaultId = getDrawableId("ic_remove_white_24dp")
            // Setting default image required to prevent incorrect image display on view recycling.
            if (id != -1) binding.image.setImageResource(id) else binding.image.setImageResource(defaultId)
            Log.i(TAG, "id=$id")
        }

        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ListItemCheckoutBinding.inflate(layoutInflater, parent, false)
                return ViewHolder(binding)
            }
        }
    }
}

class CheckoutDiffCallback : DiffUtil.ItemCallback<Item>() {

    override fun areItemsTheSame(oldItem: Item, newItem: Item): Boolean {
        return oldItem.itemId == newItem.itemId
    }

    override fun areContentsTheSame(oldItem: Item, newItem: Item): Boolean {
        return oldItem == newItem
    }
}

class CheckoutListener(val clickListener: (item: Item) -> Unit) {
    fun onClick(item: Item) = clickListener(item)
}
