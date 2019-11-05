package gensokyo.hakurei.chitlist.transactiondetail

import android.R
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.ArrayAdapter
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import gensokyo.hakurei.chitlist.database.AppDatabase
import gensokyo.hakurei.chitlist.databinding.FragmentTransactionDetailBinding

private const val TAG = "TXDetailFragment"

class TransactionDetailFragment : DialogFragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        // Get a reference to the binding object and inflate the fragment views.
        val binding = FragmentTransactionDetailBinding.inflate(inflater, container, false)

        // Create an instance of the ViewModel Factory.
        val application = requireNotNull(this.activity).application
        val arguments = TransactionDetailFragmentArgs.fromBundle(arguments!!)
        val dataSource = AppDatabase.getInstance(application).transactionDao
        val viewModelFactory = TransactionDetailViewModelFactory(arguments.creatorId, arguments.transactionKey, dataSource)

        // Get a reference to the ViewModel associated with this fragment.
        val transactionDetailViewModel =
            ViewModelProviders.of(this, viewModelFactory).get(TransactionDetailViewModel::class.java)

        // To use the View Model with data binding, you have to explicitly
        // give the binding object a reference to it.
        binding.transactionDetailViewModel = transactionDetailViewModel

        // Specify the current activity as the lifecycle owner of the binding.
        // This is necessary so that the binding can observe LiveData updates.
        binding.lifecycleOwner = viewLifecycleOwner

        // Populate account AutoCompleteTextView with accounts.
        transactionDetailViewModel.accountsList.observe(this, Observer {
            Log.i(TAG, "Observed accounts=$it")

            if (it != null) {
                // Get a reference to the AutoCompleteTextView in the layout.
                val accountAutocomplete = binding.accountAutocomplete
                // Create the adapter and set it to the AutoCompleteTextView.
                val accountsAdaptor = ArrayAdapter<String>(
                    requireContext(),
                    R.layout.simple_list_item_1,
                    it
                )
                accountAutocomplete.setAdapter(accountsAdaptor)
            }
        })

        // Populate item AutoCompleteTextView with items.
        transactionDetailViewModel.itemsList.observe(this, Observer {
            Log.i(TAG, "Observed items=$it")

            if (it != null) {
                // Get a reference to the AutoCompleteTextView in the layout.
                val accountAutocomplete = binding.itemAutocomplete
                // Create the adapter and set it to the AutoCompleteTextView.
                val accountsAdaptor = ArrayAdapter<String>(
                    requireContext(),
                    R.layout.simple_list_item_1,
                    it
                )
                accountAutocomplete.setAdapter(accountsAdaptor)
            }
        })

        // Add an Observer to the state variable for Navigating when a Submit button is tapped.
        transactionDetailViewModel.navigateToTransactionsList.observe(viewLifecycleOwner, Observer {
            // Observed state is true.
            if (it == true) {
                // Hide the keyboard.
                val inputMethodManager =
                    activity?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                inputMethodManager.hideSoftInputFromWindow(view?.windowToken, 0)

                this.findNavController().navigateUp()

                // Reset state to make sure we only navigate once.
                transactionDetailViewModel.doneNavigating()
            }
        })

        // Update linked properties on text change.
        binding.accountAutocomplete.addTextChangedListener { text ->
            transactionDetailViewModel.updateLinkedAccount(text.toString())
        }
        binding.itemAutocomplete.addTextChangedListener { text ->
            transactionDetailViewModel.updateLinkedItem(text.toString())
        }

        // Update error on database response.
        transactionDetailViewModel.linkedAccount.observe(viewLifecycleOwner, Observer {
            binding.accountAutocomplete.error = if (it == null) "Invalid account_id" else null
            Log.i(TAG, "linkedAccount=$it")
            transactionDetailViewModel.updateEnableInput()
        })
        transactionDetailViewModel.linkedItem.observe(viewLifecycleOwner, Observer {
            binding.itemAutocomplete.error = if (it == null) "Invalid item_id" else null
            Log.i(TAG, "linkedItem=$it")
            transactionDetailViewModel.updateEnableInput()
        })

        return binding.root
    }
}
