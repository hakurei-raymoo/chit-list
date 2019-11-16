package gensokyo.hakurei.chitlist.editconfig

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import gensokyo.hakurei.chitlist.R
import gensokyo.hakurei.chitlist.databinding.FragmentEditConfigBinding

private const val TAG = "EditConfigFragment"

const val SELECT_APP_LOGO_REQUEST_CODE = 101

class EditConfigFragment : DialogFragment() {

    private lateinit var editConfigViewModel: EditConfigViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        // Get a reference to the binding object and inflate the fragment views.
        val binding = FragmentEditConfigBinding.inflate(inflater, container, false)

        // Get a reference to the ViewModel associated with this fragment.
        editConfigViewModel =
            ViewModelProviders.of(this).get(EditConfigViewModel::class.java)

        // To use the View Model with data binding, you have to explicitly
        // give the binding object a reference to it.
        binding.editConfigViewModel = editConfigViewModel

        // Specify the current activity as the lifecycle owner of the binding.
        // This is necessary so that the binding can observe LiveData updates.
        binding.lifecycleOwner = viewLifecycleOwner

        binding.selectAppLogoButton.setOnClickListener {
            val intent = Intent(Intent.ACTION_OPEN_DOCUMENT).apply {
                addCategory(Intent.CATEGORY_OPENABLE)
                flags = Intent.FLAG_GRANT_READ_URI_PERMISSION or Intent.FLAG_GRANT_WRITE_URI_PERMISSION or Intent.FLAG_GRANT_PERSISTABLE_URI_PERMISSION
                type = "image/*"
            }

            startActivityForResult(intent, SELECT_APP_LOGO_REQUEST_CODE)
        }

        // Set initial state.
        when (editConfigViewModel.decimalSeparator.value) {
            "." -> binding.pointButton.isChecked = true
            "," -> binding.commaButton.isChecked = true
        }

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

        // Set initial state.
        when (editConfigViewModel.decimalOffset.value) {
            1 -> binding.oneButton.isChecked = true
            2 -> binding.twoButton.isChecked = true
            3 -> binding.threeButton.isChecked = true
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

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == Activity.RESULT_OK) {
            if (data != null && data.data != null) {
                when (requestCode) {
                    SELECT_APP_LOGO_REQUEST_CODE -> {
                        requireActivity().contentResolver.takePersistableUriPermission(data.data!!, Intent.FLAG_GRANT_READ_URI_PERMISSION)
                        editConfigViewModel.changeAppLogo(data.data!!)
                    }
                }
            }
        }
    }
}
