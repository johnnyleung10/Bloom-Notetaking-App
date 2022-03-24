package com.example.notetakingapp

import android.os.Bundle
import android.view.Window
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import com.example.notetakingapp.databinding.ActivityMainBinding
import com.example.notetakingapp.utilities.DailyEntryManager
import com.example.notetakingapp.utilities.FileManager
import com.example.notetakingapp.utilities.Profiler
import io.ktor.util.date.*
import kotlin.system.measureTimeMillis

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController

    lateinit var navView: BottomNavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        val startOnCreate = getTimeMillis()
        super.onCreate(savedInstanceState)

        // Instantiate file manager
        val fm = FileManager.instance
        fm?.initManager(this)
        // Handle the dirty data first!
        val elapsedDirtyData = measureTimeMillis { fm?.noteDataSynchronizer?.handleDirtyData()}
        val elapsedInitFiles = measureTimeMillis { fm?.initFiles() }

        // Instantiate daily entry manager
        val dem = DailyEntryManager.instance
        dem?.initManager(this)
        // Handle the dirty data first!
//        dem?.dataSynchronizer?.handleDirtyData()
        val elapsedInitDailyEntries = measureTimeMillis { dem?.initEntries() }

        // Hide Action Bar
        window.requestFeature(Window.FEATURE_ACTION_BAR)
        actionBar?.hide()

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        navView = binding.navView

        navController = findNavController(R.id.nav_host_fragment_activity_main)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_folders, R.id.navigation_prompt, R.id.navigation_notifications
            )
        )

        navView.setupWithNavController(navController)
        val endOnCreate = getTimeMillis()

        // Instantiate Profiler
        val profile = Profiler.instance
        profile?.init(this)

        profile?.profile("setup files and folders", elapsedInitFiles)
        profile?.profile("handle dirty data", elapsedDirtyData)
        profile?.profile("setup the app", endOnCreate - startOnCreate)
        profile?.profile("setup daily entries", elapsedInitDailyEntries)
    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp() || super.onSupportNavigateUp()
    }
}