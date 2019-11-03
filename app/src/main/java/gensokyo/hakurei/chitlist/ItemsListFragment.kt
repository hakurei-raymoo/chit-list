package gensokyo.hakurei.chitlist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.navigation.navGraphViewModels
import gensokyo.hakurei.chitlist.viewmodels.AdminHomeViewModel
import gensokyo.hakurei.chitlist.utilities.GridMarginItemDecoration
import gensokyo.hakurei.chitlist.adapters.ItemAdapter
import gensokyo.hakurei.chitlist.adapters.ItemListener
import gensokyo.hakurei.chitlist.databinding.FragmentItemsListBinding

private const val TAG = "ItemsListFragment"

class ItemsListFragment : Fragment() {

    private val adminHomeViewModel: AdminHomeViewModel by navGraphViewModels(R.id.home_navigation)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        // Get a reference to the binding object and inflate the fragment views.
        val binding = FragmentItemsListBinding.inflate(inflater, container, false)

        // Sets the adapter of the RecyclerView with click listener.
        val adapter =
            ItemAdapter(ItemListener { itemId ->
                adminHomeViewModel.onItemClicked(itemId)
            })
        binding.itemsList.adapter = adapter

        // Specify the current activity as the lifecycle owner of the binding.
        // This is necessary so that the binding can observe LiveData updates.
        binding.lifecycleOwner = viewLifecycleOwner

        adminHomeViewModel.items.observe(viewLifecycleOwner, Observer {
            it?.let {
                adapter.submitList(it)
            }
        })

        // Add an Observer on the state variable for Navigating when EDIT button is pressed.
        adminHomeViewModel.navigateToEditItem.observe(viewLifecycleOwner, Observer { item ->
            item?.let {
                this.findNavController().navigate(
                    AdminViewPagerFragmentDirections.actionAdminViewPagerFragmentToItemDetailFragment(item)
                )
                adminHomeViewModel.onItemNavigated()
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
