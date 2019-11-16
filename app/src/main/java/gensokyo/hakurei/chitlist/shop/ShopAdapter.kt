package gensokyo.hakurei.chitlist.shop

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import gensokyo.hakurei.chitlist.database.Item
import gensokyo.hakurei.chitlist.databinding.ListItemShopBinding
import gensokyo.hakurei.chitlist.utilities.getDrawableId

private const val TAG = "ShopAdapter"

class ShopAdapter(val clickListener: ShopListener) :
    ListAdapter<Item, ShopAdapter.ViewHolder>(ShopDiffCallback()) {

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position)!!, clickListener)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(parent)
    }

    class ViewHolder private constructor(val binding: ListItemShopBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: Item, clickListener: ShopListener) {
            binding.item = item
            binding.clickListener = clickListener
            binding.executePendingBindings()
            // Get the image id and set it.
            val name = item.image
            val id = getDrawableId(name)
            val defaultId = getDrawableId("ic_add_white_24dp")
            // Setting default image required to prevent incorrect image display on view recycling.
            if (id != -1) binding.image.setImageResource(id) else binding.image.setImageResource(defaultId)
//            Log.i(TAG, "id=$id")
        }

        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ListItemShopBinding.inflate(layoutInflater, parent, false)
                return ViewHolder(binding)
            }
        }
    }
}

class ShopDiffCallback : DiffUtil.ItemCallback<Item>() {

    override fun areItemsTheSame(oldItem: Item, newItem: Item): Boolean {
        return oldItem.itemId == newItem.itemId
    }

    override fun areContentsTheSame(oldItem: Item, newItem: Item): Boolean {
        return oldItem == newItem
    }
}

class ShopListener(val clickListener: (item: Item) -> Unit) {
    fun onClick(item: Item) = clickListener(item)
}
