package gensokyo.hakurei.chitlist.homeviewpager

import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import com.google.android.material.tabs.TabLayoutMediator
import gensokyo.hakurei.chitlist.SharedViewModel
import gensokyo.hakurei.chitlist.database.AppDatabase
import gensokyo.hakurei.chitlist.databinding.FragmentHomeViewPagerBinding
import gensokyo.hakurei.chitlist.history.HistoryViewModel
import gensokyo.hakurei.chitlist.history.HistoryViewModelFactory
import kotlinx.android.synthetic.main.fragment_home_view_pager.*
import androidx.viewpager2.widget.ViewPager2
import gensokyo.hakurei.chitlist.R
import gensokyo.hakurei.chitlist.checkout.CheckoutViewModel
import gensokyo.hakurei.chitlist.checkout.CheckoutViewModelFactory

private const val TAG = "HomeViewPagerFragment"

class HomeViewPagerFragment : Fragment() {

    private lateinit var sharedViewModel: SharedViewModel
    private lateinit var checkoutViewModel: CheckoutViewModel
    private lateinit var historyViewModel: HistoryViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentHomeViewPagerBinding.inflate(inflater, container, false)

        activity?.let {
            // Create an instance of the ViewModel Factory.
            val application = requireNotNull(this.activity).application
            val dataSource = AppDatabase.getInstance(application).shopDao
            val checkoutViewModelFactory = CheckoutViewModelFactory(dataSource)
            val historyViewModelFactory = HistoryViewModelFactory(dataSource)

            // Get a reference to the ViewModels associated with this fragment.
            sharedViewModel = ViewModelProviders.of(it).get(SharedViewModel::class.java)
            checkoutViewModel =
                ViewModelProviders.of(it, checkoutViewModelFactory).get(CheckoutViewModel::class.java)
            historyViewModel =
                ViewModelProviders.of(it, historyViewModelFactory).get(HistoryViewModel::class.java)
        }

        // To use the View Model with data binding, you have to explicitly
        // give the binding object a reference to it.
        binding.sharedViewModel = sharedViewModel
        binding.checkoutViewModel = checkoutViewModel

        // Specify the current activity as the lifecycle owner of the binding.
        // This is necessary so that the binding can observe LiveData updates.
        binding.lifecycleOwner = viewLifecycleOwner

        // Set accountId which triggers switchMap for updating history and map for updating balance.
        historyViewModel.updateHistory(sharedViewModel.user?.accountId!!)

        historyViewModel.balance.observe(viewLifecycleOwner, Observer {
            Log.i(TAG, "Observed balance=$it")
            it?.let {
                sharedViewModel.setBalance(it)
            }
        })

        // Setup view pager.
        val tabLayout = binding.tabLayout
        val viewPager = binding.viewPager
        viewPager.adapter = HomePagerAdapter(this)

        // Set the icon and text for each tab
        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
//            tab.setIcon(getTabIcon(position))
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
                logout()
            }
        }

        setHasOptionsMenu(true)

        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.home_menu, menu)
        // Show admin settings only if User is an admin.
        menu.findItem(R.id.admin_settings_item).isVisible = sharedViewModel.user?.admin == true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.admin_settings_item -> {
                adminAccess()
                true
            }
            R.id.change_password_item -> {
                // TODO: Implement change password.
                Toast.makeText(activity, "Not yet implemented. Contact an administrator to change passwords.", Toast.LENGTH_SHORT).show()
                true
            }
            R.id.logout_item -> {
                logout()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun adminAccess() {
        Log.i(TAG, "Admin Access.")
        this.findNavController().navigate(
            HomeViewPagerFragmentDirections.actionHomeViewPagerFragmentToAdminViewPagerFragment()
        )
    }

    private fun logout() {
        Toast.makeText(activity, "${sharedViewModel.user?.firstName} ${sharedViewModel.user?.lastName} logged out.", Toast.LENGTH_SHORT).show()
        (activity as AppCompatActivity).run {
            onBackPressed()
        }
    }

//    private fun getTabIcon(position: Int): Int {
//        return when (position) {
//            SHOP_PAGE_INDEX -> R.drawable.shop_tab_selector
//            CHECKOUT_PAGE_INDEX -> R.drawable.checkout_tab_selector
//            else -> throw IndexOutOfBoundsException()
//        }
//    }

    private fun getTabTitle(position: Int): String? {
        return when (position) {
            SHOP_PAGE_INDEX -> getString(R.string.shop)
            CHECKOUT_PAGE_INDEX -> getString(R.string.checkout)
            HISTORY_PAGE_INDEX -> getString(R.string.history)
            else -> null
        }
    }

    private fun animateFab(position: Int) {
        when (position) {
            1 -> {
                checkout_fab.show()
            }
            else -> {
                checkout_fab.hide()
            }
        }
    }
}