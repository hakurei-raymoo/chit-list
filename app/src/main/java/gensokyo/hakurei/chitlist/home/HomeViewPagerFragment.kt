package gensokyo.hakurei.chitlist.home

import android.os.Bundle
import android.view.*
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.navigation.navGraphViewModels
import com.google.android.material.tabs.TabLayoutMediator
import gensokyo.hakurei.chitlist.database.AppDatabase
import gensokyo.hakurei.chitlist.databinding.FragmentHomeViewPagerBinding
import kotlinx.android.synthetic.main.fragment_home_view_pager.*
import androidx.viewpager2.widget.ViewPager2
import gensokyo.hakurei.chitlist.R
import gensokyo.hakurei.chitlist.utilities.Config

private const val TAG = "HomeViewPagerFragment"

class HomeViewPagerFragment : Fragment() {

    private val homeViewModel: HomeViewModel by navGraphViewModels(R.id.home_navigation) {
        val application = requireNotNull(this.activity).application
        val dataSource = AppDatabase.getInstance(application).shopDao
        val arguments = HomeViewPagerFragmentArgs.fromBundle(arguments!!)
        val viewModelFactory = HomeViewModelFactory(arguments.userId, dataSource)
        viewModelFactory
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentHomeViewPagerBinding.inflate(inflater, container, false)

        // To use the View Model with data binding, you have to explicitly
        // give the binding object a reference to it.
        binding.homeViewModel = homeViewModel

        // Specify the current activity as the lifecycle owner of the binding.
        // This is necessary so that the binding can observe LiveData updates.
        binding.lifecycleOwner = viewLifecycleOwner

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
                animateFab(position, binding.hasItems, binding.balanceCapped)
            }
        }))

        homeViewModel.cart.observe(viewLifecycleOwner, Observer {
            // Show empty cart layout if not null or empty.
            binding.hasItems = !it.isNullOrEmpty()
            animateFab(viewPager.currentItem, binding.hasItems, binding.balanceCapped)
        })

        homeViewModel.balance.observe(viewLifecycleOwner, Observer {
            binding.balanceCapped = it > Config.BALANCE_CAP
        })

        (activity as AppCompatActivity).run {
            setSupportActionBar(binding.toolbar)

            // Add up button to toolbar.
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
            binding.toolbar.setNavigationOnClickListener {
                logout()
            }
        }

        homeViewModel.user.observe(viewLifecycleOwner, Observer {
            // Show admin settings only if User is an admin.
            if (it.admin) {
                requireActivity().invalidateOptionsMenu()
            }
        })

        setHasOptionsMenu(true)

        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.home_menu, menu)
    }

    @Override
    override fun onPrepareOptionsMenu (menu: Menu) {
        // Show admin settings only if User is an admin.
        if (homeViewModel.user.value?.admin == true) {
            menu.findItem(R.id.admin_settings_item).isVisible = true
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.admin_settings_item -> {
                adminAccess()
                true
            }
            R.id.change_password_item -> {
                changePassword()
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
        this.findNavController().navigate(
            HomeViewPagerFragmentDirections.actionHomeViewPagerFragmentToAdminViewPagerFragment(
                homeViewModel.userId
            )
        )
    }

    private fun changePassword() {
        this.findNavController().navigate(
            HomeViewPagerFragmentDirections.actionHomeViewPagerFragmentToChangePasswordDialog()
        )
    }

    private fun logout() {
//        Toast.makeText(activity, "${homeViewModel.user?.firstName} ${homeViewModel.user?.lastName} logged out", Toast.LENGTH_SHORT).show()
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

    private fun animateFab(position: Int, hasItems: Boolean, balanceCapped: Boolean) {
        if (position == 1 && hasItems && !balanceCapped) {
            checkout_fab.show()
        } else {
            checkout_fab.hide()
        }
    }
}
