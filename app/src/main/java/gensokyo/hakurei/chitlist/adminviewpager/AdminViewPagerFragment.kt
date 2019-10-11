package gensokyo.hakurei.chitlist.adminviewpager

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.google.android.material.tabs.TabLayoutMediator
import gensokyo.hakurei.chitlist.R
import gensokyo.hakurei.chitlist.SharedViewModel
import gensokyo.hakurei.chitlist.databinding.FragmentAdminViewPagerBinding

class AdminViewPagerFragment : Fragment() {

    private lateinit var sharedViewModel: SharedViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentAdminViewPagerBinding.inflate(inflater, container, false)

        activity?.let {
            sharedViewModel = ViewModelProviders.of(it).get(SharedViewModel::class.java)
        }

//        // Create an instance of the ViewModel Factory.
//        val application = requireNotNull(this.activity).application
//        val arguments = AdminViewPagerFragmentArgs.fromBundle(arguments!!)
//        val dataSource = AppDatabase.getInstance(application).loginDao
//        val viewModelFactory = HomeViewModelFactory(arguments.userKey, dataSource)
//
//        // Get a reference to the ViewModel associated with this fragment.
//        val homeViewModel =
//            ViewModelProviders.of(this, viewModelFactory).get(HomeViewModel::class.java)

        // To use the View Model with data binding, you have to explicitly
        // give the binding object a reference to it.
        binding.sharedViewModel = sharedViewModel

        // Specify the current activity as the lifecycle owner of the binding.
        // This is necessary so that the binding can observe LiveData updates.
        binding.lifecycleOwner = viewLifecycleOwner

        // Setup view pager.
        val tabLayout = binding.tabLayout
        val viewPager = binding.viewPager
        viewPager.adapter = AdminPagerAdapter(this)

        // Set the icon and text for each tab
        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
//            tab.setIcon(getTabIcon(position))
            tab.text = getTabTitle(position)
        }.attach()

        (activity as AppCompatActivity).setSupportActionBar(binding.toolbar)

        return binding.root
    }

//    private fun getTabIcon(position: Int): Int {
//        return when (position) {
//            ACCOUNTS_LIST_PAGE_INDEX -> R.drawable.accounts_list_tab_selector
//            ITEMS_LIST_PAGE_INDEX -> R.drawable.items_list_tab_selector
//            TRANSACTIONS_LIST_PAGE_INDEX -> R.drawable.transactions_list_tab_selector
//            else -> throw IndexOutOfBoundsException()
//        }
//    }

    private fun getTabTitle(position: Int): String? {
        return when (position) {
            SHOP_PAGE_INDEX -> getString(R.string.shop)
            CHECKOUT_PAGE_INDEX -> getString(R.string.checkout)
            ACCOUNTS_LIST_PAGE_INDEX -> getString(R.string.accounts)
            ITEMS_LIST_PAGE_INDEX -> getString(R.string.items)
            TRANSACTIONS_LIST_PAGE_INDEX -> getString(R.string.transactions)
            else -> null
        }
    }
}
