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

    @Query("SELECT * FROM transactions_table WHERE transaction_id = :key")
    fun getTransaction(key: Long): LiveData<Transaction>

    @Query("SELECT * FROM transactions_table ORDER BY transaction_id DESC LIMIT 1")
    fun getLastTransaction(): LiveData<Transaction>

    @Query("SELECT * FROM transactions_table ORDER BY transaction_id DESC")
    fun getTransactions(): LiveData<List<Transaction>>

    @Query("SELECT transactions_table.*," +
            "a.account_id AS a_account_id, a.first_name AS a_first_name, a.last_name AS a_last_name," +
            "c.account_id AS c_account_id, c.first_name AS c_first_name, c.last_name AS c_last_name," +
            "i.item_id, i.name, i.price FROM transactions_table" +
            " LEFT JOIN accounts_table AS a ON transactions_table.account_id = a_account_id" +
            " LEFT JOIN accounts_table AS c ON transactions_table.creator_id = c_account_id" +
            " LEFT JOIN items_table AS i ON transactions_table.item_id = i.item_id" +
            " GROUP BY transaction_id ORDER BY transaction_id DESC")
    fun getTransactionsWithChildren(): LiveData<List<TransactionWithChildren>>

    /* Helpers to populate child columns. */
    @Query("SELECT account_id, first_name, last_name FROM accounts_table WHERE account_id = :key")
    fun getAccount(key: Long): BareAccount

    @Query("SELECT item_id, name, price FROM items_table WHERE item_id = :key")
    fun getItem(key: Long): BareItem

    @Query("SELECT account_id, first_name, last_name FROM accounts_table ORDER BY account_id ASC")
    fun getBareAccounts(): LiveData<List<BareAccount>>

    @Query("SELECT item_id, name, price FROM items_table ORDER BY item_id ASC")
    fun getBareItems(): LiveData<List<BareItem>>

    @Query("SELECT item_id, name, price FROM items_table WHERE credit = :credit AND enabled = :enabled ORDER BY item_id ASC")
    fun getBareCreditItems(credit: Boolean = true, enabled: Boolean = true): LiveData<List<BareItem>>
}
