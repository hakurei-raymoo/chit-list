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
            "accounts_table.account_id, accounts_table.first_name, accounts_table.last_name," +
            "items_table.* FROM transactions_table" +
            " LEFT JOIN accounts_table ON transactions_table.account_id = accounts_table.account_id" +
            " LEFT JOIN items_table ON transactions_table.item_id = items_table.item_id" +
            " GROUP BY transaction_id ORDER BY transaction_id DESC")
    fun getTransactionsWithChildren(): LiveData<List<TransactionWithChildren>>
}
