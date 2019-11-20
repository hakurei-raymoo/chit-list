package gensokyo.hakurei.chitlist.payaccount

import android.content.Context
import android.graphics.Typeface
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.Spanned
import android.text.style.StyleSpan
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import gensokyo.hakurei.chitlist.R
import gensokyo.hakurei.chitlist.database.*
import gensokyo.hakurei.chitlist.databinding.FragmentPayAccountBinding
import gensokyo.hakurei.chitlist.utilities.Converter

private const val TAG = "PayAccountFragment"

class PayAccountFragment : DialogFragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        // Get a reference to the binding object and inflate the fragment views.
        val binding = FragmentPayAccountBinding.inflate(inflater, container, false)

        // Create an instance of the ViewModel Factory.
        val application = requireNotNull(this.activity).application
        val arguments = PayAccountFragmentArgs.fromBundle(arguments!!)
        val dataSource = AppDatabase.getInstance(application).transactionDao
        val viewModelFactory = PayAccountViewModelFactory(arguments.creatorId, dataSource)

        // Get a reference to the ViewModel associated with this fragment.
        val payAccountViewModel =
            ViewModelProviders.of(this, viewModelFactory).get(PayAccountViewModel::class.java)

        // To use the View Model with data binding, you have to explicitly
        // give the binding object a reference to it.
        binding.payAccountViewModel = payAccountViewModel

        // Specify the current activity as the lifecycle owner of the binding.
        // This is necessary so that the binding can observe LiveData updates.
        binding.lifecycleOwner = viewLifecycleOwner

        // Populate account AutoCompleteTextView with accounts.
        payAccountViewModel.accountsList.observe(this, Observer {
            Log.i(TAG, "Observed accounts=$it")

            if (it != null) {
                // Get a reference to the AutoCompleteTextView in the layout.
                val accountAutocomplete = binding.accountAutocomplete
                // Create the adapter and set it to the AutoCompleteTextView.
                val accountsAdaptor = ArrayAdapter<String>(
                    requireContext(),
                    android.R.layout.simple_list_item_1,
                    it
                )
                accountAutocomplete.setAdapter(accountsAdaptor)
            }
        })

        // Populate item AutoCompleteTextView with items.
        payAccountViewModel.itemsList.observe(this, Observer {
            Log.i(TAG, "Observed items=$it")

            if (it != null) {
                // Get a reference to the AutoCompleteTextView in the layout.
                val accountAutocomplete = binding.itemAutocomplete
                // Create the adapter and set it to the AutoCompleteTextView.
                val accountsAdaptor = ArrayAdapter<String>(
                    requireContext(),
                    android.R.layout.simple_list_item_1,
                    it
                )
                accountAutocomplete.setAdapter(accountsAdaptor)
            }
        })

        binding.createButton.setOnClickListener {
            // Hide the keyboard.
            val inputMethodManager =
                activity?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            inputMethodManager.hideSoftInputFromWindow(view?.windowToken, 0)

            val message = createTransactionMessage(payAccountViewModel.linkedAccount.value!!,
                payAccountViewModel.linkedItem.value!!,
                payAccountViewModel.transaction.value!!)

            MaterialAlertDialogBuilder(context).setTitle(getString(R.string.pay_account))
                .setMessage(message)
                .setPositiveButton(getString(R.string.create)) { _, _ -> payAccountViewModel.insert() }
                .setNegativeButton(getString(R.string.cancel), null)
                .show()
        }

        binding.cancelButton.setOnClickListener {
            this.findNavController().navigateUp()
        }

        // Add an Observer to the state variable for Navigating when a Submit button is tapped.
        payAccountViewModel.transactionInserted.observe(viewLifecycleOwner, Observer {
            // Observed state is true.
            if (it == true) {
                this.findNavController().navigateUp()

                Toast.makeText(activity, getString(R.string.transaction_created), Toast.LENGTH_SHORT).show()

                // Reset state to make sure we only navigate once.
                payAccountViewModel.doneNavigating()
            }
        })

        // Update linked properties on text change.
        binding.accountAutocomplete.addTextChangedListener { text ->
            payAccountViewModel.updateLinkedAccount(text.toString())
        }
        binding.itemAutocomplete.addTextChangedListener { text ->
            payAccountViewModel.updateLinkedItem(text.toString())
        }

        // Update error on database response.
        payAccountViewModel.linkedAccount.observe(viewLifecycleOwner, Observer {
            binding.accountInput.hint = if (it == null) getString(R.string.invalid_account) else getString(R.string.account)
            Log.i(TAG, "linkedAccount=$it")
            payAccountViewModel.updateEnableInput()
        })
        payAccountViewModel.linkedItem.observe(viewLifecycleOwner, Observer {
            binding.itemInput.hint = if (it == null) getString(R.string.invalid_item) else getString(R.string.item)
            Log.i(TAG, "linkedItem=$it")
            payAccountViewModel.updateEnableInput()
        })

        return binding.root
    }

    private fun createTransactionMessage(account: BareAccount, item: BareItem, transaction: Transaction): Spannable {
        return SpannableStringBuilder().apply {
            append(getString(R.string.create_transaction_warning))
            append("\n")
            append("\n")
            append(getString(R.string.account), StyleSpan(Typeface.BOLD), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
            append("\n")
            append(getString(R.string.number_and_full_name, account.accountId, account.firstName, account.lastName))
            append("\n")
            append(getString(R.string.item), StyleSpan(Typeface.BOLD), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
            append("\n")
            append(getString(R.string.number_and_name, item.itemId, item.name))
            append("\n")
            append(getString(R.string.amount), StyleSpan(Typeface.BOLD), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
            append("\n")
            append(Converter.addDecimal(-transaction.amount!!))
            append("\n")
            append(getString(R.string.comments), StyleSpan(Typeface.BOLD), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
            append("\n")
            append(transaction.comments!!)
        }
    }
}
