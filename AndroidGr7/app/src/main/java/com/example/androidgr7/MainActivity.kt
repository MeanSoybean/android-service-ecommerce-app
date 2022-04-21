package com.example.androidgr7

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.viewModels
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.example.androidgr7.dataModels.Order
import com.example.androidgr7.dataModels.Service
import com.example.androidgr7.viewModels.OrderListModel
import com.example.androidgr7.viewModels.ServiceViewModel
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class MainActivity : AppCompatActivity() {
    val db = Firebase.firestore
    private lateinit var navController : NavController

    private val serviceViewModel: ServiceViewModel by viewModels()
    private val orderListModel: OrderListModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {


        super.onCreate(savedInstanceState)
        val temp1= ArrayList<Service>()
        val temp2= ArrayList<Order>()
        db.collection("ServiceListings")
            .get()
            .addOnSuccessListener { result ->
                for (document in result) {
                    Log.d("CHECK", "${document.get("vendorName")}")
                    val k = Service(document.id as String,document.get("serviceType")as String,document.get("serviceName")as String,
                        document.get("vendorName")as String,
                        document.get("serviceDescription")as String,document.get("servicePrice")as String,
                        document.get("servicePhoneNumber") as String,document.get("serviceImage")as String)
                    temp1.add(k)
                }
            }
            .addOnFailureListener { exception ->
                Log.d("CHECK", "Error getting documents: ", exception)
            }

        db.collection("OrderListing")
            .get()
            .addOnSuccessListener { result ->
                for (document in result) {
                    Log.d("CHECK", "${document.get("vendorName")}")
                    val k = Service(document.id as String,document.get("serviceType")as String,document.get("serviceName")as String,
                        document.get("vendorName")as String,
                        document.get("serviceDescription")as String,document.get("servicePrice")as String,
                        document.get("servicePhoneNumber") as String,document.get("serviceImage")as String)
                    temp1.add(k)
                }
            }
            .addOnFailureListener { exception ->
                Log.d("CHECK", "Error getting documents: ", exception)
            }

        serviceViewModel.setServiceList(temp1)
        orderListModel.setServiceList(ArrayList())
        setContentView(R.layout.activity_main)
        setupNav()
    }
    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp() || super.onSupportNavigateUp()
    }
    private fun setupNav(){
        var bottomNav = findViewById<BottomNavigationView>(R.id.home_bottom_navigation)
        val navFragment = supportFragmentManager.findFragmentById(R.id.customer_fragment_container_view) as NavHostFragment

        navController = navFragment.navController
        bottomNav.setupWithNavController(navController)
        navFragment.navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.home_customer_fragment -> {bottomNav.visibility = View.VISIBLE}
                R.id.OrderFragment -> {bottomNav.visibility = View.VISIBLE}
                R.id.NotiFragment -> {bottomNav.visibility = View.VISIBLE}
                R.id.UserFragment -> {bottomNav.visibility = View.VISIBLE}
                else -> {bottomNav.visibility = View.GONE}
            }
        }
    }
}