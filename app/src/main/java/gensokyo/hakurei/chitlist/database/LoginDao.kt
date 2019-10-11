package gensokyo.hakurei.chitlist.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Query

/**
 * The Data Access Object for Login functions.
 */
@Dao
interface LoginDao {
    @Query("SELECT * from accounts_table WHERE account_id = :key")
    fun getAccount(key: Long): LiveData<Account>

    @Query("SELECT * from accounts_table WHERE account_id = :key AND password_hash = :passwordHash AND enabled = :enabled")
    fun getLogin(key: Long, passwordHash: String, enabled: Boolean = true): Account

    @Query("SELECT * FROM accounts_table WHERE enabled = :enabled ORDER BY first_name ASC")
    fun getBareAccounts(enabled: Boolean = true): List<BareAccount>
}
