package gensokyo.hakurei.chitlist.database

import androidx.room.*
import androidx.sqlite.db.SupportSQLiteQuery
import androidx.room.RawQuery

/**
 * The Data Access Object for Admin Settings functions.
 */
@Dao
interface AdminActionsDao {
    @Query("SELECT * FROM accounts_table ORDER BY account_id ASC")
    fun getAccountsWithHistory(): List<AccountWithHistory>

    @Query("SELECT * FROM items_table ORDER BY item_id ASC")
    fun getItems(): List<Item>

    @Query("SELECT transactions_table.*," +
            "a.account_id AS a_account_id, a.first_name AS a_first_name, a.last_name AS a_last_name," +
            "c.account_id AS c_account_id, c.first_name AS c_first_name, c.last_name AS c_last_name," +
            "i.item_id, i.name, i.price FROM transactions_table" +
            " LEFT JOIN accounts_table AS a ON transactions_table.account_id = a_account_id" +
            " LEFT JOIN accounts_table AS c ON transactions_table.creator_id = c_account_id" +
            " LEFT JOIN items_table AS i ON transactions_table.item_id = i.item_id" +
            " GROUP BY transaction_id ORDER BY transaction_id ASC")
    fun getTransactionsWithChildren(): List<TransactionWithChildren>

    @RawQuery
    fun checkpoint(supportSQLiteQuery: SupportSQLiteQuery): Int
}
