package gensokyo.hakurei.chitlist.database

import android.content.Context
import android.util.Log
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import gensokyo.hakurei.chitlist.utilities.Config
import gensokyo.hakurei.chitlist.utilities.hash
import java.util.concurrent.Executors

private const val TAG = "AppDatabase"

@Database(
    entities = [Account::class, Item::class, Transaction::class],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {

    abstract val loginDao: LoginDao
    abstract val shopDao: ShopDao
    abstract val adminSettingsDao: AdminSettingsDao
    abstract val adminHomeDao: AdminHomeDao
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
                        Config.DATABASE_NAME
                    )
                        .fallbackToDestructiveMigration()
                        .addCallback(object : Callback() {
                            override fun onCreate(db: SupportSQLiteDatabase) {
                                super.onCreate(db)

                                prepopulate(context)
                            }
                        })
                        .build()
                    INSTANCE = instance
                }
                return instance
            }
        }

        private fun prepopulate(context: Context) {
            Executors.newSingleThreadScheduledExecutor().execute {
                getInstance(context).accountDao
                    .insert(
                        Account(
                            firstName = "admin",
                            lastName = "default",
                            passwordHash = "".hash(),
                            admin = true
                        )
                    )
                getInstance(context).itemDao
                    .insert(
                        Item(
                            name = "Cash",
                            enabled = false
                        )
                    )
                getInstance(context).itemDao
                    .insert(
                        Item(
                            name = "EFT",
                            enabled = false
                        )
                    )
            }
        }
    }
}
