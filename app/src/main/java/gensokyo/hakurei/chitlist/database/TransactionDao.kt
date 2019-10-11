package gensokyo.hakurei.chitlist.database

import androidx.lifecycle.LiveData
import androidx.room.*

/**
 * Defines methods for using the [Transaction] class with Room.
 */
@Dao
interface TransactionDao {
    @Insert
    fun insert(transaction: Transaction)

    @Update
    fun update(transaction: Transaction)

    @Delete
    fun delete(transaction: Transaction)

    @Query("SELECT * FROM transactions_table WHERE transaction_id = :key")
    fun getTransaction(key: Long): LiveData<Transaction>

    @Query("SELECT * FROM transactions_table ORDER BY transaction_id DESC LIMIT 1")
    fun getLastTransaction(): LiveData<Transaction>

    @Query("SELECT * FROM transactions_table ORDER BY transaction_id DESC")
    fun getTransactions(): LiveData<List<Transaction>>

    @Query("SELECT transactions_table.*, accounts_table.*, items_table.* FROM transactions_table" +
        " LEFT JOIN accounts_table ON transactions_table.account_id = accounts_table.account_id" +
        " LEFT JOIN items_table ON transactions_table.item_id = items_table.item_id" +
        " GROUP BY transaction_id ORDER BY transaction_id DESC")
    fun getTransactionsWithChildren(): LiveData<List<TransactionWithChildren>>

    @Query("SELECT * FROM accounts_table WHERE account_id = :key")
    fun getAccount(key: Long): LiveData<BareAccount>

    @Query("SELECT * FROM items_table WHERE item_id = :key")
    fun getItem(key: Long): Item
}
