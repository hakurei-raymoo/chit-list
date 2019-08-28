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
     * Selects and returns the row that matches the supplied accountId, which is our key.
     *
     * @param key accountId to match
     */
    @Query ("SELECT * from accounts_table WHERE accountId = :key")
    fun get(key: Long): Account

    /**
     * Deletes all values from the table.
     *
     * This does not delete the table, only its contents.
     */
    @Query("DELETE FROM accounts_table")
    fun clear()

    /**
     * Selects and returns all rows in the table,
     *
     * sorted by accountId in descending order.
     */
    @Query("SELECT * FROM accounts_table ORDER BY accountId DESC")
    fun getAccounts(): LiveData<List<Account>>

    /**
     * Selects and returns the account with given accountId.
     */
    @Query("SELECT * from accounts_table WHERE accountId = :key")
    fun getAccount(key: Long): LiveData<Account>
}