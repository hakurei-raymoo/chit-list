package gensokyo.hakurei.chitlist

import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.navGraphViewModels
import gensokyo.hakurei.chitlist.viewmodels.HomeViewModel
import gensokyo.hakurei.chitlist.databinding.DialogChangePasswordBinding
import gensokyo.hakurei.chitlist.utilities.hash

private const val TAG = "ChangePasswordDialog"

class ChangePasswordDialog : DialogFragment() {

    private val homeViewModel: HomeViewModel by navGraphViewModels(R.id.home_navigation)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        // Get a reference to the binding object and inflate the fragment views.
        val binding = DialogChangePasswordBinding.inflate(inflater, container, false)

        // To use the View Model with data binding, you have to explicitly
        // give the binding object a reference to it.
        binding.homeViewModel = homeViewModel

        // Specify the current activity as the lifecycle owner of the binding.
        // This is necessary so that the binding can observe LiveData updates.
        binding.lifecycleOwner = viewLifecycleOwner

        binding.okButton.setOnClickListener {
            val currentPassword = binding.currentPasswordEdit.text.toString()
            val newPassword = binding.newPasswordEdit.text.toString()
            val confirmPassword = binding.confirmPasswordEdit.text.toString()

            Log.i(TAG, currentPassword.hash())
            if (currentPassword.hash() == homeViewModel.user.value?.passwordHash) {
                if (confirmPassword == newPassword) {
                    homeViewModel.changePassword(newPassword.hash())
                    Toast.makeText(activity, "Password changed.", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(activity, "Passwords do not match.", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(activity, "Incorrect password.", Toast.LENGTH_SHORT).show()
            }

            this.findNavController().navigateUp()
        }

        return binding.root
    }
}
