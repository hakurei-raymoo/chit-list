package gensokyo.hakurei.chitlist.checkout

import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import androidx.viewpager2.widget.ViewPager2
import gensokyo.hakurei.chitlist.GridMarginItemDecoration
import gensokyo.hakurei.chitlist.R
import gensokyo.hakurei.chitlist.SharedViewModel
import gensokyo.hakurei.chitlist.database.AppDatabase
import gensokyo.hakurei.chitlist.databinding.FragmentCheckoutBinding
import gensokyo.hakurei.chitlist.homeviewpager.SHOP_PAGE_INDEX
import gensokyo.hakurei.chitlist.shop.ShopAdapter
import gensokyo.hakurei.chitlist.shop.ShopListener

private const val TAG = "CheckoutFragment"

class CheckoutFragment : Fragment() {

    private lateinit var sharedViewModel: SharedViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        // Get a reference to the binding object and inflate the fragment views.
        val binding = FragmentCheckoutBinding.inflate(inflater, container, false)

        activity?.let {
            sharedViewModel = ViewModelProviders.of(it).get(SharedViewModel::class.java)
        }

        // Create an instance of the ViewModel Factory.
        val application = requireNotNull(this.activity).application
        val dataSource = AppDatabase.getInstance(application).shopDao
        val viewModelFactory = CheckoutViewModelFactory(dataSource)

        // Get a reference to the ViewModel associated with this fragment.
        val checkoutViewModel =
            ViewModelProviders.of(this, viewModelFactory).get(CheckoutViewModel::class.java)

        // To use the View Model with data binding, you have to explicitly
        // give the binding object a reference to it.
        binding.checkoutViewModel = checkoutViewModel

        val adapter = ShopAdapter(ShopListener {
            sharedViewModel.removeItem(it)
            Toast.makeText(activity, "Removed ${it.name} from cart.", Toast.LENGTH_SHORT).show()
        })
        binding.itemsList.adapter = adapter

        // Specify the current activity as the lifecycle owner of the binding.
        // This is necessary so that the binding can observe LiveData updates.
        binding.lifecycleOwner = viewLifecycleOwner

        sharedViewModel.cart.observe(viewLifecycleOwner, Observer {
            // Show layout if not null or empty.
            binding.hasItems = !it.isNullOrEmpty()
            it?.let {
                it.forEach {
                    Log.i(TAG, "Observed cart=$it")
                }
                adapter.submitList(it)
                adapter.notifyDataSetChanged()
            }
        })

        binding.addItem.setOnClickListener {
            navigateToShopPage()
        }

        // Add an Observer on the state variable for Navigating when EDIT button is pressed.
        checkoutViewModel.navigateToCheckout.observe(viewLifecycleOwner, Observer {
            if (it == true) {
                checkoutViewModel.checkout(sharedViewModel.user!!, sharedViewModel.cart.value)

                this.findNavController().navigateUp()

                checkoutViewModel.onCheckoutNavigated()
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
        requireActivity().findViewById<ViewPager2>(R.id.view_pager).currentItem = SHOP_PAGE_INDEX
    }
}
