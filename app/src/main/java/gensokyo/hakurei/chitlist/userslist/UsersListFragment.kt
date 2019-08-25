package gensokyo.hakurei.chitlist.userslist

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import gensokyo.hakurei.chitlist.R
import gensokyo.hakurei.chitlist.database.UsersDatabase
import gensokyo.hakurei.chitlist.databinding.FragmentUsersListBinding

private const val TAG = "UsersListFragment"

class UsersListFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        Log.i(TAG, "onCreateView called")

        // Get a reference to the binding object and inflate the fragment views.
        val binding: FragmentUsersListBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_users_list, container, false)

        val application = requireNotNull(this.activity).application

        // Create an instance of the ViewModel Factory.
        val dataSource = UsersDatabase.getInstance(application).usersDatabaseDao
        val viewModelFactory = UsersListViewModelFactory(dataSource, application)

        // Get a reference to the ViewModel associated with this fragment.
        val usersListViewModel =
            ViewModelProviders.of(
                this, viewModelFactory).get(UsersListViewModel::class.java)

        // To use the View Model with data binding, you have to explicitly
        // give the binding object a reference to it.
        binding.usersListViewModel = usersListViewModel

        val adapter = UserAdaptor(UserListener { userId ->
//            Toast.makeText(context, "${userId}", Toast.LENGTH_LONG).show()
            usersListViewModel.onEditUserClicked(userId)
        })
        binding.usersList.adapter = adapter

        usersListViewModel.users.observe(viewLifecycleOwner, Observer {
            it?.let {
                adapter.submitList(it)
            }
        })

        // Specify the current activity as the lifecycle owner of the binding.
        // This is necessary so that the binding can observe LiveData updates.
        binding.setLifecycleOwner(this)

        // Add an Observer on the state variable for Navigating when EDIT button is pressed.
        usersListViewModel.navigateToEditUser.observe(this, Observer { user ->
            user?.let {
                this.findNavController().navigate(
                    UsersListFragmentDirections
                        .actionUsersListFragmentToUserDetailFragment(user))
                usersListViewModel.onEditUserNavigated()
            }
        })

        return binding.root
}
}
