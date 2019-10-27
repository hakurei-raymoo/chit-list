package gensokyo.hakurei.chitlist

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.NavigationUI
import gensokyo.hakurei.chitlist.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        // TODO: Setup kiosk mode.
        // Call the superclass implementation.
        super.onCreate(savedInstanceState)

        // Associate the activity_main layout with the MainActivity and return the associated binding.
        @Suppress("UNUSED_VARIABLE")
        val binding: ActivityMainBinding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        // Set up Android Jetpack Navigation.
        navController = findNavController(R.id.nav_host_fragment)
    }

//    override fun onSupportNavigateUp(): Boolean {
//        val navController = this.findNavController(R.id.nav_host_fragment)
//        return navController.navigateUp()
//    }
}
