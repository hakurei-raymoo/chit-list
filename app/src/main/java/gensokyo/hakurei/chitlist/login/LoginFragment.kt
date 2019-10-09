package gensokyo.hakurei.chitlist.login

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import gensokyo.hakurei.chitlist.R
import gensokyo.hakurei.chitlist.database.AppDatabase
import gensokyo.hakurei.chitlist.databinding.FragmentLoginBinding

private const val TAG = "LoginFragment"

class LoginFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        // Get a reference to the binding object and inflate the fragment views.
        val binding: FragmentLoginBinding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_login, container, false)

        val application = requireNotNull(this.activity).application

        // Get a reference to the input method manager to allow hiding the keyboard.
        val inputMethodManager =
            activity?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager

        // Create an instance of the ViewModel Factory.
        val dataSource = AppDatabase.getInstance(application).accountDao
        val viewModelFactory = LoginViewModelFactory(dataSource, application)

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
        loginViewModel.navigateToHome.observe(this, Observer { accountId ->
            accountId?.let {
                // Hide the keyboard.
                inputMethodManager.hideSoftInputFromWindow(view?.windowToken, 0)

                this.findNavController().navigate(
                    LoginFragmentDirections.actionLoginFragmentToHomeFragment(accountId)
                )
                loginViewModel.onHomeNavigated()
            }
        })

        // Observer to process login attempt once account returned.
        loginViewModel.account.observe(this, Observer {
            Log.i(TAG, "Observed ${loginViewModel.account.value}")
            loginViewModel.checkCredentials()
        })

        return binding.root
    }
}