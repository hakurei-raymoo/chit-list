package gensokyo.hakurei.chitlist.transactiondetail

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import gensokyo.hakurei.chitlist.database.AppDatabase
import gensokyo.hakurei.chitlist.databinding.FragmentTransactionDetailBinding

private const val TAG = "TXDetailFragment"

class TransactionDetailFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        // Get a reference to the binding object and inflate the fragment views.
        val binding = FragmentTransactionDetailBinding.inflate(inflater, container, false)

        val application = requireNotNull(this.activity).application
        val arguments = TransactionDetailFragmentArgs.fromBundle(arguments!!)

        // Create an instance of the ViewModel Factory.
        val dataSource = AppDatabase.getInstance(application).transactionDao
        val viewModelFactory = TransactionDetailViewModelFactory(arguments.transactionKey, dataSource)

        // Get a reference to the ViewModel associated with this fragment.
        val transactionDetailViewModel =
            ViewModelProviders.of(this, viewModelFactory).get(TransactionDetailViewModel::class.java)

        // To use the View Model with data binding, you have to explicitly
        // give the binding object a reference to it.
        binding.transactionDetailViewModel = transactionDetailViewModel

        // Specify the current activity as the lifecycle owner of the binding.
        // This is necessary so that the binding can observe LiveData updates.
        binding.lifecycleOwner = viewLifecycleOwner

        // Add an Observer to the state variable for Navigating when a Submit button is tapped.
        transactionDetailViewModel.navigateToTransactionsList.observe(viewLifecycleOwner, Observer {
            if (it == true) { // Observed state is true.
                val inputMethodManager =
                    activity?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager

                // Hide the keyboard.
                inputMethodManager.hideSoftInputFromWindow(view?.windowToken, 0)

                this.findNavController().navigate(
                    TransactionDetailFragmentDirections.actionTransactionDetailFragmentToTransactionsListFragment()
                )
                // Reset state to make sure we only navigate once, even if the device
                // has a configuration change.
                transactionDetailViewModel.doneNavigating()
            }
        })

        // Test Observer to report changes on item.
        // TODO: Remove after testing.
        transactionDetailViewModel.publicTransaction.observe(viewLifecycleOwner, Observer {
            Log.i(TAG, "Observed ${transactionDetailViewModel.publicTransaction.value}")
        })

        return binding.root
    }
}
