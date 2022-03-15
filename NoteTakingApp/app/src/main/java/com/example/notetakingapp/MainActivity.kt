package com.example.notetakingapp

import android.os.Bundle
import android.view.Window
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.notetakingapp.databinding.ActivityMainBinding
import com.example.notetakingapp.utilities.DataSynchronizer
import com.example.notetakingapp.utilities.FileManager

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Instantiate file manager
        val fm = FileManager.instance
        fm?.initManager(this)
        // Handle the dirty data first!
        fm?.dataSynchronizer?.handleDirtyData()
        fm?.initFiles()

        // Hide Action Bar
        window.requestFeature(Window.FEATURE_ACTION_BAR)
        actionBar?.hide()

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navView: BottomNavigationView = binding.navView

        navController = findNavController(R.id.nav_host_fragment_activity_main)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_folders, R.id.navigation_prompt, R.id.navigation_notifications
            )
        )

        navView.setupWithNavController(navController)
    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp() || super.onSupportNavigateUp()
    }
}