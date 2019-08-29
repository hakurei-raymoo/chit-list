package gensokyo.hakurei.chitlist.database

import androidx.lifecycle.LiveData
import androidx.room.*

/**
 * Defines methods for using the Transaction class with Room.
 */
@Dao
interface TransactionDao {

    @Insert
    fun insert(transaction: Transaction)

    @Update
    fun update(transaction: Transaction)

    @Delete
    fun delete(transaction: Transaction)

    @Query("SELECT * from transactions_table WHERE id = :key")
    fun get(key: Long): Transaction

    @Query("SELECT * FROM transactions_table ORDER BY id DESC")
    fun getTransactions(): LiveData<List<Transaction>>

    @Query("SELECT * from transactions_table WHERE id = :key")
    fun getTransaction(key: Long): LiveData<Transaction>

    @Query("SELECT * FROM transactions_table WHERE account_id = :accountId ORDER BY time DESC")
    fun getTransactionsForAccount(accountId: Long): Array<Transaction>

    @Query("SELECT * FROM transactions_table WHERE item_id = :itemId ORDER BY time DESC")
    fun getTransactionsForItem(itemId: Long): Array<Transaction>
}
