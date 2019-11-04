package gensokyo.hakurei.chitlist

import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.navGraphViewModels
import gensokyo.hakurei.chitlist.utilities.GridMarginItemDecoration
import gensokyo.hakurei.chitlist.adapters.ShopAdapter
import gensokyo.hakurei.chitlist.adapters.ShopListener
import gensokyo.hakurei.chitlist.viewmodels.HomeViewModel
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
                Toast.makeText(activity, "Added ${it.name} to cart.", Toast.LENGTH_SHORT).apply {
                    setGravity(Gravity.CENTER, 0, 0)
                    show()
                }
            })
        binding.itemsList.adapter = adapter

        // Specify the current activity as the lifecycle owner of the binding.
        // This is necessary so that the binding can observe LiveData updates.
        binding.lifecycleOwner = viewLifecycleOwner

        homeViewModel.items.observe(viewLifecycleOwner, Observer {
            it?.let {
                adapter.submitList(it)
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
}
