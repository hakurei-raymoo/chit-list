package gensokyo.hakurei.chitlist.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Query

/**
 * The Data Access Object for Shop functions.
 */
@Dao
interface ShopDao {
    @Query("SELECT * FROM items_table WHERE locked = :locked ORDER BY name ASC")
    fun getItems(locked: Boolean = false): LiveData<List<Item>>
}
