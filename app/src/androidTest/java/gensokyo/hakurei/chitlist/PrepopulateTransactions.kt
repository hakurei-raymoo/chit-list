package gensokyo.hakurei.chitlist

import android.content.Context
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import gensokyo.hakurei.chitlist.database.AppDatabase
import gensokyo.hakurei.chitlist.database.Transaction
import org.junit.After
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.Before
import java.util.*
import org.junit.rules.TestRule
import org.junit.Rule

/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(AndroidJUnit4::class)
class PrepopulateTransactions {

    @Rule
    @JvmField
    var rule: TestRule = InstantTaskExecutorRule()

    private lateinit var database: AppDatabase
    private var numAccounts = 0
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

        initTransactions()
    }

    fun initTransactions() {
        val accounts = database.accountDao.getAccounts()
        val items = database.itemDao.getItems()
        accounts.observeForever{}
        items.observeForever{}
        numAccounts =  accounts.value?.size!!
        numItems = items.value?.size!!
        val random = Random()
        val itemsList = items.value

        itemsList?.forEach {
            val numTransactions = random.nextInt(15) + 5
            for (i in 1..numTransactions) {
                val accountId = random.nextInt(numAccounts - 1).toLong() + 2L
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
