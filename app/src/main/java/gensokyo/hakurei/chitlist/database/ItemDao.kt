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

    @Query("SELECT * from items_table WHERE item_id = :key")
    fun getItem(key: Long): LiveData<Item>

    @Query("SELECT * from items_table ORDER BY item_id DESC LIMIT 1")
    fun getLastItem(): LiveData<Item>

    @Query("SELECT * FROM items_table ORDER BY item_id DESC")
    fun getItems(): LiveData<List<Item>>
}
