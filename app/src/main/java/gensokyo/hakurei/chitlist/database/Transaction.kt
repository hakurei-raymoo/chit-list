package gensokyo.hakurei.chitlist.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(tableName = "transactions_table",
    primaryKeys = arrayOf("account_id","item_id"),
    foreignKeys = arrayOf(
        ForeignKey(
            entity = Account::class,
            parentColumns = arrayOf("id"),
            childColumns = arrayOf("account_id")),
        ForeignKey(
            entity = Item::class,
            parentColumns = arrayOf("id"),
            childColumns = arrayOf("item_id"))
    )
)
data class Transaction (
    @ColumnInfo(name = "id")
    var id: Long = 0L,

    @ColumnInfo(name = "time")
    val time: Long = System.currentTimeMillis(),

    @ColumnInfo(name = "account_id")
    var accountId: Long = 0L,

    @ColumnInfo(name = "item_id")
    var itemId: Long = 0L
)
