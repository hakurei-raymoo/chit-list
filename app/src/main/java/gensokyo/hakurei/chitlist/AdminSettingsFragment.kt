package gensokyo.hakurei.chitlist

import android.app.Activity.RESULT_OK
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import gensokyo.hakurei.chitlist.utilities.DATABASE_NAME
import gensokyo.hakurei.chitlist.database.AppDatabase
import gensokyo.hakurei.chitlist.databinding.FragmentAdminSettingsBinding
import gensokyo.hakurei.chitlist.viewmodels.AdminSettingsViewModel
import gensokyo.hakurei.chitlist.viewmodels.AdminSettingsViewModelFactory
import kotlin.system.exitProcess

private const val TAG = "AdminSettingsFragment"

const val EXPORT_BALANCES_REQUEST_CODE = 101
const val EXPORT_ITEMS_REQUEST_CODE = 102
const val EXPORT_TRANSACTIONS_REQUEST_CODE = 103
const val BACKUP_DATABASE_REQUEST_CODE = 201
const val RESTORE_DATABASE_REQUEST_CODE = 202

class AdminSettingsFragment : Fragment() {

    private lateinit var adminSettingsViewModel: AdminSettingsViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        // Get a reference to the binding object and inflate the fragment views.
        val binding = FragmentAdminSettingsBinding.inflate(inflater, container, false)

        // Create an instance of the ViewModel Factory.
        val application = requireNotNull(this.activity).application
        val dataSource = AppDatabase.getInstance(application).adminSettingsDao
        val viewModelFactory = AdminSettingsViewModelFactory(
            dataSource,
            application
        )

        // Get a reference to the ViewModel associated with this fragment.
        adminSettingsViewModel =
            ViewModelProviders.of(this, viewModelFactory).get(AdminSettingsViewModel::class.java)

        // To use the View Model with data binding, you have to explicitly
        // give the binding object a reference to it.
        binding.adminSettingsViewModel = adminSettingsViewModel

        // Specify the current activity as the lifecycle owner of the binding.
        // This is necessary so that the binding can observe LiveData updates.
        binding.lifecycleOwner = viewLifecycleOwner

        binding.exportBalancesButton.setOnClickListener {
            val intent = Intent(Intent.ACTION_CREATE_DOCUMENT).apply {
                // Filter to only show results that can be "opened", such as
                // a file (as opposed to a list of contacts or timezones).
                addCategory(Intent.CATEGORY_OPENABLE)

                // Create a file with the requested MIME type.
                type = "text/csv"
                putExtra(Intent.EXTRA_TITLE, "balances-${System.currentTimeMillis()}.csv")
            }

            startActivityForResult(intent, EXPORT_BALANCES_REQUEST_CODE)
        }

        binding.exportItemsButton.setOnClickListener {
            val intent = Intent(Intent.ACTION_CREATE_DOCUMENT).apply {
                addCategory(Intent.CATEGORY_OPENABLE)
                type = "text/csv"
                putExtra(Intent.EXTRA_TITLE, "items-${System.currentTimeMillis()}.csv")
            }

            startActivityForResult(intent, EXPORT_ITEMS_REQUEST_CODE)
        }

        binding.exportTransactionsButton.setOnClickListener {
            val intent = Intent(Intent.ACTION_CREATE_DOCUMENT).apply {
                addCategory(Intent.CATEGORY_OPENABLE)
                type = "text/csv"
                putExtra(Intent.EXTRA_TITLE, "transactions-${System.currentTimeMillis()}.csv")
            }

            startActivityForResult(intent,
                EXPORT_TRANSACTIONS_REQUEST_CODE
            )
        }

        binding.backupDatabaseButton.setOnClickListener {
            val intent = Intent(Intent.ACTION_CREATE_DOCUMENT).apply {
                addCategory(Intent.CATEGORY_OPENABLE)
                type = "application/x-sqlite3"
                putExtra(Intent.EXTRA_TITLE, "$DATABASE_NAME-${System.currentTimeMillis()}")
            }

            startActivityForResult(intent, BACKUP_DATABASE_REQUEST_CODE)
        }

        binding.restoreDatabaseButton.setOnClickListener {
            val intent = Intent(Intent.ACTION_OPEN_DOCUMENT).apply {
                addCategory(Intent.CATEGORY_OPENABLE)
                type = "application/x-sqlite3"
            }

            startActivityForResult(intent, RESTORE_DATABASE_REQUEST_CODE)
        }

        // Observer to process return messages.
        adminSettingsViewModel.returnMessage.observe(this, Observer {
            if (it != null) {
                Toast.makeText(activity, it, Toast.LENGTH_LONG).show()

                // Reset state to make sure we only display once.
                adminSettingsViewModel.messageReturned()
            }
        })

        binding.exitButton.setOnClickListener {
            adminSettingsViewModel.exitApp()
        }

        // Observer to process exit attempt.
        adminSettingsViewModel.exitApp.observe(this, Observer {
            if (it != null) {
                exitProcess(0) // Warning: Only works for one activity.
            }
        })

        return binding.root
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == RESULT_OK) {
            if (data != null && data.data != null) {
                when (requestCode) {
                    EXPORT_BALANCES_REQUEST_CODE -> adminSettingsViewModel.exportBalances(data.data!!)
                    EXPORT_ITEMS_REQUEST_CODE -> adminSettingsViewModel.exportItems(data.data!!)
                    EXPORT_TRANSACTIONS_REQUEST_CODE -> adminSettingsViewModel.exportTransactions(data.data!!)
                    BACKUP_DATABASE_REQUEST_CODE -> adminSettingsViewModel.backupDatabase(data.data!!)
                    RESTORE_DATABASE_REQUEST_CODE -> adminSettingsViewModel.restoreDatabase(data.data!!)
                }
            }
        }
    }
}
