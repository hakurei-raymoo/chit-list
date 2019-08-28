package gensokyo.hakurei.chitlist.database

import android.content.Context
import android.util.Log
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

private const val TAG = "AppDatabase"

@Database(entities = [Account::class, Item::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {

    abstract val accountDao: AccountDao
    abstract val itemDao: ItemDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            synchronized(this) {
                var instance = INSTANCE

                if (instance == null) {

                    Log.i(TAG, "New Database created, Room.databaseBuilder called")
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        AppDatabase::class.java,
                        "chit_list-db"
                    )
                        .fallbackToDestructiveMigration()
                        .build()
                    INSTANCE = instance
                }
                return instance
            }
        }
    }
}