package gensokyo.hakurei.chitlist.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

/**
 * The Data Access Object for Shop functions.
 */
@Dao
interface ShopDao {
    @Insert
    fun insert(transaction: Transaction)

    @Query("SELECT * FROM items_table WHERE item_id = :key")
    fun getItem(key: Long): Item

    @Query("SELECT * FROM items_table WHERE enabled = :enabled ORDER BY name ASC")
    fun getItems(enabled: Boolean = true): LiveData<List<Item>>

    @Query("SELECT transactions_table.*," +
            "accounts_table.account_id, accounts_table.first_name, accounts_table.last_name," +
            "items_table.* FROM transactions_table" +
            " LEFT JOIN accounts_table ON transactions_table.account_id = accounts_table.account_id" +
            " LEFT JOIN items_table ON transactions_table.item_id = items_table.item_id" +
            " WHERE transactions_table.account_id = :account_id" +
            " GROUP BY transaction_id ORDER BY transaction_id DESC")
    fun getHistory(account_id: Long): LiveData<List<TransactionWithChildren>>
}
