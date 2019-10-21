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

        initAccounts()
    }

    private fun initAccounts() {
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
        accounts.add(Account(firstName = "Travis", lastName = "Bickle", passwordHash = "scum"))
        accounts.add(Account(firstName = "Tyler", lastName = "Durden", passwordHash = "fightclub"))
        accounts.add(Account(firstName = "John", lastName = "McClane", passwordHash = "christmas"))
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
        }
    }

    @Test
    fun getAccountData() {
        val retrievedAccounts = database.accountDao.getAccounts()
        assert(retrievedAccounts == accounts.sortedWith(compareBy({ String.format("${it.firstName} ${it.lastName}") }, { String.format("${it.firstName} ${it.lastName}") })))
    }
}