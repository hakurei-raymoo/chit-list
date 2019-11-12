package gensokyo.hakurei.chitlist

import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import gensokyo.hakurei.chitlist.databinding.FragmentEditConfigBinding
import gensokyo.hakurei.chitlist.viewmodels.EditConfigViewModel

private const val TAG = "EditConfigFragment"

class EditConfigFragment : DialogFragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        // Get a reference to the binding object and inflate the fragment views.
        val binding = FragmentEditConfigBinding.inflate(inflater, container, false)

        // Get a reference to the ViewModel associated with this fragment.
        val editConfigViewModel =
            ViewModelProviders.of(this).get(EditConfigViewModel::class.java)

        // To use the View Model with data binding, you have to explicitly
        // give the binding object a reference to it.
        binding.editConfigViewModel = editConfigViewModel

        // Specify the current activity as the lifecycle owner of the binding.
        // This is necessary so that the binding can observe LiveData updates.
        binding.lifecycleOwner = viewLifecycleOwner

        // Set decimal separator symbol.
        binding.pointButton.setOnClickListener {
            binding.pointButton.isChecked = true
            binding.commaButton.isChecked = false
            editConfigViewModel.decimalSeparator.value = "."
        }
        binding.commaButton.setOnClickListener {
            binding.pointButton.isChecked = false
            binding.commaButton.isChecked = true
            editConfigViewModel.decimalSeparator.value = ","
        }

        // Set decimal separator offset.
        binding.oneButton.setOnClickListener {
            binding.oneButton.isChecked = true
            binding.twoButton.isChecked = false
            binding.threeButton.isChecked = false
            editConfigViewModel.decimalOffset.value = 1
        }
        binding.twoButton.setOnClickListener {
            binding.oneButton.isChecked = false
            binding.twoButton.isChecked = true
            binding.threeButton.isChecked = false
            editConfigViewModel.decimalOffset.value = 2
        }
        binding.threeButton.setOnClickListener {
            binding.oneButton.isChecked = false
            binding.twoButton.isChecked = false
            binding.threeButton.isChecked = true
            editConfigViewModel.decimalOffset.value = 3
        }

        binding.updateButton.setOnClickListener {
            editConfigViewModel.write()

            this.findNavController().navigateUp()

            Toast.makeText(activity, getString(R.string.config_saved), Toast.LENGTH_LONG).show()
        }

        binding.cancelButton.setOnClickListener {
            this.findNavController().navigateUp()
        }

        return binding.root
    }
}
