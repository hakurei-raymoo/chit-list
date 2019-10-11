package gensokyo.hakurei.chitlist.database

import android.content.Context
import android.util.Log
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import gensokyo.hakurei.chitlist.DATABASE_NAME

private const val TAG = "AppDatabase"

@Database(
    entities = [Account::class, Item::class, Transaction::class],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {

    abstract val loginDao: LoginDao
    abstract val shopDao: ShopDao
    abstract val accountDao: AccountDao
    abstract val itemDao: ItemDao
    abstract val transactionDao: TransactionDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            synchronized(this) {
                var instance = INSTANCE

                if (instance == null) {

                    Log.i(TAG, "Room.databaseBuilder called")
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        AppDatabase::class.java,
                        DATABASE_NAME
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
