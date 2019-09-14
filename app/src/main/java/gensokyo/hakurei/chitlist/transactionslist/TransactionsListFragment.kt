package gensokyo.hakurei.chitlist.transactionslist

import android.content.Context
import android.os.Bundle
import android.view.*
import android.view.inputmethod.InputMethodManager
import android.widget.SearchView
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import gensokyo.hakurei.chitlist.database.AppDatabase
import gensokyo.hakurei.chitlist.databinding.FragmentTransactionsListBinding
import androidx.navigation.findNavController
import androidx.navigation.ui.NavigationUI
import gensokyo.hakurei.chitlist.R


private const val TAG = "TXsListFragment"

class TransactionsListFragment : Fragment() {

    lateinit var adapter: TransactionAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        // Get a reference to the binding object and inflate the fragment views.
        val binding: FragmentTransactionsListBinding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_transactions_list, container, false)

        val application = requireNotNull(this.activity).application

        // Get a reference to the input method manager to allow hiding the keyboard.
        val inputMethodManager =
            activity?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager

        // Create an instance of the ViewModel Factory.
        val dataSource = AppDatabase.getInstance(application).transactionDao
        val viewModelFactory = TransactionsListViewModelFactory(dataSource, application)

        // Get a reference to the ViewModel associated with this fragment.
        val transactionsListViewModel =
            ViewModelProviders.of(this, viewModelFactory).get(TransactionsListViewModel::class.java)

        // To use the View Model with data binding, you have to explicitly
        // give the binding object a reference to it.
        binding.transactionsListViewModel = transactionsListViewModel

        adapter = TransactionAdapter(TransactionListener { transactionId ->
            transactionsListViewModel.onEditTransactionClicked(transactionId)
        })
        binding.transactionsList.adapter = adapter

        transactionsListViewModel.transactions.observe(viewLifecycleOwner, Observer {
            it?.let {
                adapter.data = it
                adapter.getFilter().filter("") // Initial call to filter data to display.
            }
        })

        // Specify the current activity as the lifecycle owner of the binding.
        // This is necessary so that the binding can observe LiveData updates.
        binding.setLifecycleOwner(this)

        // Add an Observer on the state variable for Navigating when EDIT button is pressed.
        transactionsListViewModel.navigateToEditTransaction.observe(this, Observer { transaction ->
            transaction?.let {
                // Hide the keyboard.
                inputMethodManager.hideSoftInputFromWindow(view?.windowToken, 0)

                this.findNavController().navigate(
                    TransactionsListFragmentDirections.actionTransactionsListFragmentToTransactionDetailFragment(transaction)
                )
                transactionsListViewModel.onEditTransactionNavigated()
            }
        })

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

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return NavigationUI.onNavDestinationSelected(item, view!!.findNavController())
                || super.onOptionsItemSelected(item)
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
