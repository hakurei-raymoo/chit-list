package gensokyo.hakurei.chitlist.login

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import gensokyo.hakurei.chitlist.SharedViewModel
import gensokyo.hakurei.chitlist.database.AppDatabase
import gensokyo.hakurei.chitlist.databinding.FragmentLoginBinding

private const val TAG = "LoginFragment"

class LoginFragment : Fragment() {

    private lateinit var sharedViewModel: SharedViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        // Get a reference to the binding object and inflate the fragment views.
        val binding = FragmentLoginBinding.inflate(inflater, container, false)

        activity?.let {
            sharedViewModel = ViewModelProviders.of(it).get(SharedViewModel::class.java)
        }

        // Get a reference to the input method manager to allow hiding the keyboard.
        val inputMethodManager =
            activity?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager

        // Create an instance of the ViewModel Factory.
        val application = requireNotNull(this.activity).application
        val dataSource = AppDatabase.getInstance(application).loginDao
        val viewModelFactory = LoginViewModelFactory(dataSource)

        // Get a reference to the ViewModel associated with this fragment.
        val loginViewModel =
            ViewModelProviders.of(this, viewModelFactory).get(LoginViewModel::class.java)

        // To use the View Model with data binding, you have to explicitly
        // give the binding object a reference to it.
        binding.loginViewModel = loginViewModel

        // Specify the current activity as the lifecycle owner of the binding.
        // This is necessary so that the binding can observe LiveData updates.
        binding.setLifecycleOwner(this)

        // Add an Observer on the state variable for Navigating when LOGIN button is pressed.
        loginViewModel.navigateToHome.observe(this, Observer {
            // Observed state is true.
            if (it == true) {
                // Hide the keyboard.
                inputMethodManager.hideSoftInputFromWindow(view?.windowToken, 0)

                this.findNavController().navigate(
                    LoginFragmentDirections.actionLoginFragmentToHomeViewPagerFragment()
                )

                // Reset state to make sure we only navigate once.
                loginViewModel.onHomeNavigated()
            }
        })

        loginViewModel.accounts.observe(this, Observer {
            Log.i(TAG, "Observed accounts=$it")

            if (it != null) {
                loginViewModel.formatAccounts(it)

                // Get a reference to the AutoCompleteTextView in the layout.
                val accountAutocomplete = binding.accountAutocomplete
                // Create the adapter and set it to the AutoCompleteTextView.
                val accountsAdaptor = ArrayAdapter<String>(
                    requireContext(),
                    android.R.layout.simple_list_item_1,
                    loginViewModel.accountNames
                )
                accountAutocomplete.setAdapter(accountsAdaptor)
            }
        })

        // Observer to process login attempt once account returned.
        loginViewModel.account.observe(this, Observer {
            Log.i(TAG, "Observed account=$it")

            if (it != null) {
                sharedViewModel.login(it)
                loginViewModel.onNavigateToHome()
                Toast.makeText(activity, "Logged in as ${it.firstName} ${it.lastName}.", Toast.LENGTH_SHORT).show()
            }
            loginViewModel.enableInput.value = true
        })

        return binding.root
    }
}
