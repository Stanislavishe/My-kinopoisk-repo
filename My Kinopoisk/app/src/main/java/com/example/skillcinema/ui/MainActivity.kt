package com.example.skillcinema.ui

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.example.skillcinema.R
import com.example.skillcinema.databinding.ActivityMainBinding
import com.google.android.material.bottomnavigation.BottomNavigationView
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navView: BottomNavigationView = binding.navView

        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment_activity_main) as NavHostFragment

        navController = navHostFragment.navController

        navView.setupWithNavController(navController)

        navView.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.home_icon -> navController.navigate(R.id.homeFragment)
                R.id.nav_search -> navController.navigate(R.id.search)
                R.id.nav_profile -> navController.navigate(R.id.profile)
            }
            true
        }
        navController.addOnDestinationChangedListener { _, destination, _ ->
            navView.visibility = if (
                destination.id == R.id.loading ||
                destination.id == R.id.photoFullscreen ||
                destination.id == R.id.startFragment ||
                destination.id == R.id.helloScreensViewPagerFragment
            ) View.GONE else View.VISIBLE

        }
    }
}