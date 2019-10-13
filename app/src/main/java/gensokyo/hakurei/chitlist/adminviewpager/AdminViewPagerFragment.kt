package gensokyo.hakurei.chitlist.adminviewpager

import android.os.Bundle
import android.view.*
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

        (activity as AppCompatActivity).run {
            setSupportActionBar(binding.toolbar)
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
            binding.toolbar.setNavigationOnClickListener {
//                sharedViewModel.logout()
                onBackPressed()
            }
        }

        return binding.root
    }

//    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
//        super.onCreateOptionsMenu(menu, inflater)
//        inflater.inflate(R.menu.settings_menu, menu)
//    }
//
//    override fun onOptionsItemSelected(item: MenuItem): Boolean {
//        return when (item.itemId) {
//            R.id.settings -> {
//                enableAdminAccess()
//                true
//            }
//            else -> super.onOptionsItemSelected(item)
//        }
//    }
//
//    fun enableAdminAccess() {
//    }

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
