package gensokyo.hakurei.chitlist.accountslist

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
import gensokyo.hakurei.chitlist.database.AppDatabase
import gensokyo.hakurei.chitlist.databinding.FragmentAccountsListBinding

private const val TAG = "AccountsListFragment"

class AccountsListFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        Log.i(TAG, "onCreateView called")

        // Get a reference to the binding object and inflate the fragment views.
        val binding: FragmentAccountsListBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_accounts_list, container, false)

        val application = requireNotNull(this.activity).application

        // Create an instance of the ViewModel Factory.
        val dataSource = AppDatabase.getInstance(application).accountDao
        val viewModelFactory = AccountsListViewModelFactory(dataSource, application)

        // Get a reference to the ViewModel associated with this fragment.
        val accountsListViewModel =
            ViewModelProviders.of(
                this, viewModelFactory).get(AccountsListViewModel::class.java)

        // To use the View Model with data binding, you have to explicitly
        // give the binding object a reference to it.
        binding.accountsListViewModel = accountsListViewModel

        val adapter = AccountAdaptor(AccountListener { accountId ->
//            Toast.makeText(context, "${id}", Toast.LENGTH_LONG).show()
            accountsListViewModel.onEditAccountClicked(accountId)
        })
        binding.accountsList.adapter = adapter

        accountsListViewModel.accounts.observe(viewLifecycleOwner, Observer {
            it?.let {
                adapter.submitList(it)
            }
        })

        // Specify the current activity as the lifecycle owner of the binding.
        // This is necessary so that the binding can observe LiveData updates.
        binding.setLifecycleOwner(this)

        // Add an Observer on the state variable for Navigating when EDIT button is pressed.
        accountsListViewModel.navigateToEditAccount.observe(this, Observer { account ->
            account?.let {
                this.findNavController().navigate(
                    AccountsListFragmentDirections
                        .actionAccountsListFragmentToAccountDetailFragment(account))
                accountsListViewModel.onEditAccountNavigated()
            }
        })

        return binding.root
}
}
