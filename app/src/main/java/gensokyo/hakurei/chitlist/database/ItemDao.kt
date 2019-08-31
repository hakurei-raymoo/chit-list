package gensokyo.hakurei.chitlist.database

import androidx.lifecycle.LiveData
import androidx.room.*

/**
 * Defines methods for using the [Item] class with Room.
 */
@Dao
interface ItemDao {

    @Insert
    fun insert(item: Item)

    @Update
    fun update(item: Item)

    @Delete
    fun delete(item: Item)

    /**
     * Selects and returns the row that matches the supplied id, which is our key.
     *
     * @param key id to match
     */
    @Query ("SELECT * from items_table WHERE id = :key")
    fun get(key: Long): Item

    /**
     * Selects and returns all rows in the table,
     *
     * sorted by id in descending order.
     */
    @Query("SELECT * FROM items_table ORDER BY id DESC")
    fun getItems(): LiveData<List<Item>>

    /**
     * Selects and returns the [Item] with given id.
     */
    @Query("SELECT * from items_table WHERE id = :key")
    fun getItem(key: Long): LiveData<Item>
}