package gensokyo.hakurei.chitlist.itemslist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import gensokyo.hakurei.chitlist.MarginItemDecoration
import gensokyo.hakurei.chitlist.R
import gensokyo.hakurei.chitlist.database.AppDatabase
import gensokyo.hakurei.chitlist.databinding.FragmentItemsListBinding

private const val TAG = "ItemsListFragment"

class ItemsListFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        // Get a reference to the binding object and inflate the fragment views.
        val binding = FragmentItemsListBinding.inflate(inflater, container, false)

        val application = requireNotNull(this.activity).application

        // Create an instance of the ViewModel Factory.
        val dataSource = AppDatabase.getInstance(application).itemDao
        val viewModelFactory = ItemsListViewModelFactory(dataSource, application)

        // Get a reference to the ViewModel associated with this fragment.
        val itemsListViewModel =
            ViewModelProviders.of(this, viewModelFactory).get(ItemsListViewModel::class.java)

        // To use the View Model with data binding, you have to explicitly
        // give the binding object a reference to it.
        binding.itemsListViewModel = itemsListViewModel

        val adapter = ItemAdapter(ItemListener { itemId ->
            itemsListViewModel.onEditItemClicked(itemId)
        })
        binding.itemsList.adapter = adapter

        // Specify the current activity as the lifecycle owner of the binding.
        // This is necessary so that the binding can observe LiveData updates.
        binding.lifecycleOwner = viewLifecycleOwner

        itemsListViewModel.items.observe(viewLifecycleOwner, Observer {
            it?.let {
                adapter.submitList(it)
            }
        })

        // Add an Observer on the state variable for Navigating when EDIT button is pressed.
        itemsListViewModel.navigateToEditItem.observe(viewLifecycleOwner, Observer { item ->
            item?.let {
                this.findNavController().navigate(
                    ItemsListFragmentDirections.actionItemsListFragmentToItemDetailFragment(item)
                )
                itemsListViewModel.onEditItemNavigated()
            }
        })

        val manager = GridLayoutManager(activity, 2)
        binding.itemsList.layoutManager = manager
        binding.itemsList.addItemDecoration(
            MarginItemDecoration(
                resources.getDimension(R.dimen.grid_spacing_small).toInt()
            )
        )

        return binding.root
    }
}
