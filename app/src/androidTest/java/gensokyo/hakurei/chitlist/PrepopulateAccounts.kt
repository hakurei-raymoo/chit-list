package gensokyo.hakurei.chitlist

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import gensokyo.hakurei.chitlist.database.Account
import gensokyo.hakurei.chitlist.database.AppDatabase
import gensokyo.hakurei.chitlist.utilities.Config
import gensokyo.hakurei.chitlist.utilities.hash
import org.junit.*
import org.junit.runner.RunWith

/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(AndroidJUnit4::class)
class PrepopulateAccounts {

    private lateinit var database: AppDatabase
    private var accounts = mutableListOf<Account>()

    @Before
    fun initDatabase() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        database = Room.databaseBuilder(
            context.applicationContext,
            AppDatabase::class.java,
            Config.databaseName
        )
            .fallbackToDestructiveMigration()
            .build()

        initAccounts(accounts)
    }

    private fun initAccounts(accounts: MutableList<Account>) {
        accounts.add(Account(firstName = "Sarah", lastName = "Connor", passwordHash = "skynet".hash()))
        accounts.add(Account(firstName = "Jack", lastName = "Sparrow", passwordHash = "blackpearl".hash()))
        accounts.add(Account(firstName = "Luke", lastName = "Skywalker", passwordHash = "theforce".hash()))
        accounts.add(Account(firstName = "Thomas A.", lastName = "Anderson", passwordHash = "neo".hash(), admin = true))
        accounts.add(Account(firstName = "Indiana", lastName = "Jones", passwordHash = "snakes".hash()))
        accounts.add(Account(firstName = "James", lastName = "Bond", passwordHash = "007".hash()))
        accounts.add(Account(firstName = "Ellen", lastName = "Ripley", passwordHash = "alien".hash()))
        accounts.add(Account(firstName = "Frodo", lastName = "Baggins", passwordHash = "shire".hash()))
        accounts.add(Account(firstName = "Tony", lastName = "Montana", passwordHash = "money".hash()))
        accounts.add(Account(firstName = "Harry", lastName = "Potter", passwordHash = "hogwarts".hash()))
        accounts.add(Account(firstName = "Snake", lastName = "Plissken", passwordHash = "nyc".hash()))
        accounts.add(Account(firstName = "Forrest", lastName = "Gump", passwordHash = "chocolate".hash()))
        accounts.add(Account(firstName = "Travis", lastName = "Bickle", passwordHash = "iris".hash()))
        accounts.add(Account(firstName = "Tyler", lastName = "Durden", passwordHash = "fightclub".hash()))
        accounts.add(Account(firstName = "John", lastName = "McClane", passwordHash = "christmas".hash()))
        accounts.add(Account(firstName = "Charles Foster", lastName = "Kane", passwordHash = "rosebud".hash()))
        accounts.add(Account(firstName = "Vito", lastName = "Corleone", passwordHash = "sicily".hash(), admin = true))
        accounts.add(Account(firstName = "John", lastName = "Wick", passwordHash = "babayaga".hash()))
        accounts.add(Account(firstName = "Leeloo", lastName = "Dallas", passwordHash = "multipass".hash()))
        accounts.add(Account(firstName = "Rick", lastName = "Deckard", passwordHash = "replicant".hash()))
        accounts.add(Account(firstName = "Nicholas", lastName = "Angel", passwordHash = "sandford".hash()))
        accounts.add(Account(firstName = "Maximus Decimus", lastName = "Meridius", passwordHash = "rome".hash(), enabled = false))
        accounts.add(Account(firstName = "Vincent Anton", lastName = "Freeman", passwordHash = "jerome".hash()))
        accounts.add(Account(firstName = "Austin", lastName = "Powers", passwordHash = "groovy".hash()))
        accounts.add(Account(firstName = "Leonard", lastName = "Lawrence", passwordHash = "7.62fmj".hash(), enabled = false))
        accounts.shuffle()
    }

    @After
    fun closeDatabase() {
        database.close()
    }

    @Test
    fun insertAccountData() {
        accounts.forEach {
            database.accountDao.insert(it)
            val retrievedAccount = database.accountDao.getLastAccount()
            assert(retrievedAccount.value == it)
        }
    }
}
