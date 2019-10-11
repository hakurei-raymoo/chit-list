package gensokyo.hakurei.chitlist.accountslist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import gensokyo.hakurei.chitlist.MarginItemDecoration
import gensokyo.hakurei.chitlist.R
import gensokyo.hakurei.chitlist.adminviewpager.AdminViewPagerFragmentDirections
import gensokyo.hakurei.chitlist.database.AppDatabase
import gensokyo.hakurei.chitlist.databinding.FragmentAccountsListBinding

private const val TAG = "AccountsListFragment"

class AccountsListFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        // Get a reference to the binding object and inflate the fragment views.
        val binding = FragmentAccountsListBinding.inflate(inflater, container, false)

        // Create an instance of the ViewModel Factory.
        val application = requireNotNull(this.activity).application
        val dataSource = AppDatabase.getInstance(application).accountDao
        val viewModelFactory = AccountsListViewModelFactory(dataSource)

        // Get a reference to the ViewModel associated with this fragment.
        val accountsListViewModel =
            ViewModelProviders.of(this, viewModelFactory).get(AccountsListViewModel::class.java)

        // To use the View Model with data binding, you have to explicitly
        // give the binding object a reference to it.
        binding.accountsListViewModel = accountsListViewModel

        // Specify the current activity as the lifecycle owner of the binding.
        // This is necessary so that the binding can observe LiveData updates.
        binding.lifecycleOwner = viewLifecycleOwner

        val adapter = AccountAdapter(AccountListener { accountId ->
            accountsListViewModel.onAccountClicked(accountId)
        })
        binding.accountsList.adapter = adapter

        accountsListViewModel.accounts.observe(viewLifecycleOwner, Observer {
            it?.let {
                adapter.submitList(it)
            }
        })

        // Add an Observer on the state variable for Navigating when EDIT button is pressed.
        accountsListViewModel.navigateToEditAccount.observe(viewLifecycleOwner, Observer { account ->
            account?.let {
                this.findNavController().navigate(
                    AdminViewPagerFragmentDirections.actionAdminViewPagerFragmentToAccountDetailFragment(account)
                )
                accountsListViewModel.onAccountNavigated()
            }
        })

        binding.accountsList.addItemDecoration(
            MarginItemDecoration(
                resources.getDimension(R.dimen.card_margin_linear).toInt()
            )
        )

        return binding.root
    }
}
