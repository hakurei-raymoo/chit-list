package gensokyo.hakurei.chitlist.transactionslist

import android.content.Context
import android.os.Bundle
import android.view.*
import android.view.inputmethod.InputMethodManager
import android.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import gensokyo.hakurei.chitlist.database.AppDatabase
import gensokyo.hakurei.chitlist.databinding.FragmentTransactionsListBinding
import gensokyo.hakurei.chitlist.MarginItemDecoration
import gensokyo.hakurei.chitlist.adminviewpager.AdminViewPagerFragmentDirections
import gensokyo.hakurei.chitlist.R
import gensokyo.hakurei.chitlist.SharedViewModel

private const val TAG = "TXsListFragment"

class TransactionsListFragment : Fragment() {

    private lateinit var sharedViewModel: SharedViewModel
    private lateinit var transactionsListViewModel: TransactionsListViewModel

    lateinit var adapter: TransactionAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        // Get a reference to the binding object and inflate the fragment views.
        val binding = FragmentTransactionsListBinding.inflate(inflater, container, false)

        // Get a reference to the input method manager to allow hiding the keyboard.
        val inputMethodManager =
            activity?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager

        // Create an instance of the ViewModel Factory.
        val application = requireNotNull(this.activity).application
        val dataSource = AppDatabase.getInstance(application).transactionDao
        val viewModelFactory = TransactionsListViewModelFactory(dataSource)

        // Get a reference to the ViewModel associated with this fragment.
        activity?.let {
            sharedViewModel = ViewModelProviders.of(it).get(SharedViewModel::class.java)
            transactionsListViewModel =
                ViewModelProviders.of(it, viewModelFactory).get(TransactionsListViewModel::class.java)
        }

        // To use the View Model with data binding, you have to explicitly
        // give the binding object a reference to it.
        binding.transactionsListViewModel = transactionsListViewModel

        adapter = TransactionAdapter(TransactionListener { transactionId ->
            transactionsListViewModel.onTransactionClicked(transactionId)
        })
        binding.transactionsList.adapter = adapter

        // Specify the current activity as the lifecycle owner of the binding.
        // This is necessary so that the binding can observe LiveData updates.
        binding.lifecycleOwner = viewLifecycleOwner

        transactionsListViewModel.transactions.observe(viewLifecycleOwner, Observer {
            it?.let {
                adapter.data = it
                adapter.getFilter().filter("") // Initial call to filter data to display.
            }
        })

        // Add an Observer on the state variable for Navigating when EDIT button is pressed.
        transactionsListViewModel.navigateToEditTransaction.observe(viewLifecycleOwner, Observer { transaction ->
            transaction?.let {
                // Hide the keyboard.
                inputMethodManager.hideSoftInputFromWindow(view?.windowToken, 0)

                this.findNavController().navigate(
                    AdminViewPagerFragmentDirections.actionAdminViewPagerFragmentToTransactionDetailFragment(sharedViewModel.user?.accountId!!, transaction)
                )
                transactionsListViewModel.onTransactionNavigated()
            }
        })

        binding.transactionsList.addItemDecoration(
            MarginItemDecoration(
                resources.getDimension(R.dimen.card_margin_linear).toInt()
            )
        )

        setHasOptionsMenu(true)

        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.search_menu, menu)

        val search = menu.findItem(R.id.search)
        val searchView = search.actionView as SearchView
        search(searchView)
        searchView.queryHint = getString(R.string.account_name)
    }

    private fun search(searchView: SearchView) {
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                searchView.clearFocus()
                return false
            }

            override fun onQueryTextChange(newText: String): Boolean {
                adapter.getFilter().filter(newText)
                return true
            }
        })
    }
}
