package gensokyo.hakurei.chitlist.adminactions

import android.app.Activity.RESULT_OK
import android.app.admin.DevicePolicyManager
import android.content.ComponentName
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
import androidx.navigation.navGraphViewModels
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import gensokyo.hakurei.chitlist.ChitlistDeviceAdminReceiver
import gensokyo.hakurei.chitlist.R
import gensokyo.hakurei.chitlist.adminhome.AdminHomeViewModel
import gensokyo.hakurei.chitlist.adminhome.AdminViewPagerFragmentDirections
import gensokyo.hakurei.chitlist.database.AppDatabase
import gensokyo.hakurei.chitlist.databinding.FragmentAdminActionsBinding
import gensokyo.hakurei.chitlist.utilities.Config
import gensokyo.hakurei.chitlist.utilities.Converter
import kotlin.system.exitProcess

private const val TAG = "AdminActionsFragment"

const val EXPORT_ACCOUNTS_REQUEST_CODE = 101
const val EXPORT_ITEMS_REQUEST_CODE = 102
const val EXPORT_TRANSACTIONS_REQUEST_CODE = 103
const val BACKUP_DATABASE_REQUEST_CODE = 201
const val RESTORE_DATABASE_REQUEST_CODE = 202

class AdminActionsFragment : Fragment() {

    private lateinit var adminActionsViewModel: AdminActionsViewModel

    private val adminHomeViewModel: AdminHomeViewModel by navGraphViewModels(R.id.home_navigation)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        // Get a reference to the binding object and inflate the fragment views.
        val binding = FragmentAdminActionsBinding.inflate(inflater, container, false)

        // Create an instance of the ViewModel Factory.
        val application = requireNotNull(this.activity).application
        val dataSource = AppDatabase.getInstance(application).adminActionsDao
        val viewModelFactory = AdminActionsViewModelFactory(dataSource, application)

        // Get a reference to the ViewModel associated with this fragment.
        adminActionsViewModel =
            ViewModelProviders.of(this, viewModelFactory).get(AdminActionsViewModel::class.java)

        // Specify the current activity as the lifecycle owner of the binding.
        // This is necessary so that the binding can observe LiveData updates.
        binding.lifecycleOwner = viewLifecycleOwner


        binding.payAccountButton.setOnClickListener {
            this.findNavController().navigate(
                AdminViewPagerFragmentDirections.actionAdminViewPagerFragmentToPayAccountFragment(adminHomeViewModel.userId)
            )
        }

        binding.editConfigButton.setOnClickListener {
            this.findNavController().navigate(
                AdminViewPagerFragmentDirections.actionAdminViewPagerFragmentToEditConfigFragment()
            )
        }

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

        binding.dropDatabaseButton.setOnClickListener {
            MaterialAlertDialogBuilder(context).setTitle(getString(R.string.drop_database))
                .setMessage(getString(R.string.drop_database_warning))
                .setPositiveButton(getString(R.string.delete)) { _, _ -> adminActionsViewModel.dropDatabase() }
                .setNegativeButton(getString(R.string.cancel), null)
                .show()
        }

        // Observer to process return messages.
        adminActionsViewModel.returnMessage.observe(this, Observer {
            if (it != null) {
                Toast.makeText(activity, it, Toast.LENGTH_LONG).show()

                // Reset state to make sure we only display once.
                adminActionsViewModel.messageReturned()
            }
        })

        binding.exitButton.setOnClickListener {
            adminActionsViewModel.exitApp()
        }

        // Observer to process exit attempt.
        adminActionsViewModel.exitApp.observe(this, Observer {
            if (it != null) {
                val adminComponentName = ChitlistDeviceAdminReceiver.getComponentName(requireContext())
                val devicePolicyManager = requireContext().getSystemService(Context.DEVICE_POLICY_SERVICE) as DevicePolicyManager
                if (devicePolicyManager.isDeviceOwnerApp(requireContext().packageName)) {
                    removeKioskPolicies(adminComponentName, devicePolicyManager)
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
                binding.dropDatabaseButton.visibility = View.VISIBLE
                binding.logText.visibility = View.VISIBLE
                binding.logText.text = writeLog()
            } else {
                binding.exportAccountsButton.visibility = View.GONE
                binding.exportItemsButton.visibility = View.GONE
                binding.dropDatabaseButton.visibility = View.GONE
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
                    EXPORT_ACCOUNTS_REQUEST_CODE -> adminActionsViewModel.exportAccounts(data.data!!)
                    EXPORT_ITEMS_REQUEST_CODE -> adminActionsViewModel.exportItems(data.data!!)
                    EXPORT_TRANSACTIONS_REQUEST_CODE -> adminActionsViewModel.exportTransactions(data.data!!)
                    BACKUP_DATABASE_REQUEST_CODE -> adminActionsViewModel.backupDatabase(data.data!!)
                    RESTORE_DATABASE_REQUEST_CODE -> adminActionsViewModel.restoreDatabase(data.data!!)
                }
            }
        }
    }

    private fun removeKioskPolicies(adminComponentName: ComponentName, devicePolicyManager: DevicePolicyManager) {
        requireActivity().stopLockTask()

        // Clear as default application.
        devicePolicyManager.clearPackagePersistentPreferredActivities(
            adminComponentName,
            requireActivity().packageName
        )

        // Allow the user to select the default home screen.
        val intent = Intent(Intent.ACTION_MAIN).apply {
            addCategory(Intent.CATEGORY_HOME)
            flags = Intent.FLAG_ACTIVITY_NEW_TASK
        }
        startActivity(Intent.createChooser(intent, "Select home"))
    }

    private fun writeLog(): Spanned {
        SpannableStringBuilder().apply {
            val mDevicePolicyManager = requireContext().getSystemService(Context.DEVICE_POLICY_SERVICE) as DevicePolicyManager
            val database = requireActivity().getDatabasePath(Config.DATABASE_NAME).absolutePath

            append("Time: ", StyleSpan(Typeface.BOLD), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
            append(Converter.convertLongToDateStringShort(System.currentTimeMillis()))
            append("\n")
            append("PackageName: ", StyleSpan(Typeface.BOLD), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
            append(requireContext().packageName)
            append("\n")
            append("isDeviceOwnerApp: ", StyleSpan(Typeface.BOLD), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
            append(mDevicePolicyManager.isDeviceOwnerApp(requireContext().packageName).toString())
            append("\n")
            append("Database path: ", StyleSpan(Typeface.BOLD), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
            append(database)
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

            return this
        }
    }
}
