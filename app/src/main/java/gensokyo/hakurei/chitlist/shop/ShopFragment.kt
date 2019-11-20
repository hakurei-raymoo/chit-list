package gensokyo.hakurei.chitlist.shop

import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.navigation.navGraphViewModels
import gensokyo.hakurei.chitlist.R
import gensokyo.hakurei.chitlist.utilities.GridMarginItemDecoration
import gensokyo.hakurei.chitlist.home.HomeViewModel
import gensokyo.hakurei.chitlist.databinding.FragmentShopBinding

private const val TAG = "ShopFragment"

class ShopFragment : Fragment() {

    private val homeViewModel: HomeViewModel by navGraphViewModels(R.id.home_navigation)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        // Get a reference to the binding object and inflate the fragment views.
        val binding = FragmentShopBinding.inflate(inflater, container, false)

        // Sets the adapter of the RecyclerView with click listener.
        val adapter =
            ShopAdapter(ShopListener {
                homeViewModel.addItem(it)
                Toast.makeText(activity, "Added ${it.name} to cart", Toast.LENGTH_SHORT).show()
            })
        binding.itemsList.adapter = adapter

        // Specify the current activity as the lifecycle owner of the binding.
        // This is necessary so that the binding can observe LiveData updates.
        binding.lifecycleOwner = viewLifecycleOwner

        homeViewModel.items.observe(viewLifecycleOwner, Observer {
            // Show empty shop layout if not null or empty.
            binding.hasItems = !it.isNullOrEmpty()
            it?.let {
                adapter.submitList(it)
            }
        })

        binding.logoutButton.setOnClickListener {
            this.findNavController().navigateUp()
        }

        binding.itemsList.addItemDecoration(
            GridMarginItemDecoration(
                resources.getDimension(R.dimen.card_margin_grid).toInt()
            )
        )

        setHasOptionsMenu(true)

        return binding.root
    }
}
