package gensokyo.hakurei.chitlist.history

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.navGraphViewModels
import gensokyo.hakurei.chitlist.R
import gensokyo.hakurei.chitlist.utilities.MarginItemDecoration
import gensokyo.hakurei.chitlist.home.HomeViewModel
import gensokyo.hakurei.chitlist.databinding.FragmentHistoryBinding

private const val TAG = "HistoryFragment"

class HistoryFragment : Fragment() {

    private val homeViewModel: HomeViewModel by navGraphViewModels(R.id.home_navigation)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        // Get a reference to the binding object and inflate the fragment views.
        val binding = FragmentHistoryBinding.inflate(inflater, container, false)

        // Specify the current activity as the lifecycle owner of the binding.
        // This is necessary so that the binding can observe LiveData updates.
        binding.lifecycleOwner = viewLifecycleOwner

        // Sets the adapter of the RecyclerView with click listener.
        val adapter =
            HistoryAdapter(HistoryListener {
                homeViewModel.onHistoryClicked(it)
            })
        binding.transactionsList.adapter = adapter

        homeViewModel.history.observe(viewLifecycleOwner, Observer {
            Log.i(TAG, "Observed history=$it")
            // Show empty history layout if not null or empty.
            binding.hasHistory = !it.isNullOrEmpty()
            it?.let {
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
