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

    // Flags for fullscreen.
    private val flags = (View.SYSTEM_UI_FLAG_LAYOUT_STABLE
            or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
            or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
            or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
            or View.SYSTEM_UI_FLAG_FULLSCREEN
            or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY)

    override fun onCreate(savedInstanceState: Bundle?) {
        Config.init(this)

        // Call the superclass implementation.
        super.onCreate(savedInstanceState)

        // Associate the activity_main layout with the MainActivity and return the associated binding.
        @Suppress("UNUSED_VARIABLE")
            val binding: ActivityMainBinding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        // Set up Android Jetpack Navigation.
        @Suppress("UNUSED_VARIABLE")
        val navController = findNavController(R.id.nav_host_fragment)

        val adminComponentName = ChitlistDeviceAdminReceiver.getComponentName(this)
        val devicePolicyManager = getSystemService(Context.DEVICE_POLICY_SERVICE) as DevicePolicyManager
        if (devicePolicyManager.isDeviceOwnerApp(packageName)) {
            setKioskPolicies(adminComponentName, devicePolicyManager)
        } else {
            Toast.makeText(this, getString(R.string.not_admin_warning), Toast.LENGTH_LONG).show()
            startLockTask()
        }

        // Set fullscreen.
        window.decorView.systemUiVisibility = flags
    }

    private fun setKioskPolicies(adminComponentName: ComponentName, devicePolicyManager: DevicePolicyManager) {
        Log.i(TAG, getString(R.string.is_admin))

        devicePolicyManager.setLockTaskPackages(adminComponentName, arrayOf(packageName))
        startLockTask()

        // Set as default application.
        val intentFilter = IntentFilter(Intent.ACTION_MAIN)
        intentFilter.addCategory(Intent.CATEGORY_HOME)
        intentFilter.addCategory(Intent.CATEGORY_DEFAULT)
        devicePolicyManager.addPersistentPreferredActivity(adminComponentName,
            intentFilter, ComponentName(packageName, MainActivity::class.java.name))

        // Disable lock screen to start app automatically.
        devicePolicyManager.setKeyguardDisabled(adminComponentName, true)

        // Keep screen awake.
        devicePolicyManager.setGlobalSetting(adminComponentName,
            Settings.Global.STAY_ON_WHILE_PLUGGED_IN,
            (BatteryManager.BATTERY_PLUGGED_AC
                    or BatteryManager.BATTERY_PLUGGED_USB
                    or BatteryManager.BATTERY_PLUGGED_WIRELESS).toString()
        )
    }
}
