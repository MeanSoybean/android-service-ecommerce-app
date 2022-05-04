package com.example.androiddevelopmentgroup7.views.activities



import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.example.androiddevelopmentgroup7.R
import com.example.androiddevelopmentgroup7.utils.Utils
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase


class MainActivity : AppCompatActivity() {
    val db = Firebase.firestore
    private lateinit var navController : NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val role = when (Utils.typeUser) {
            0 -> "Customer"
            else -> "Vendor"
        }
        setupNav(role)
    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp() || super.onSupportNavigateUp()
    }

    private fun setupNav(role: String){
        val bottomNav = findViewById<BottomNavigationView>(R.id.home_bottom_navigation)
        val navFragment = supportFragmentManager.findFragmentById(R.id.vendor_fragment_container_view) as NavHostFragment

        val inflater = navFragment.navController.navInflater
        val graph = inflater.inflate(R.navigation.main_navigation)
        if (role == "Customer"){
            bottomNav.menu.removeItem(R.id.home_vendor_fragment)
            graph.setStartDestination(R.id.home_customer_fragment)
        } else {
            bottomNav.menu.removeItem(R.id.home_customer_fragment)
            graph.setStartDestination(R.id.home_vendor_fragment)
        }
        val navController = navFragment.navController
        navController.setGraph(graph, intent.extras)
        bottomNav.setupWithNavController(navController)

        navFragment.navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.home_customer_fragment -> {bottomNav.visibility = View.VISIBLE}
                R.id.home_vendor_fragment -> {bottomNav.visibility = View.VISIBLE}
                R.id.orderServiceFragment -> {bottomNav.visibility = View.VISIBLE}
                R.id.fragment_communication -> {bottomNav.visibility = View.VISIBLE}
                R.id.profile_fragment -> {bottomNav.visibility = View.VISIBLE}
                else -> {bottomNav.visibility = View.GONE}
            }
        }
    }
}