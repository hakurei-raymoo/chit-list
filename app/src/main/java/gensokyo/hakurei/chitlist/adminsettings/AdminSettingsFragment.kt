package gensokyo.hakurei.chitlist.adminsettings

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import gensokyo.hakurei.chitlist.database.AppDatabase
import gensokyo.hakurei.chitlist.databinding.FragmentAdminSettingsBinding

private const val TAG = "AdminSettingsFragment"

class AdminSettingsFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        // Get a reference to the binding object and inflate the fragment views.
        val binding = FragmentAdminSettingsBinding.inflate(inflater, container, false)

        // Create an instance of the ViewModel Factory.
        val application = requireNotNull(this.activity).application
        val dataSource = AppDatabase.getInstance(application).adminDao
        val viewModelFactory = AdminSettingsViewModelFactory(dataSource, application)

        // Get a reference to the ViewModel associated with this fragment.
        val adminSettingsViewModel =
            ViewModelProviders.of(this, viewModelFactory).get(AdminSettingsViewModel::class.java)

        // To use the View Model with data binding, you have to explicitly
        // give the binding object a reference to it.
        binding.adminSettingsViewModel = adminSettingsViewModel

        // Specify the current activity as the lifecycle owner of the binding.
        // This is necessary so that the binding can observe LiveData updates.
        binding.setLifecycleOwner(this)


        // Observer to process exit attempt.
        adminSettingsViewModel.exportReturn.observe(this, Observer {
            if (it != null) {
                Toast.makeText(activity, it, Toast.LENGTH_SHORT).show()

                // Reset state to make sure we only display once.
                adminSettingsViewModel.returnedExport()
            }
        })

        // Observer to process exit attempt.
        adminSettingsViewModel.exitApp.observe(this, Observer {
            if (it != null) {
                // Exit the application.
                requireNotNull(this.activity).finish()

                // Reset state to make sure we only navigate once.
                adminSettingsViewModel.onAppExited()
            }
        })

        return binding.root
    }
}
