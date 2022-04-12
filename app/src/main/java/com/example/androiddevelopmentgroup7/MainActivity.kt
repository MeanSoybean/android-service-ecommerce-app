package com.example.androiddevelopmentgroup7


import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.example.androiddevelopmentgroup7.viewModels.ServiceViewModel

import com.google.android.material.bottomnavigation.BottomNavigationView


class MainActivity : AppCompatActivity() {
    private lateinit var navController : NavController
    private val serviceViewModel: ServiceViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        serviceViewModel.setServiceList(ArrayList())
        setContentView(R.layout.activity_main)
        setupNav()
    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp() || super.onSupportNavigateUp()
    }
    private fun setupNav(){
        var bottomNav = findViewById<BottomNavigationView>(R.id.home_bottom_navigation)
        val navFragment = supportFragmentManager.findFragmentById(R.id.vendor_fragment_container_view) as NavHostFragment

        navController = navFragment.navController
        bottomNav.setupWithNavController(navController)
        navFragment.navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.home_vendor_fragment -> {bottomNav.visibility = View.VISIBLE}
                R.id.test1Fragment -> {bottomNav.visibility = View.VISIBLE}
                R.id.test2Fragment -> {bottomNav.visibility = View.VISIBLE}
                else -> {bottomNav.visibility = View.GONE}
            }
        }
    }
}