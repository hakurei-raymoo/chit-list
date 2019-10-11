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

    @Delete
    fun delete(account: Account)

    @Query("SELECT * from accounts_table WHERE account_id = :key AND password_hash = :passwordHash")
    fun getLogin(key: Long, passwordHash: String): Account

    @Query("SELECT * from accounts_table WHERE account_id = :key")
    fun getAccount(key: Long): LiveData<Account>

    @Query("SELECT * from accounts_table ORDER BY account_id DESC LIMIT 1")
    fun getLastAccount(): LiveData<Account>

    @Query("SELECT * FROM accounts_table ORDER BY account_id DESC")
    fun getAccounts(): LiveData<List<Account>>

    @Query("SELECT * FROM accounts_table ORDER BY first_name DESC")
    fun getBareAccounts(): List<BareAccount>
}
