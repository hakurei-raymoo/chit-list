package gensokyo.hakurei.chitlist.database

import androidx.lifecycle.LiveData
import androidx.room.*

/**
 * Defines methods for using the [Account] class with Room.
 */
@Dao
interface AccountDao {
    @Insert
    fun insert(account: Account)

    @Update
    fun update(account: Account)

    @Query("SELECT * from accounts_table WHERE account_id = :key")
    fun getAccount(key: Long): LiveData<Account>

    @Query("SELECT * from accounts_table ORDER BY account_id DESC LIMIT 1")
    fun getLastAccount(): LiveData<Account>

    @Query("SELECT * FROM accounts_table ORDER BY account_id DESC")
    fun getAccounts(): LiveData<List<Account>>

    @Query("SELECT * FROM accounts_table WHERE admin = :admin AND enabled = :enabled ORDER BY first_name ASC")
    fun getAdminAccounts(admin: Boolean = true, enabled: Boolean = true): LiveData<List<Account>>
}
