package gensokyo.hakurei.chitlist.checkout

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import gensokyo.hakurei.chitlist.GridMarginItemDecoration
import gensokyo.hakurei.chitlist.R
import gensokyo.hakurei.chitlist.SharedViewModel
import gensokyo.hakurei.chitlist.adminviewpager.AdminViewPagerFragmentDirections
import gensokyo.hakurei.chitlist.database.AppDatabase
import gensokyo.hakurei.chitlist.databinding.FragmentShopBinding
import gensokyo.hakurei.chitlist.itemslist.ItemAdapter
import gensokyo.hakurei.chitlist.itemslist.ItemListener
import gensokyo.hakurei.chitlist.shop.ShopViewModel
import gensokyo.hakurei.chitlist.shop.ShopViewModelFactory

private const val TAG = "CheckoutFragment"

class CheckoutFragment : Fragment() {

    private lateinit var sharedViewModel: SharedViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        // Get a reference to the binding object and inflate the fragment views.
        val binding = FragmentShopBinding.inflate(inflater, container, false)

        activity?.let {
            sharedViewModel = ViewModelProviders.of(it).get(SharedViewModel::class.java)
        }

        // Create an instance of the ViewModel Factory.
        val application = requireNotNull(this.activity).application
        val dataSource = AppDatabase.getInstance(application).shopDao
        val viewModelFactory = ShopViewModelFactory(dataSource, application)

        // Get a reference to the ViewModel associated with this fragment.
        val shopViewModel =
            ViewModelProviders.of(this, viewModelFactory).get(ShopViewModel::class.java)

        // To use the View Model with data binding, you have to explicitly
        // give the binding object a reference to it.
        binding.shopViewModel = shopViewModel

        val adapter = ItemAdapter(ItemListener { itemId ->
            sharedViewModel.addItem(itemId)
        })
        binding.itemsList.adapter = adapter

        // Specify the current activity as the lifecycle owner of the binding.
        // This is necessary so that the binding can observe LiveData updates.
        binding.lifecycleOwner = viewLifecycleOwner

        shopViewModel.items.observe(viewLifecycleOwner, Observer {
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
