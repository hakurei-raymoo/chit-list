package gensokyo.hakurei.chitlist.database

import androidx.lifecycle.LiveData
import androidx.room.*

/**
 * Defines methods for using the User class with Room.
 */
@Dao
interface UserDao {

    @Insert
    fun insert(user: User)

    @Update
    fun update(user: User)

    @Delete
    fun delete(user: User)

    /**
     * Selects and returns the row that matches the supplied userId, which is our key.
     *
     * @param key userId to match
     */
    @Query ("SELECT * from users_table WHERE userId = :key")
    fun get(key: Long): User

    /**
     * Deletes all values from the table.
     *
     * This does not delete the table, only its contents.
     */
    @Query("DELETE FROM users_table")
    fun clear()

    /**
     * Selects and returns all rows in the table,
     *
     * sorted by userId in descending order.
     */
    @Query("SELECT * FROM users_table ORDER BY userId DESC")
    fun getAllUsers(): LiveData<List<User>>

    /**
     * Selects and returns the latest user.
     */
    @Query("SELECT * FROM users_table ORDER BY userId DESC LIMIT 1")
    fun getLastUser(): User?

    /**
     * Selects and returns the user with given userId.
     */
    @Query("SELECT * from users_table WHERE userId = :key")
    fun getUserWithId(key: Long): LiveData<User>
}