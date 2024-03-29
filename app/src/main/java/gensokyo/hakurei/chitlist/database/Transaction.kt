package gensokyo.hakurei.chitlist.database

import androidx.room.*

@Entity(
    tableName = "transactions_table",
    foreignKeys = arrayOf(
        ForeignKey(
            entity = Account::class,
            parentColumns = arrayOf("account_id"),
            childColumns = arrayOf("account_id")
        ),
        ForeignKey(
            entity = Account::class,
            parentColumns = arrayOf("account_id"),
            childColumns = arrayOf("creator_id")
        ),
        ForeignKey(
            entity = Item::class,
            parentColumns = arrayOf("item_id"),
            childColumns = arrayOf("item_id")
        )
    )
)
data class Transaction(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "transaction_id")
    var transactionId: Long = 0L,

    val time: Long = System.currentTimeMillis(),

    @ColumnInfo(name = "account_id", index = true)
    var accountId: Long,

    @ColumnInfo(name = "creator_id", index = true)
    var creatorId: Long,

    @ColumnInfo(name = "item_id", index = true)
    var itemId: Long,

    var amount: Int = 0,

    var comments: String = "",

    val type: Boolean = false
)

data class TransactionWithChildren(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "transaction_id")
    var transactionId: Long,

    val time: Long,

    @Embedded(prefix = "a_")
    val account: BareAccount,

    @Embedded(prefix = "c_")
    val creator: BareAccount,

    @Embedded
    val item: BareItem,

    var amount: Int,

    var comments: String,

    val type: Boolean
)
