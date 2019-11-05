package gensokyo.hakurei.chitlist.accountdetail

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import gensokyo.hakurei.chitlist.database.AppDatabase
import gensokyo.hakurei.chitlist.databinding.FragmentAccountDetailBinding

private const val TAG = "AccountDetailFragment"

class AccountDetailFragment : DialogFragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        // Get a reference to the binding object and inflate the fragment views.
        val binding = FragmentAccountDetailBinding.inflate(inflater, container, false)

        // Create an instance of the ViewModel Factory.
        val application = requireNotNull(this.activity).application
        val arguments = AccountDetailFragmentArgs.fromBundle(arguments!!)
        val dataSource = AppDatabase.getInstance(application).accountDao
        val viewModelFactory = AccountDetailViewModelFactory(arguments.accountKey, dataSource)

        // Get a reference to the ViewModel associated with this fragment.
        val accountDetailViewModel =
            ViewModelProviders.of(this, viewModelFactory).get(AccountDetailViewModel::class.java)

        // To use the View Model with data binding, you have to explicitly
        // give the binding object a reference to it.
        binding.accountDetailViewModel = accountDetailViewModel

        // Specify the current activity as the lifecycle owner of the binding.
        // This is necessary so that the binding can observe LiveData updates.
        binding.lifecycleOwner = viewLifecycleOwner

        // Add an Observer to the state variable for Navigating when a Submit button is tapped.
        accountDetailViewModel.navigateToAccountsList.observe(viewLifecycleOwner, Observer {
            // Observed state is true.
            if (it == true) {
                // Hide the keyboard.
                val inputMethodManager =
                    activity?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                inputMethodManager.hideSoftInputFromWindow(view?.windowToken, 0)

                this.findNavController().navigateUp()

                // Reset state to make sure we only navigate once.
                accountDetailViewModel.doneNavigating()
            }
        })

        return binding.root
    }
}
