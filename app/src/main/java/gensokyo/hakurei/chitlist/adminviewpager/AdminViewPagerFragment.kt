package gensokyo.hakurei.chitlist.adminviewpager

import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayoutMediator
import gensokyo.hakurei.chitlist.R
import gensokyo.hakurei.chitlist.SharedViewModel
import gensokyo.hakurei.chitlist.accountslist.AccountsListViewModel
import gensokyo.hakurei.chitlist.accountslist.AccountsListViewModelFactory
import gensokyo.hakurei.chitlist.database.AppDatabase
import gensokyo.hakurei.chitlist.databinding.FragmentAdminViewPagerBinding
import gensokyo.hakurei.chitlist.itemslist.ItemsListViewModel
import gensokyo.hakurei.chitlist.itemslist.ItemsListViewModelFactory
import gensokyo.hakurei.chitlist.transactionslist.TransactionsListViewModel
import gensokyo.hakurei.chitlist.transactionslist.TransactionsListViewModelFactory
import kotlinx.android.synthetic.main.fragment_admin_view_pager.*

private const val TAG = "AdminViewPagerFragment"

class AdminViewPagerFragment : Fragment() {

    private lateinit var sharedViewModel: SharedViewModel
    private lateinit var accountsListViewModel: AccountsListViewModel
    private lateinit var itemsListViewModel: ItemsListViewModel
    private lateinit var transactionsListViewModel: TransactionsListViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentAdminViewPagerBinding.inflate(inflater, container, false)

        // Create an instance of the ViewModel Factory.
        val application = requireNotNull(this.activity).application
        val accountDao = AppDatabase.getInstance(application).accountDao
        val itemDao = AppDatabase.getInstance(application).itemDao
        val transactionDao = AppDatabase.getInstance(application).transactionDao
        val accountsListViewModelFactory = AccountsListViewModelFactory(accountDao)
        val itemsListViewModelFactory = ItemsListViewModelFactory(itemDao)
        val transactionsListViewModelFactory = TransactionsListViewModelFactory(transactionDao)

        activity?.let {
            sharedViewModel = ViewModelProviders.of(it).get(SharedViewModel::class.java)
            accountsListViewModel =
                ViewModelProviders.of(it, accountsListViewModelFactory).get(
                    AccountsListViewModel::class.java)
            itemsListViewModel =
                ViewModelProviders.of(it, itemsListViewModelFactory).get(
                    ItemsListViewModel::class.java)
            transactionsListViewModel =
                ViewModelProviders.of(it, transactionsListViewModelFactory).get(
                    TransactionsListViewModel::class.java)
        }

        // To use the View Model with data binding, you have to explicitly
        // give the binding object a reference to it.
        binding.sharedViewModel = sharedViewModel
        binding.accountsListViewModel = accountsListViewModel
        binding.itemsListViewModel = itemsListViewModel
        binding.transactionsListViewModel = transactionsListViewModel

        // Specify the current activity as the lifecycle owner of the binding.
        // This is necessary so that the binding can observe LiveData updates.
        binding.lifecycleOwner = viewLifecycleOwner

        // Setup view pager.
        val tabLayout = binding.tabLayout
        val viewPager = binding.viewPager
        viewPager.adapter = AdminPagerAdapter(this)

        // Set the icon and text for each tab
        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            tab.text = getTabTitle(position)
        }.attach()

        // Hide and show FABs depending on page.
        viewPager.registerOnPageChangeCallback((object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                animateFab(position)
            }
        }))

        (activity as AppCompatActivity).run {
            setSupportActionBar(binding.toolbar)

            // Add up button.
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
            binding.toolbar.setNavigationOnClickListener {
                onBackPressed()
            }
        }

        setHasOptionsMenu(true)

        return binding.root
    }

    private fun getTabTitle(position: Int): String? {
        return when (position) {
            ADMIN_SETTINGS_PAGE_INDEX -> getString(R.string.admin_settings)
            ACCOUNTS_LIST_PAGE_INDEX -> getString(R.string.accounts)
            ITEMS_LIST_PAGE_INDEX -> getString(R.string.items)
            TRANSACTIONS_LIST_PAGE_INDEX -> getString(R.string.transactions)
            else -> null
        }
    }

    private fun animateFab(position: Int) {
        when (position) {
            1 -> {
                accounts_fab.show()
                items_fab.hide()
                transactions_fab.hide()
            }
            2 -> {
                accounts_fab.hide()
                items_fab.show()
                transactions_fab.hide()
            }
            3 -> {
                accounts_fab.hide()
                items_fab.hide()
                transactions_fab.show()
            }
            else -> {
                accounts_fab.hide()
                items_fab.hide()
                transactions_fab.hide()
            }
        }
    }
}
