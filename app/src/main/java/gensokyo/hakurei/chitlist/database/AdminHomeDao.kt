package gensokyo.hakurei.chitlist.database

import androidx.lifecycle.LiveData
import androidx.room.*

/**
 * The Data Access Object for Admin functions.
 */
@Dao
interface AdminHomeDao {
    @Insert
    fun insert(item: Item)

    @Update
    fun update(item: Item)

    @Insert
    fun insert(transaction: Transaction)

    @Update
    fun update(transaction: Transaction)

    @Insert
    fun insert(account: Account)

    @Update
    fun update(account: Account)

    @Query("SELECT * FROM accounts_table ORDER BY account_id DESC")
    fun getAccounts(): LiveData<List<Account>>

    @Query("SELECT * FROM items_table ORDER BY item_id DESC")
    fun getItems(): LiveData<List<Item>>

    @Query("SELECT transactions_table.*," +
            "a.account_id AS a_account_id, a.first_name AS a_first_name, a.last_name AS a_last_name," +
            "c.account_id AS c_account_id, c.first_name AS c_first_name, c.last_name AS c_last_name," +
            "i.item_id, i.name, i.price FROM transactions_table" +
            " LEFT JOIN accounts_table AS a ON transactions_table.account_id = a_account_id" +
            " LEFT JOIN accounts_table AS c ON transactions_table.creator_id = c_account_id" +
            " LEFT JOIN items_table AS i ON transactions_table.item_id = i.item_id" +
            " GROUP BY transaction_id ORDER BY transaction_id DESC")
    fun getTransactionsWithChildren(): LiveData<List<TransactionWithChildren>>
}
