package gensokyo.hakurei.chitlist.home

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import gensokyo.hakurei.chitlist.database.AppDatabase
import gensokyo.hakurei.chitlist.databinding.FragmentHomeBinding

private const val TAG = "HomeFragment"

class HomeFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        // Get a reference to the binding object and inflate the fragment views.
        val binding = FragmentHomeBinding.inflate(inflater, container, false)

//        // Create an instance of the ViewModel Factory.
//        val application = requireNotNull(this.activity).application
//        val arguments = HomeFragmentArgs.fromBundle(arguments!!)
//        val dataSource = AppDatabase.getInstance(application).loginDao
//        val viewModelFactory = HomeViewModelFactory(arguments.userKey, dataSource)
//
//        // Get a reference to the ViewModel associated with this fragment.
//        val homeViewModel =
//            ViewModelProviders.of(this, viewModelFactory).get(HomeViewModel::class.java)
//
//        // To use the View Model with data binding, you have to explicitly
//        // give the binding object a reference to it.
//        binding.homeViewModel = homeViewModel
//
//        // Specify the current activity as the lifecycle owner of the binding.
//        // This is necessary so that the binding can observe LiveData updates.
//        binding.lifecycleOwner = viewLifecycleOwner

//        // Add an Observer on the state variable for Navigating when ITEMS button is pressed.
//        homeViewModel.navigateToAccounts.observe(this, Observer { transaction ->
//            transaction?.let {
//                this.findNavController().navigate(
//                    HomeFragmentDirections.actionHomeFragmentToAccountsListFragment()
//                )
//                homeViewModel.onAccountsNavigated()
//            }
//        })
//
//        // Add an Observer on the state variable for Navigating when ITEMS button is pressed.
//        homeViewModel.navigateToItems.observe(this, Observer { transaction ->
//            transaction?.let {
//                this.findNavController().navigate(
//                    HomeFragmentDirections.actionHomeFragmentToItemsListFragment()
//                )
//                homeViewModel.onItemsNavigated()
//            }
//        })
//
//        // Add an Observer on the state variable for Navigating when ITEMS button is pressed.
//        homeViewModel.navigateToTransactions.observe(this, Observer { transaction ->
//            transaction?.let {
//                this.findNavController().navigate(
//                    HomeFragmentDirections.actionHomeFragmentToTransactionsListFragment()
//                )
//                homeViewModel.onTransactionsNavigated()
//            }
//        })

        return binding.root
    }
}
