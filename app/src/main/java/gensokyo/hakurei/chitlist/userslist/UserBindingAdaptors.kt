package gensokyo.hakurei.chitlist.userslist

import android.widget.TextView
import androidx.databinding.BindingAdapter
import gensokyo.hakurei.chitlist.database.User

@BindingAdapter("userIdFormatted")
fun TextView.setUserIdFormatted(item: User?) {
    item?.let {
        text = "userId=${item.userId.toString()}"
    }
}

@BindingAdapter("firstNameFormatted")
fun TextView.setFirstNameFormatted(item: User?) {
    item?.let {
        text = "firstName=${item.firstName}"
    }
}

@BindingAdapter("lastNameFormatted")
fun TextView.setLastNameFormatted(item: User?) {
    item?.let {
        text = "lastName=${item.lastName}"
    }
}

@BindingAdapter("defAuthHashFormatted")
fun TextView.setDefAuthHashFormatted(item: User?) {
    item?.let {
        text = "defAuthHash=${item.defAuthHash}"
    }
}
