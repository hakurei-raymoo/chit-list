package gensokyo.hakurei.chitlist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.navigation.navGraphViewModels
import gensokyo.hakurei.chitlist.viewmodels.AdminHomeViewModel
import gensokyo.hakurei.chitlist.utilities.MarginItemDecoration
import gensokyo.hakurei.chitlist.adapters.AccountAdapter
import gensokyo.hakurei.chitlist.adapters.AccountListener
import gensokyo.hakurei.chitlist.databinding.FragmentAccountsListBinding

private const val TAG = "AccountsListFragment"

class AccountsListFragment : Fragment() {

    private val adminHomeViewModel: AdminHomeViewModel by navGraphViewModels(R.id.home_navigation)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        // Get a reference to the binding object and inflate the fragment views.
        val binding = FragmentAccountsListBinding.inflate(inflater, container, false)

        // Specify the current activity as the lifecycle owner of the binding.
        // This is necessary so that the binding can observe LiveData updates.
        binding.lifecycleOwner = viewLifecycleOwner

        // Sets the adapter of the RecyclerView with click listener.
        val adapter =
            AccountAdapter(AccountListener { accountId ->
                adminHomeViewModel.onAccountClicked(accountId)
            })
        binding.accountsList.adapter = adapter

        adminHomeViewModel.accounts.observe(viewLifecycleOwner, Observer {
            it?.let {
                adapter.submitList(it)
            }
        })

        // Add an Observer on the state variable for Navigating when EDIT button is pressed.
        adminHomeViewModel.navigateToEditAccount.observe(viewLifecycleOwner, Observer { account ->
            account?.let {
                this.findNavController().navigate(
                    AdminViewPagerFragmentDirections.actionAdminViewPagerFragmentToAccountDetailFragment(account)
                )
                adminHomeViewModel.onAccountNavigated()
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
