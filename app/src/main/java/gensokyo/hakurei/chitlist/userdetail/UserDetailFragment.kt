package gensokyo.hakurei.chitlist.userdetail

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
import gensokyo.hakurei.chitlist.database.UsersDatabase
import gensokyo.hakurei.chitlist.databinding.FragmentUserDetailBinding

private const val TAG = "UserDetailFragment"

class UserDetailFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        Log.i(TAG, "onCreateView called")

        // Get a reference to the binding object and inflate the fragment views.
        val binding: FragmentUserDetailBinding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_user_detail, container, false
        )

        val application = requireNotNull(this.activity).application
        val arguments = UserDetailFragmentArgs.fromBundle(arguments!!)

        // Create an instance of the ViewModel Factory.
        val dataSource = UsersDatabase.getInstance(application).usersDatabaseDao
        val viewModelFactory = UserDetailViewModelFactory(arguments.userKey, dataSource)

        // Get a reference to the ViewModel associated with this fragment.
        val userDetailViewModel =
            ViewModelProviders.of(this, viewModelFactory).get(UserDetailViewModel::class.java)

        // To use the View Model with data binding, you have to explicitly
        // give the binding object a reference to it.
        binding.userDetailViewModel = userDetailViewModel

        binding.setLifecycleOwner(this)

        // Add an Observer to the state variable for Navigating when a Submit button is tapped.
        userDetailViewModel.navigateToUsersList.observe(this, Observer {
            if (it == true) { // Observed state is true.
                // Hide the keyboard.
                val inputMethodManager = activity?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                inputMethodManager.hideSoftInputFromWindow(view?.windowToken, 0)

                this.findNavController().navigate(
                    UserDetailFragmentDirections.actionUserDetailFragmentToUsersListFragment()
                )
                // Reset state to make sure we only navigate once, even if the device
                // has a configuration change.
                userDetailViewModel.doneNavigating()
            }
        })

        return binding.root
    }
}
