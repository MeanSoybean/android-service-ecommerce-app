package com.example.androiddevelopmentgroup7


import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.FrameLayout
import android.widget.ProgressBar
import androidx.activity.viewModels
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.example.androiddevelopmentgroup7.viewModels.ServiceViewModel
import com.google.android.gms.common.internal.safeparcel.SafeParcelable

import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase


class MainActivity : AppCompatActivity() {

    lateinit var user: FirebaseUser
    lateinit var vendorID: String
    val db = Firebase.firestore
    private lateinit var navController : NavController
    private val serviceViewModel: ServiceViewModel by viewModels()
    val auth = Firebase.auth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        auth.signInWithEmailAndPassword("khatuantran11@gmail.com", "123123")
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    user = auth.currentUser!!
                    db.collection("Accounts")
                        .whereEqualTo("UID", user.uid)
                        .get()
                        .addOnSuccessListener { doc ->
                            db.collection("Vendors")
                                .whereEqualTo("AccountID", doc.documents[0].id)
                                .get()
                                .addOnSuccessListener { snapshot ->
                                    vendorID = snapshot.documents[0].id
                                    Log.i("VendorID", vendorID)
                                    serviceViewModel.setupVendorID(vendorID)

                                    setContentView(R.layout.activity_main)
                                    setupNav()
                                }
                            Log.i("CURRENT USER", user.uid)

                        }



                } else {
                    // If sign in fails, display a message to the user.
                    Log.i("Login Fail", "createUserWithEmail:failure", task.exception)
                }
            }



    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp() || super.onSupportNavigateUp()
    }
    private fun setupNav(){
        val bottomNav = findViewById<BottomNavigationView>(R.id.home_bottom_navigation)
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