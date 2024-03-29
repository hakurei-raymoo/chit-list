package gensokyo.hakurei.chitlist.adminhome

import android.os.Bundle
import android.view.*
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.navGraphViewModels
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayoutMediator
import gensokyo.hakurei.chitlist.R
import gensokyo.hakurei.chitlist.database.AppDatabase
import gensokyo.hakurei.chitlist.databinding.FragmentAdminViewPagerBinding
import kotlinx.android.synthetic.main.fragment_admin_view_pager.*

private const val TAG = "AdminViewPagerFragment"

class AdminViewPagerFragment : Fragment() {

    private val adminHomeViewModel: AdminHomeViewModel by navGraphViewModels(R.id.home_navigation) {
        val application = requireNotNull(this.activity).application
        val dataSource = AppDatabase.getInstance(application).adminHomeDao
        val arguments = AdminViewPagerFragmentArgs.fromBundle(arguments!!)
        val viewModelFactory = AdminHomeViewModelFactory(arguments.userId, dataSource )
        viewModelFactory
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentAdminViewPagerBinding.inflate(inflater, container, false)

        // To use the View Model with data binding, you have to explicitly
        // give the binding object a reference to it.
        binding.adminHomeViewModel = adminHomeViewModel

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

            // Add up button to toolbar.
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
            ADMIN_ACTIONS_PAGE_INDEX -> getString(R.string.actions)
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
            }
            2 -> {
                accounts_fab.hide()
                items_fab.show()
            }
            else -> {
                accounts_fab.hide()
                items_fab.hide()
            }
        }
    }
}
