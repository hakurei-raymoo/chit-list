package gensokyo.hakurei.chitlist.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "chit_users_table")
data class User(
    @PrimaryKey(autoGenerate = true)
    var userId: Long = 0L,

    @ColumnInfo(name = "first_name")
    var firstName: String = "",

    @ColumnInfo(name = "last_name")
    var lastName: String = "",

    @ColumnInfo(name = "def_auth_hash")
    var defAuthHash: String = "",

    @ColumnInfo(name = "bio_auth_hash")
    var bioAuthHash: String = "",

    @ColumnInfo(name = "two_FA_hash")
    var twoFAHash: String = ""
)