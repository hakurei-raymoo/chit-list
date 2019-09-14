package gensokyo.hakurei.chitlist.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(tableName = "accounts_table")
data class Account(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "account_id", index = true)
    var accountId: Long = 0L,

    @ColumnInfo(name = "first_name")
    var firstName: String = "",

    @ColumnInfo(name = "last_name")
    var lastName: String = "",

    @ColumnInfo(name = "password_hash")
    var passwordHash: String = "",

    @ColumnInfo(name = "alt_auth")
    var altAuth: String = "",

    @ColumnInfo(name = "bio_auth")
    var bioAuth: String = "",

    var enabled: Boolean = true
)

data class BareAccount(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "account_id", index = true)
    var accountId: Long,

    @ColumnInfo(name = "first_name")
    var firstName: String,

    @ColumnInfo(name = "last_name")
    var lastName: String
)
