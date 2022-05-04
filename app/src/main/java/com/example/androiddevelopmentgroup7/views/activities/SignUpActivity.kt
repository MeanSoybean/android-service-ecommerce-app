package com.example.androiddevelopmentgroup7.views.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.FrameLayout
import android.widget.Toast
import com.example.androiddevelopmentgroup7.views.fragments.ICustomerSignUp
import com.example.androiddevelopmentgroup7.views.fragments.IVendorSignUp
import com.example.androiddevelopmentgroup7.R
import com.example.androiddevelopmentgroup7.models.UserCustomer
import com.example.androiddevelopmentgroup7.models.UserVendor
import com.example.androiddevelopmentgroup7.utils.Utils
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class SignUpActivity : AppCompatActivity(), ICustomerSignUp, IVendorSignUp {
    private lateinit var auth: FirebaseAuth
    private lateinit var db: FirebaseFirestore
    private lateinit var loader: FrameLayout
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)

        auth = Firebase.auth
        db = Firebase.firestore
        loader = findViewById(R.id.loader_layout)
    }

    private fun onSignUpFailed() {
        Toast.makeText(this, getString(R.string.could_not_sign_up), Toast.LENGTH_SHORT).show()
        loader.visibility = View.GONE
    }

    override fun signUpCustomer(name: String, email: String, password: String) {
        // Auth create
        loader.visibility = View.VISIBLE
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    onSignUpCustomerSucceeded(email, name)
                } else {
                    onSignUpFailed()
                }
            }
    }

    private fun onSignUpCustomerSucceeded(email: String, name: String) {
        val user = auth.currentUser!!
        // Firebase Account document
        val data: HashMap<String, Any> = hashMapOf(
            "email" to email,
            "role" to "Customer",
            "UID" to user.uid,
            "accountStatus" to "Activated"
        )
        val accountDocRef = db.collection("Accounts").document(user.uid)
        accountDocRef.set(data)
            .addOnSuccessListener { onWriteCustomerAccountDocSucceeded(accountDocRef, name, email) }
            .addOnFailureListener { onSignUpFailed() }
    }

    private fun onWriteCustomerAccountDocSucceeded(accountDocRef: DocumentReference, name: String, email: String) {
        // Firebase Customer document
        val data = hashMapOf(
            "accountID" to accountDocRef.id,
            "email" to email,
            "name" to name,
            "phoneNumber" to "",
            "address" to "",
            "rating" to 5.0f,
            "paymentDetails" to ""
        )
        val customerDocRef = db.collection("Customers").document(accountDocRef.id)
        customerDocRef.set(data)
            .addOnSuccessListener {
                val locationData = hashMapOf(
                    "accountID" to accountDocRef.id,
                    "address" to getString(R.string.hcmus_address),
                    "latitude" to getString(R.string.hcmus_lat),
                    "longitude" to getString(R.string.hcmus_long),
                )
                val locationDocRef = db.collection("Locations").document(accountDocRef.id)
                locationDocRef.set(locationData)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            Utils.typeUser = 0
                            Utils.customer = UserCustomer(
                                data["accountID"] as String,
                                data["name"] as String,
                                data["phoneNumber"] as String,
                                data["address"] as String,
                                data["rating"] as Float,
                                data["paymentDetails"] as String,
                            )
                            startActivity(Intent(this, MainActivity::class.java))
                        }
                        else {
                            onSignUpFailed()
                        }
                    }
            }
            .addOnFailureListener {
                onSignUpFailed()
            }
    }

    override fun signUpVendor(
        name: String,
        email: String,
        phone: String,
        address: String,
        password: String
    ) {
        // Auth create
        loader.visibility = View.VISIBLE
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    onSignUpVendorSucceeded(email, name, phone, address)
                } else {
                    onSignUpFailed()
                }
            }
    }

    private fun onSignUpVendorSucceeded(email: String, name: String, phone: String, address: String) {
        val user = auth.currentUser!!
        // Firebase Account document
        val data: HashMap<String, Any> = hashMapOf(
            "email" to email,
            "role" to "Vendor",
            "UID" to user.uid,
            "accountStatus" to "Activated"
        )
        val accountDocRef = db.collection("Accounts").document(user.uid)
        accountDocRef.set(data)
            .addOnSuccessListener { onWriteVendorAccountDocSucceeded(accountDocRef, name, phone, address, email) }
            .addOnFailureListener { onSignUpFailed() }
    }

    private fun onWriteVendorAccountDocSucceeded(accountDocRef: DocumentReference, name: String, phone: String, address: String, email: String) {
        // Firebase Vendor document
        val data = hashMapOf(
            "accountID" to accountDocRef.id,
            "email" to email,
            "name" to name,
            "phoneNumber" to phone,
            "address" to address,
            "rating" to 5.0f,
        )
        val vendorDocRef = db.collection("Vendors").document(accountDocRef.id)
        vendorDocRef.set(data)
            .addOnSuccessListener {
                val locationData = hashMapOf(
                    "accountID" to accountDocRef.id,
                    "address" to getString(R.string.hcmus_address),
                    "latitude" to getString(R.string.hcmus_lat),
                    "longitude" to getString(R.string.hcmus_long),
                )
                val locationDocRef = db.collection("Locations").document(accountDocRef.id)
                locationDocRef.set(locationData)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            Utils.typeUser = 1
                            Utils.vendor = UserVendor(
                                data["accountID"] as String,
                                data["name"] as String,
                                data["phoneNumber"] as String,
                                data["address"] as String,
                                data["rating"] as Float,
                            )
                            startActivity(Intent(this, MainActivity::class.java))
                        }
                        else {
                            onSignUpFailed()
                        }
                    }
            }
            .addOnFailureListener {
                onSignUpFailed()
            }
    }
}