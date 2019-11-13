package gensokyo.hakurei.chitlist

import android.app.Activity.RESULT_OK
import android.app.admin.DevicePolicyManager
import android.content.Context
import android.content.Intent
import android.graphics.Typeface
import android.os.Bundle
import android.text.SpannableStringBuilder
import android.text.Spanned
import android.text.style.StyleSpan
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import gensokyo.hakurei.chitlist.database.AppDatabase
import gensokyo.hakurei.chitlist.databinding.FragmentAdminSettingsBinding
import gensokyo.hakurei.chitlist.utilities.Config
import gensokyo.hakurei.chitlist.utilities.Converter
import gensokyo.hakurei.chitlist.viewmodels.AdminSettingsViewModel
import gensokyo.hakurei.chitlist.viewmodels.AdminSettingsViewModelFactory
import kotlin.system.exitProcess

private const val TAG = "AdminSettingsFragment"

const val EXPORT_ACCOUNTS_REQUEST_CODE = 101
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

        binding.exportAccountsButton.setOnClickListener {
            val intent = Intent(Intent.ACTION_CREATE_DOCUMENT).apply {
                // Filter to only show results that can be "opened", such as
                // a file (as opposed to a list of contacts or timezones).
                addCategory(Intent.CATEGORY_OPENABLE)

                // Create a file with the requested MIME type.
                type = "text/csv"
                putExtra(Intent.EXTRA_TITLE, "accounts-${System.currentTimeMillis()}.csv")
            }

            startActivityForResult(intent, EXPORT_ACCOUNTS_REQUEST_CODE)
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
                putExtra(Intent.EXTRA_TITLE, "${Config.DATABASE_NAME}-${System.currentTimeMillis()}")
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

        binding.editConfigButton.setOnClickListener {
            this.findNavController().navigate(
                AdminViewPagerFragmentDirections.actionAdminViewPagerFragmentToEditConfigFragment()
            )
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
                val mAdminComponentName = requireActivity().componentName
                val mDevicePolicyManager = requireContext().getSystemService(Context.DEVICE_POLICY_SERVICE) as DevicePolicyManager
                if (mDevicePolicyManager.isDeviceOwnerApp(requireContext().packageName)) {
                    Log.i(TAG, "Removing from default home apps list.")
                    mDevicePolicyManager.clearPackagePersistentPreferredActivities(
                        mAdminComponentName,
                        requireContext().packageName
                    )
                } else {
                    Log.i(TAG, "Was not a device administrator.")
                }

                requireActivity().finish()
                exitProcess(0) // Warning: Only exits one activity.
            }
        })

        // Show log once version is clicked 5 times.
        var clicks = 0
        binding.versionText.setOnClickListener{
            clicks += 1
            if (clicks % 5 == 0) {
                binding.exportAccountsButton.visibility = View.VISIBLE
                binding.exportItemsButton.visibility = View.VISIBLE
                binding.logText.visibility = View.VISIBLE
                binding.logText.text = writeLog()
            } else {
                binding.exportAccountsButton.visibility = View.GONE
                binding.exportItemsButton.visibility = View.GONE
                binding.logText.visibility = View.GONE
            }
        }

        return binding.root
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == RESULT_OK) {
            if (data != null && data.data != null) {
                when (requestCode) {
                    EXPORT_ACCOUNTS_REQUEST_CODE -> adminSettingsViewModel.exportAccounts(data.data!!)
                    EXPORT_ITEMS_REQUEST_CODE -> adminSettingsViewModel.exportItems(data.data!!)
                    EXPORT_TRANSACTIONS_REQUEST_CODE -> adminSettingsViewModel.exportTransactions(data.data!!)
                    BACKUP_DATABASE_REQUEST_CODE -> adminSettingsViewModel.backupDatabase(data.data!!)
                    RESTORE_DATABASE_REQUEST_CODE -> adminSettingsViewModel.restoreDatabase(data.data!!)
                }
            }
        }
    }

    private fun writeLog(): Spanned {
        SpannableStringBuilder().apply {
            val mAdminComponentName = requireActivity().componentName
            val mDevicePolicyManager = requireContext().getSystemService(Context.DEVICE_POLICY_SERVICE) as DevicePolicyManager
            val database = requireActivity().getDatabasePath(Config.DATABASE_NAME).absolutePath

            append("Time: ", StyleSpan(Typeface.BOLD), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
            append(Converter.convertLongToDateStringShort(System.currentTimeMillis()))
            append("\n")
            append("PackageName: ", StyleSpan(Typeface.BOLD), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
            append(requireContext().packageName)
            append("\n")
            append("AdminComponentName: ", StyleSpan(Typeface.BOLD), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
            append(mAdminComponentName.toString())
            append("\n")
            append("DevicePolicyManager: ", StyleSpan(Typeface.BOLD), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
            append(mDevicePolicyManager.toString())
            append("\n")
            append("isDeviceOwnerApp: ", StyleSpan(Typeface.BOLD), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
            append(mDevicePolicyManager.isDeviceOwnerApp(requireContext().packageName).toString())
            append("\n")
            append("Properties path: ", StyleSpan(Typeface.BOLD), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
            append(Config.file.toString())
            append("\n")
            append("Properties {", StyleSpan(Typeface.BOLD), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
            append("\n")
            Config.properties.forEach { (k, v) ->
                append("\t$k: ", StyleSpan(Typeface.BOLD), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
                append("$v")
                append("\n")
            }
            append("}", StyleSpan(Typeface.BOLD), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
            append("\n")
            append("Database path: ", StyleSpan(Typeface.BOLD), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
            append(database)
            append("\n")

            return this
        }
    }
}
