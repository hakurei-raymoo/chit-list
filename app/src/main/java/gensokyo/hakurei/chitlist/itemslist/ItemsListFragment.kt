package gensokyo.hakurei.chitlist.itemslist

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import gensokyo.hakurei.chitlist.R
import gensokyo.hakurei.chitlist.database.AppDatabase
import gensokyo.hakurei.chitlist.databinding.FragmentItemsListBinding
import gensokyo.hakurei.chitlist.databinding.FragmentUsersListBinding
import gensokyo.hakurei.chitlist.userslist.*

private const val TAG = "ItemsListFragment"

class ItemsListFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        Log.i(TAG, "onCreateView called")

        // Get a reference to the binding object and inflate the fragment views.
        val binding: FragmentItemsListBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_items_list, container, false)

        val application = requireNotNull(this.activity).application

        // Create an instance of the ViewModel Factory.
        val dataSource = AppDatabase.getInstance(application).itemDao
        val viewModelFactory = ItemsListViewModelFactory(dataSource, application)

        // Get a reference to the ViewModel associated with this fragment.
        val itemsListViewModel =
            ViewModelProviders.of(
                this, viewModelFactory).get(ItemsListViewModel::class.java)

        // To use the View Model with data binding, you have to explicitly
        // give the binding object a reference to it.
        binding.itemsListViewModel = itemsListViewModel

        val adapter = ItemAdaptor(ItemListener { itemId ->
            Toast.makeText(context, "${itemId}", Toast.LENGTH_LONG).show()
//            itemsListViewModel.onEditItemClicked(itemId)
        })
        binding.itemsList.adapter = adapter

        itemsListViewModel.items.observe(viewLifecycleOwner, Observer {
            it?.let {
                adapter.submitList(it)
            }
        })

        // Specify the current activity as the lifecycle owner of the binding.
        // This is necessary so that the binding can observe LiveData updates.
        binding.setLifecycleOwner(this)

        // Add an Observer on the state variable for Navigating when EDIT button is pressed.
//        itemsListViewModel.navigateToEditItem.observe(this, Observer { item ->
//            item?.let {
//                this.findNavController().navigate(
//                    ItemsListFragmentDirections
//                        .actionItemsListFragmentToItemDetailFragment(item))
//                itemsListViewModel.onEditItemNavigated()
//            }
//        })

        return binding.root
}
}
