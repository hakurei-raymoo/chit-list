package gensokyo.hakurei.chitlist.history

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import gensokyo.hakurei.chitlist.MarginItemDecoration
import gensokyo.hakurei.chitlist.R
import gensokyo.hakurei.chitlist.SharedViewModel
import gensokyo.hakurei.chitlist.database.AppDatabase
import gensokyo.hakurei.chitlist.databinding.FragmentHistoryBinding

private const val TAG = "HistoryFragment"

class HistoryFragment : Fragment() {

    private lateinit var sharedViewModel: SharedViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        // Get a reference to the binding object and inflate the fragment views.
        val binding = FragmentHistoryBinding.inflate(inflater, container, false)

        activity?.let {
            sharedViewModel = ViewModelProviders.of(it).get(SharedViewModel::class.java)
        }

        // Create an instance of the ViewModel Factory.
        val application = requireNotNull(this.activity).application
        val dataSource = AppDatabase.getInstance(application).shopDao
        val viewModelFactory = HistoryViewModelFactory(dataSource)

        // Get a reference to the ViewModel associated with this fragment.
        val historyViewModel =
            ViewModelProviders.of(this, viewModelFactory).get(HistoryViewModel::class.java)

        // To use the View Model with data binding, you have to explicitly
        // give the binding object a reference to it.
        binding.historyViewModel = historyViewModel

        // Specify the current activity as the lifecycle owner of the binding.
        // This is necessary so that the binding can observe LiveData updates.
        binding.lifecycleOwner = viewLifecycleOwner

        val adapter = HistoryAdapter(HistoryListener {
//            historyViewModel.onHistoryClicked(transactionId)
        })
        binding.transactionsList.adapter = adapter

        historyViewModel.updateAccount(sharedViewModel.user?.accountId!!)

        historyViewModel.accountId.observe(viewLifecycleOwner, Observer {
            Log.i(TAG, "accountId=$it")
            it?.let {
                historyViewModel.updateHistory()
            }
        })

        historyViewModel.history.observe(viewLifecycleOwner, Observer {
            Log.i(TAG, "Observed cart=$it")
            it?.let {
                historyViewModel.updateBalance()
                sharedViewModel.balance.postValue(historyViewModel.balance.value)
                adapter.submitList(it)
            }
        })

        binding.transactionsList.addItemDecoration(
            MarginItemDecoration(
                resources.getDimension(R.dimen.card_margin_linear).toInt()
            )
        )

        return binding.root
    }
}
