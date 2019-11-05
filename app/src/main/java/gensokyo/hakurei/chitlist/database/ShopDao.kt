package gensokyo.hakurei.chitlist.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

/**
 * The Data Access Object for Shop functions.
 */
@Dao
interface ShopDao {
    @Insert
    fun insert(transaction: Transaction)

    @Update
    fun update(account: Account)

//    @Query("SELECT * FROM items_table WHERE item_id = :key")
//    fun getItem(key: Long): Item

    @Query("SELECT * FROM items_table WHERE enabled = :enabled ORDER BY name ASC")
    fun getItems(enabled: Boolean = true): LiveData<List<Item>>

    @Query("SELECT transactions_table.*," +
            "a.account_id AS a_account_id, a.first_name AS a_first_name, a.last_name AS a_last_name," +
            "c.account_id AS c_account_id, c.first_name AS c_first_name, c.last_name AS c_last_name," +
            "i.item_id, i.name, i.price FROM transactions_table" +
            " LEFT JOIN accounts_table AS a ON transactions_table.account_id = a_account_id" +
            " LEFT JOIN accounts_table AS c ON transactions_table.creator_id = c_account_id" +
            " LEFT JOIN items_table AS i ON transactions_table.item_id = i.item_id" +
            " WHERE transactions_table.account_id = :account_id" +
            " GROUP BY transaction_id ORDER BY transaction_id DESC")
    fun getHistory(account_id: Long): LiveData<List<TransactionWithChildren>>

    @Query("SELECT * FROM accounts_table WHERE account_id = :key")
    fun getAccount(key: Long): LiveData<Account>
}
