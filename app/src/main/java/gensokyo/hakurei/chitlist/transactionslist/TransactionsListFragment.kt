package gensokyo.hakurei.chitlist.transactionslist

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import gensokyo.hakurei.chitlist.R
import gensokyo.hakurei.chitlist.database.AppDatabase
import gensokyo.hakurei.chitlist.databinding.FragmentTransactionsListBinding

private const val TAG = "TXsListFragment"

class TransactionsListFragment : Fragment() {

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

        val adapter = TransactionAdaptor(TransactionListener { transactionId ->
            transactionsListViewModel.onEditTransactionClicked(transactionId)
        })
        binding.transactionsList.adapter = adapter

        transactionsListViewModel.displayTransactions.observe(viewLifecycleOwner, Observer {
            it?.let {
                adapter.submitList(it)
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

        // Add an Observer to the LiveData when the Search button is tapped.
        transactionsListViewModel.publicDisplaySearch.observe(this, Observer {
            // Hide the keyboard.
            inputMethodManager.hideSoftInputFromWindow(view?.windowToken, 0)
        })

        // Get a reference to the AutoCompleteTextView in the layout.
        val searchAutocomplete = binding.searchAutocomplete as AutoCompleteTextView
        // Create the adapter and set it to the AutoCompleteTextView.
        val accountsAdaptor = ArrayAdapter<String>(
            requireContext(),
            android.R.layout.simple_list_item_1,
            transactionsListViewModel.accountNames
        )
        searchAutocomplete.setAdapter(accountsAdaptor)


        return binding.root
    }
}
