package gensokyo.hakurei.chitlist

import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.navigation.navGraphViewModels
import androidx.viewpager2.widget.ViewPager2
import gensokyo.hakurei.chitlist.utilities.GridMarginItemDecoration
import gensokyo.hakurei.chitlist.adapters.CheckoutAdapter
import gensokyo.hakurei.chitlist.adapters.CheckoutListener
import gensokyo.hakurei.chitlist.viewmodels.HomeViewModel
import gensokyo.hakurei.chitlist.databinding.FragmentCheckoutBinding
import gensokyo.hakurei.chitlist.adapters.SHOP_PAGE_INDEX

private const val TAG = "CheckoutFragment"

class CheckoutFragment : Fragment() {

    private val homeViewModel: HomeViewModel by navGraphViewModels(R.id.home_navigation)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        // Get a reference to the binding object and inflate the fragment views.
        val binding = FragmentCheckoutBinding.inflate(inflater, container, false)

        // Sets the adapter of the RecyclerView with click listener.
        val adapter =
            CheckoutAdapter(CheckoutListener {
                homeViewModel.removeItem(it)
                Toast.makeText(activity, "Removed ${it.name} from cart", Toast.LENGTH_SHORT).show()
            })
        binding.itemsList.adapter = adapter

        // Specify the current activity as the lifecycle owner of the binding.
        // This is necessary so that the binding can observe LiveData updates.
        binding.lifecycleOwner = viewLifecycleOwner

        homeViewModel.cart.observe(viewLifecycleOwner, Observer {
            // Show empty cart layout if not null or empty.
            binding.hasItems = !it.isNullOrEmpty()
            it?.let {
                adapter.submitList(it)
                adapter.notifyDataSetChanged()
            }
        })

        binding.addItem.setOnClickListener {
            navigateToShopPage()
        }

        // Add an Observer on the state variable for Navigating when EDIT button is pressed.
        homeViewModel.navigateToLogin.observe(viewLifecycleOwner, Observer {
            if (it == true) {
                this.findNavController().navigateUp()

                homeViewModel.onLoginNavigated()

                Toast.makeText(activity, "Checked out ${homeViewModel.cart.value?.size} items", Toast.LENGTH_SHORT).show()
            }
        })

        binding.itemsList.addItemDecoration(
            GridMarginItemDecoration(
                resources.getDimension(R.dimen.card_margin_grid).toInt()
            )
        )

        setHasOptionsMenu(true)

        return binding.root
    }

    private fun navigateToShopPage() {
        requireActivity().findViewById<ViewPager2>(R.id.view_pager).currentItem =
            SHOP_PAGE_INDEX
    }
}
