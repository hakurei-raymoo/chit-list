package gensokyo.hakurei.chitlist.database

import androidx.lifecycle.LiveData
import androidx.room.*

/**
 * Defines methods for using the Account class with Room.
 */
@Dao
interface AccountDao {

    @Insert
    fun insert(account: Account)

    @Update
    fun update(account: Account)

    @Delete
    fun delete(account: Account)

    /**
     * Selects and returns the row that matches the supplied id, which is our key.
     *
     * @param key id to match
     */
    @Query ("SELECT * from accounts_table WHERE id = :key")
    fun get(key: Long): Account

    /**
     * Selects and returns all rows in the table,
     *
     * sorted by id in descending order.
     */
    @Query("SELECT * FROM accounts_table ORDER BY id DESC")
    fun getAccounts(): LiveData<List<Account>>

    /**
     * Selects and returns the account with given id.
     */
    @Query("SELECT * from accounts_table WHERE id = :key")
    fun getAccount(key: Long): LiveData<Account>
}