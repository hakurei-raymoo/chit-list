package gensokyo.hakurei.chitlist

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.runner.AndroidJUnit4
import gensokyo.hakurei.chitlist.database.AppDatabase
import gensokyo.hakurei.chitlist.database.Transaction
import org.junit.After
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.Before
import java.util.*

/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(AndroidJUnit4::class)
class PrepopulateTransactions {

    private lateinit var database: AppDatabase
    private var numAccounts = 0
    private var items = database.itemDao.getItems()
    private var numItems = 0
    private var transactions = mutableListOf<Transaction>()

    @Before
    fun initDatabase() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        database = Room.databaseBuilder(
            context.applicationContext,
            AppDatabase::class.java,
            DATABASE_NAME
        )
            .fallbackToDestructiveMigration()
            .build()
    }

    @Before
    fun initTransactions() {
        numAccounts = database.accountDao.getBareAccounts().size
        numItems = items.value?.size!!
        val random = Random()
        val itemsList = items.value
        itemsList?.forEach {
            for (i in 1..5) {
                val accountId = random.nextInt(numAccounts).toLong()
                transactions.add(
                    Transaction(
                        accountId = accountId,
                        itemId = it.itemId,
                        amount = it.price
                    )
                )
            }
        }
        transactions.shuffle()
    }

    @After
    fun closeDatabase() {
        database.close()
    }

    @Test
    fun insertTransactionData() {
        transactions.forEach {
            database.transactionDao.insert(it)
        }
    }

    @Test
    fun getTransactionData() {
//        val retrievedTransactions = database.accountDao.getBareAccounts()
//        assert(retrievedTransactions == transactions.sortedWith(sortedWith(compareBy({it}, {it}))))
    }
}
