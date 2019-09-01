package gensokyo.hakurei.chitlist.itemdetail

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import gensokyo.hakurei.chitlist.R
import gensokyo.hakurei.chitlist.database.AppDatabase
import gensokyo.hakurei.chitlist.databinding.FragmentItemDetailBinding

private const val TAG = "ItemDetailFragment"

class ItemDetailFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        // Get a reference to the binding object and inflate the fragment views.
        val binding: FragmentItemDetailBinding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_item_detail, container, false
        )

        val application = requireNotNull(this.activity).application
        val arguments = ItemDetailFragmentArgs.fromBundle(arguments!!)

        // Create an instance of the ViewModel Factory.
        val dataSource = AppDatabase.getInstance(application).itemDao
        val viewModelFactory = ItemDetailViewModelFactory(arguments.itemKey, dataSource)

        // Get a reference to the ViewModel associated with this fragment.
        val itemDetailViewModel =
            ViewModelProviders.of(this, viewModelFactory).get(ItemDetailViewModel::class.java)

        // To use the View Model with data binding, you have to explicitly
        // give the binding object a reference to it.
        binding.itemDetailViewModel = itemDetailViewModel

        binding.setLifecycleOwner(this)

        // Add an Observer to the state variable for Navigating when a Submit button is tapped.
        itemDetailViewModel.navigateToItemsList.observe(this, Observer {
            if (it == true) { // Observed state is true.
                val inputMethodManager =
                    activity?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager

                // Hide the keyboard.
                inputMethodManager.hideSoftInputFromWindow(view?.windowToken, 0)

                this.findNavController().navigate(
                    ItemDetailFragmentDirections.actionItemDetailFragmentToItemsListFragment()
                )
                // Reset state to make sure we only navigate once, even if the device
                // has a configuration change.
                itemDetailViewModel.doneNavigating()
            }
        })

        // Test Observer to report changes on item.
        // TODO: Remove after testing.
        itemDetailViewModel.publicItem.observe(this, Observer {
            Log.i(TAG, "Observed ${itemDetailViewModel.publicItem.value}")
        })

        return binding.root
    }
}
