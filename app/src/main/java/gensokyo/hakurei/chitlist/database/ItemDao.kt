package gensokyo.hakurei.chitlist.database

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface ItemDao {

    @Insert
    fun insert(item: Item)

    @Update
    fun update(item: Item)

    @Delete
    fun delete(item: Item)

    /**
     * Deletes all values from the table.
     *
     * This does not delete the table, only its contents.
     */
    @Query("DELETE FROM items_table")
    fun clear()

    /**
     * Selects and returns all rows in the table,
     *
     * sorted by itemId in descending order.
     */
    @Query("SELECT * FROM items_table ORDER BY itemId DESC")
    fun getItems(): LiveData<List<Item>>

    /**
     * Selects and returns the item with given itemId.
     */
    @Query("SELECT * from items_table WHERE itemId = :key")
    fun getItem(key: Long): LiveData<Item>
}