package com.example.androiddevelopmentgroup7.views.activities


import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.View
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.example.androiddevelopmentgroup7.R
import com.example.androiddevelopmentgroup7.utils.Utils
import com.example.androiddevelopmentgroup7.models.UserCustomer
import com.example.androiddevelopmentgroup7.models.UserVendor
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase


class MainActivity : AppCompatActivity() {

    lateinit var user: FirebaseUser

    val db = Firebase.firestore
    private lateinit var navController : NavController
    val auth = Firebase.auth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //auth.signInWithEmailAndPassword("khatuantran11@gmail.com", "123123") //vendor
        //auth.signInWithEmailAndPassword("bioclaw@gmail.com", "dummyPassword123") //customer
            //.addOnCompleteListener(this) { task ->
                //if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    user = auth.currentUser!!
                    db.collection("Accounts")
                        .whereEqualTo("UID", user.uid)
                        .get()
                        .addOnSuccessListener { doc ->
                            if(doc.documents[0].data!!.get("Role").toString().equals("Vendor")){
                                Utils.typeUser = 1
                                db.collection("Vendors")
                                    .whereEqualTo("AccountID", doc.documents[0].id)
                                    .get()
                                    .addOnSuccessListener { snapshot ->
                                        Utils.vendor = UserVendor(
                                            doc.documents[0].id, //AccountID (ID of document in Accounts)
                                            snapshot.documents[0].data!!.get("Name").toString(),
                                            snapshot.documents[0].data!!.get("PhoneNumber").toString(),
                                            snapshot.documents[0].data!!.get("Address").toString(),
                                            snapshot.documents[0].data!!.get("Rating").toString().toFloat(),
                                        )
                                        Utils.vendor.id = snapshot.documents[0].id //id of document

                                        setContentView(R.layout.activity_main)
                                        setupNav(doc.documents[0].get("Role").toString())
                                    }
                                Log.i("CURRENT USER", user.uid)
                            } else {
                                Utils.typeUser = 0
                                Log.i("ACCOOURBID",doc.documents[0].id )
                                db.collection("Customers")
                                    .whereEqualTo("AccountID", doc.documents[0].id)
                                    .get()
                                    .addOnSuccessListener { snapshot ->
                                        Log.i("LENGTH NAPSHOT", snapshot.documents.size.toString())
                                        Utils.customer = UserCustomer(
                                            doc.documents[0].id, //AccountID (ID of document in Accounts)
                                            snapshot.documents[0].data!!.get("Name").toString(),
                                            "",
//                                            snapshot.documents[0].data!!.get("PhoneNumber").toString(),
//                                            snapshot.documents[0].data!!.get("Address").toString(),
                                            "",
                                            snapshot.documents[0].data!!.get("Rating").toString().toFloat(),
                                            "",
                                        )
                                        Utils.customer.id = snapshot.documents[0].id //id of document

                                        setContentView(R.layout.activity_main)
                                        setupNav(doc.documents[0].get("Role").toString())
                                        //setupNav("Customer")

                                    }
                                Log.i("CURRENT USER", user.uid)
                            }


                        }
//                } else {
//                    // If sign in fails, display a message to the user.
//                    Log.i("Login Fail", "createUserWithEmail:failure", task.exception)
//                }
            //}


    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp() || super.onSupportNavigateUp()
    }
    private fun setupNav(role: String){

        val bottomNav = findViewById<BottomNavigationView>(R.id.home_bottom_navigation)



        val navFragment = supportFragmentManager.findFragmentById(R.id.vendor_fragment_container_view) as NavHostFragment
        //navController = navFragment.navController
        val inflater = navFragment.navController.navInflater
        val graph = inflater.inflate(R.navigation.main_navigation)
        if(role.equals("Customer")){
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