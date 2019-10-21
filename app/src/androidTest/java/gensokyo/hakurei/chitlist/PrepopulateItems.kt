package gensokyo.hakurei.chitlist

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import gensokyo.hakurei.chitlist.database.AppDatabase
import gensokyo.hakurei.chitlist.database.Item
import org.junit.*
import org.junit.runner.RunWith

/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(AndroidJUnit4::class)
class PrepopulateItems {

    private lateinit var database: AppDatabase
    private var items = mutableListOf<Item>()

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

        initItems()
    }

    private fun initItems() {
        items.add(Item(name = "Coffee", price = 350))
        items.add(Item(name = "Tea", price = 200))
        items.add(Item(name = "Water", price = 100))
        items.add(Item(name = "Soft drink", price = 100))
        items.add(Item(name = "Energy drink", price = 300))
        items.add(Item(name = "Pie", price = 300))
        items.add(Item(name = "Sausage roll", price = 250))
        items.add(Item(name = "Pastry", price = 300))
        items.add(Item(name = "Soup", price = 200))
        items.add(Item(name = "Sandwich", price = 500))
        items.shuffle()
    }

    @After
    fun closeDatabase() {
        database.close()
    }

    @Test
    fun insertItemData() {
        items.forEach {
            database.itemDao.insert(it)
        }
    }

    @Test
    fun getItemData() {
        val retrievedItems = database.itemDao.getItems()
        assert(retrievedItems.value == items)
    }
}