package gensokyo.hakurei.chitlist.itemslist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import gensokyo.hakurei.chitlist.GridMarginItemDecoration
import gensokyo.hakurei.chitlist.R
import gensokyo.hakurei.chitlist.adminviewpager.AdminViewPagerFragmentDirections
import gensokyo.hakurei.chitlist.database.AppDatabase
import gensokyo.hakurei.chitlist.databinding.FragmentItemsListBinding

private const val TAG = "ItemsListFragment"

class ItemsListFragment : Fragment() {

    private lateinit var itemsListViewModel: ItemsListViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        // Get a reference to the binding object and inflate the fragment views.
        val binding = FragmentItemsListBinding.inflate(inflater, container, false)

        // Create an instance of the ViewModel Factory.
        val application = requireNotNull(this.activity).application
        val dataSource = AppDatabase.getInstance(application).itemDao
        val viewModelFactory = ItemsListViewModelFactory(dataSource)

        // Get a reference to the ViewModel associated with this fragment.
        activity?.let {
            itemsListViewModel =
                ViewModelProviders.of(it, viewModelFactory).get(ItemsListViewModel::class.java)
        }

        // To use the View Model with data binding, you have to explicitly
        // give the binding object a reference to it.
        binding.itemsListViewModel = itemsListViewModel

        val adapter = ItemAdapter(ItemListener { itemId ->
            itemsListViewModel.onItemClicked(itemId)
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
                    AdminViewPagerFragmentDirections.actionAdminViewPagerFragmentToItemDetailFragment(item)
                )
                itemsListViewModel.onItemNavigated()
            }
        })

        binding.itemsList.addItemDecoration(
            GridMarginItemDecoration(
                resources.getDimension(R.dimen.card_margin_grid).toInt()
            )
        )

        return binding.root
    }
}
