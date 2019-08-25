package gensokyo.hakurei.chitlist.userslist

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import gensokyo.hakurei.chitlist.database.User
import gensokyo.hakurei.chitlist.databinding.ListItemUserBinding

private const val TAG = "UserAdaptor"

class UserAdaptor(val clickListener: UserListener) : ListAdapter<User, UserAdaptor.ViewHolder>(UserDiffCallback()) {


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
//        val res = holder.itemView.context.resources

        holder.bind(getItem(position)!!, clickListener)

        holder.binding.idText.text = item.userId.toString()
        holder.binding.firstNameText.text = item.firstName
        holder.binding.lastNameText.text = item.lastName
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(parent)
    }

    class ViewHolder private constructor(val binding: ListItemUserBinding) : RecyclerView.ViewHolder(binding.root){

        fun bind(item: User, clickListener: UserListener) {
            binding.user = item
            binding.clickListener = clickListener
            binding.executePendingBindings()
        }

        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ListItemUserBinding.inflate(layoutInflater, parent, false)
                return ViewHolder(binding)
            }
        }
    }
}

class UserDiffCallback : DiffUtil.ItemCallback<User>() {

    override fun areItemsTheSame(oldItem: User, newItem: User): Boolean {
        return oldItem.userId == newItem.userId
    }

    override fun areContentsTheSame(oldItem: User, newItem: User): Boolean {
        return oldItem == newItem
    }
}

class UserListener(val clickListener: (userId: Long) -> Unit) {
    fun onClick(user: User) = clickListener(user.userId)
}
