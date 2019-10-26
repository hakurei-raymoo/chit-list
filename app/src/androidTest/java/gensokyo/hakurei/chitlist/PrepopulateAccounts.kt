package gensokyo.hakurei.chitlist

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import gensokyo.hakurei.chitlist.database.Account
import gensokyo.hakurei.chitlist.database.AppDatabase
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
            DATABASE_NAME
        )
            .fallbackToDestructiveMigration()
            .build()

        initAccounts(accounts)
    }

    private fun initAccounts(accounts: MutableList<Account>) {
        accounts.add(Account(firstName = "Sarah", lastName = "Connor", passwordHash = "skynet"))
        accounts.add(Account(firstName = "Jack", lastName = "Sparrow", passwordHash = "blackpearl"))
        accounts.add(Account(firstName = "Luke", lastName = "Skywalker", passwordHash = "theforce"))
        accounts.add(Account(firstName = "Thomas A.", lastName = "Anderson", passwordHash = "neo"))
        accounts.add(Account(firstName = "Indiana", lastName = "Jones", passwordHash = "snakes"))
        accounts.add(Account(firstName = "James", lastName = "Bond", passwordHash = "007"))
        accounts.add(Account(firstName = "Ellen", lastName = "Ripley", passwordHash = "alien"))
        accounts.add(Account(firstName = "Frodo", lastName = "Baggins", passwordHash = "shire"))
        accounts.add(Account(firstName = "Tony", lastName = "Montana", passwordHash = "money"))
        accounts.add(Account(firstName = "Harry", lastName = "Potter", passwordHash = "hogwarts"))
        accounts.add(Account(firstName = "Snake", lastName = "Plissken", passwordHash = "nyc"))
        accounts.add(Account(firstName = "Forrest", lastName = "Gump", passwordHash = "chocolate"))
        accounts.add(Account(firstName = "Travis", lastName = "Bickle", passwordHash = "iris"))
        accounts.add(Account(firstName = "Tyler", lastName = "Durden", passwordHash = "fightclub"))
        accounts.add(Account(firstName = "John", lastName = "McClane", passwordHash = "christmas"))
        accounts.add(Account(firstName = "Charles Foster", lastName = "Kane", passwordHash = "rosebud"))
        accounts.add(Account(firstName = "Vito", lastName = "Corleone", passwordHash = "sicily"))
        accounts.add(Account(firstName = "John", lastName = "Wick", passwordHash = "babayaga"))
        accounts.add(Account(firstName = "Leeloo", lastName = "Dallas", passwordHash = "multipass"))
        accounts.add(Account(firstName = "Rick", lastName = "Deckard", passwordHash = "replicant"))
        accounts.add(Account(firstName = "Beatrix", lastName = "Kiddo", passwordHash = "blackmamba"))
        accounts.add(Account(firstName = "Maximus Decimus", lastName = "Meridius", passwordHash = "rome"))
        accounts.add(Account(firstName = "Django", lastName = "Freeman", passwordHash = "hildi"))
        accounts.add(Account(firstName = "Dorothy", lastName = "Gale", passwordHash = "kansas"))
        accounts.add(Account(firstName = "Leonard", lastName = "Lawrence", passwordHash = "7.62fmj"))
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
