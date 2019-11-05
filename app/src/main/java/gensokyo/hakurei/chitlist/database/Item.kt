package gensokyo.hakurei.chitlist.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "items_table")
data class Item (
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "item_id", index = true)
    var itemId: Long = 0L,

    var name: String = "",

    var price: Int = 0,

    var image: String = "",

    var enabled: Boolean = true
)

data class BareItem(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "item_id", index = true)
    var itemId: Long,

    var name: String = "",

    var price: Int = 0
)
