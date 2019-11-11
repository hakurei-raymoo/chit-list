package gensokyo.hakurei.chitlist

import android.app.admin.DevicePolicyManager
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.BatteryManager
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.navigation.findNavController
import gensokyo.hakurei.chitlist.databinding.ActivityMainBinding
import gensokyo.hakurei.chitlist.utilities.Config

private const val TAG = "MainActivity"

class MainActivity : AppCompatActivity() {
    private lateinit var mAdminComponentName: ComponentName
    private lateinit var mDevicePolicyManager: DevicePolicyManager

    // Flags for fullscreen.
    private val flags = (View.SYSTEM_UI_FLAG_LAYOUT_STABLE
            or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
            or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
            or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
            or View.SYSTEM_UI_FLAG_FULLSCREEN
            or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY)

    override fun onCreate(savedInstanceState: Bundle?) {
        Config.init(this)
        Config.write()
        Config.read()

        // Call the superclass implementation.
        super.onCreate(savedInstanceState)

        // Associate the activity_main layout with the MainActivity and return the associated binding.
        @Suppress("UNUSED_VARIABLE")
        val binding: ActivityMainBinding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        // Set up Android Jetpack Navigation.
        @Suppress("UNUSED_VARIABLE")
        val navController = findNavController(R.id.nav_host_fragment)

        mAdminComponentName = componentName
        mDevicePolicyManager = getSystemService(Context.DEVICE_POLICY_SERVICE) as DevicePolicyManager
        if (mDevicePolicyManager.isDeviceOwnerApp(packageName)) {
            Log.i(TAG, getString(R.string.is_admin))
            setKioskPolicies()
        } else {
            Toast.makeText(this, getString(R.string.not_admin_warning), Toast.LENGTH_LONG).show()
            startLockTask()
        }

        // Set fullscreen.
        window.decorView.systemUiVisibility = flags
    }

    private fun setKioskPolicies() {
        mDevicePolicyManager.setLockTaskPackages(mAdminComponentName, arrayOf(packageName))
        startLockTask()

        // Set as default application.
        val intentFilter = IntentFilter(Intent.ACTION_MAIN)
        intentFilter.addCategory(Intent.CATEGORY_HOME)
        intentFilter.addCategory(Intent.CATEGORY_DEFAULT)
        mDevicePolicyManager.addPersistentPreferredActivity(mAdminComponentName,
            intentFilter, ComponentName(packageName, MainActivity::class.java.name))

        // Disable lock screen to start app automatically.
        mDevicePolicyManager.setKeyguardDisabled(mAdminComponentName, true)

        // Keep screen awake.
        mDevicePolicyManager.setGlobalSetting(mAdminComponentName,
            Settings.Global.STAY_ON_WHILE_PLUGGED_IN,
            (BatteryManager.BATTERY_PLUGGED_AC
                    or BatteryManager.BATTERY_PLUGGED_USB
                    or BatteryManager.BATTERY_PLUGGED_WIRELESS).toString()
        )
    }
}
