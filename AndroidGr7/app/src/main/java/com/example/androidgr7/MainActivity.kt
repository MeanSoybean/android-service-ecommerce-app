package com.example.androidgr7

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.viewModels
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.example.androidgr7.dataModels.Notification
import com.example.androidgr7.dataModels.Order
import com.example.androidgr7.dataModels.Service
import com.example.androidgr7.dataModels.UserCustomer
import com.example.androidgr7.viewModels.NotificationViewModel
import com.example.androidgr7.viewModels.OrderListModel
import com.example.androidgr7.viewModels.ServiceViewModel
import com.example.androidgr7.viewModels.UserViewModel
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.firestore.auth.User
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class MainActivity : AppCompatActivity() {
    val db = Firebase.firestore
    private lateinit var navController : NavController

    private val serviceViewModel: ServiceViewModel by viewModels()
    private val orderListModel: OrderListModel by viewModels()
    private val userViewModel:UserViewModel by viewModels()
    private val notificationViewModel: NotificationViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {


        super.onCreate(savedInstanceState)
        val temp1= ArrayList<Service>()
        val temp2= ArrayList<Order>()
        val temp3 = ArrayList<UserCustomer>()
        val temp4 = ArrayList<Notification>()
        db.collection("Customers")
            .document("Customer")
            .get()
            .addOnSuccessListener { document ->
                val user = UserCustomer(document.get("accountID")as String,document.get("name")as String,
                    document.get("phoneNumber")as String,document.get("address")as String,
                    document.get("rating") as Long ,document.get("paymentDetails")as String)
                temp3.add(user)

                db.collection("OrderListing")
                    .whereEqualTo("idCustomer",temp3[0].AccountID)
                    .get()
                    .addOnSuccessListener { result ->
                        Log.d("CHECK", temp3[0].AccountID)
                        for (document in result) {
                            Log.d("CHECK", document.id)
                            val k =Order(document.id as String,
                                document.get("idCustomer")as String,
                                document.get("serviceName")as String,document.get("nameVendor")as String,
                                document.get("timeOrder") as String,document.get("timeComing")as String,
                                document.get("orderAddress")as String,
                                document.get("orderCurrent")as String,document.get("price")as String,
                                document.get("serviceImage")as String)
                            temp2.add(k)
                        }
                    }
                    .addOnFailureListener { exception ->
                        Log.d("CHECK", "Error getting documents: ", exception)
                    }

                db.collection("Notifications")
                    .whereEqualTo("idCustomer",temp3[0].AccountID)
                    .get()
                    .addOnSuccessListener { result ->
                        Log.d("CHECK", temp3[0].AccountID)
                        for (document in result) {
                            Log.d("CHECK", document.id)
                            val k =Notification(document.get("Name")as String,document.get("idCustomer")as String,
                                document.get("Description")as String)
                            temp4.add(k)
                        }
                    }
                    .addOnFailureListener { exception ->
                        Log.d("CHECK", "Error getting documents: ", exception)
                    }


            }
            .addOnFailureListener { exception ->
                Log.w("CHECK", "Error getting documents: ", exception)
            }

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



        notificationViewModel.setServiceList(temp4)
        userViewModel.setServiceList(temp3)
        serviceViewModel.setServiceList(temp1)
        orderListModel.setServiceList(temp2)
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