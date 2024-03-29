package gensokyo.hakurei.chitlist.database

import androidx.room.*
import gensokyo.hakurei.chitlist.utilities.hash

@Entity(tableName = "accounts_table")
data class Account(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "account_id", index = true)
    var accountId: Long = 0L,

    @ColumnInfo(name = "first_name")
    var firstName: String = "",

    @ColumnInfo(name = "last_name")
    var lastName: String = "",

    @ColumnInfo(name = "location")
    var location: String = "",

    @ColumnInfo(name = "contact_number")
    var contactNumber: String = "",

    @ColumnInfo(name = "email_address")
    var emailAddress: String = "",

    @ColumnInfo(name = "password_hash")
    var passwordHash: String = "".hash(),

    @ColumnInfo(name = "alt_auth")
    var altAuth: String = "",

    @ColumnInfo(name = "bio_auth")
    var bioAuth: String = "",

    var admin: Boolean = false,

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

data class BareAccountWithHistory(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "account_id", index = true)
    var accountId: Long,

    @ColumnInfo(name = "first_name")
    var firstName: String,

    @ColumnInfo(name = "last_name")
    var lastName: String,

    var admin: Boolean = false,

    var enabled: Boolean = true,

    @Relation(parentColumn = "account_id", entityColumn = "account_id")
    var history: List<Transaction>
)

data class AccountWithHistory(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "account_id", index = true)
    var accountId: Long,

    @ColumnInfo(name = "first_name")
    var firstName: String,

    @ColumnInfo(name = "last_name")
    var lastName: String,

    @ColumnInfo(name = "location")
    var location: String = "",

    @ColumnInfo(name = "contact_number")
    var contactNumber: String = "",

    @ColumnInfo(name = "email_address")
    var emailAddress: String = "",

    var admin: Boolean = false,

    var enabled: Boolean = true,

    @Relation(parentColumn = "account_id", entityColumn = "account_id")
    var history: List<Transaction>
)
