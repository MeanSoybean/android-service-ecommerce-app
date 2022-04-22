package com.example.androiddevelopmentgroup7

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class SignUpActivity : AppCompatActivity(), ICustomerSignUp, IVendorSignUp {
    private val auth = Firebase.auth
    private val db = Firebase.firestore

    //TODO: firebase writing not working right now, need to debug
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)
    }

    override fun signUpCustomer(name: String, email: String, password: String) {
        // Auth create
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    val user = auth.currentUser
                    user?.let {
                        // Firebase Account document
                        var data: HashMap<String, Any> = hashMapOf(
                            "Email" to email,
                            "Password" to password,
                            "Role" to "Customer",
                            "UID" to it.uid,
                            "AccountStatus" to "Activated"
                        )
                        val accountDocRef = db.collection("Accounts").document()
                        accountDocRef.set(data)
                            .addOnSuccessListener { Log.d("TAG", "Account DocRef written!") }
                            .addOnFailureListener { e -> Log.d("TAG", "Error writing Account", e) }
                        // Firebase Customer document
                        data = hashMapOf(
                            "AccountID" to accountDocRef.id,
                            "Name" to name,
                            "Rating" to 5
                        )
                        val customerDocRef = db.collection("Customers").document()
                        customerDocRef.set(data)
                            .addOnSuccessListener { Log.d("TAG", "Customer DocRef written!") }
                            .addOnFailureListener { e -> Log.d("TAG", "Error writing Customer", e) }
                        // Exit this activity and launch the corresponding Customer activity
                        val intent = Intent()
                        startActivity(intent)
                        finish()
                        //TODO: launch customer activity
                    }
                } else {
                    // If sign in fails, display a message to the user.
                    //TODO: Implement a sign up failed message
                }
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
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    val user = auth.currentUser
                    user?.let {
                        // Firebase Account document
                        var data: HashMap<String, Any> = hashMapOf(
                            "Email" to email,
                            "Password" to password,
                            "Role" to "Vendor",
                            "UID" to it.uid,
                            "AccountStatus" to "Activated"
                        )
                        val accountDocRef = db.collection("Accounts").document()
                        accountDocRef.set(data)

                        //TODO: VendorRegistrationForm document

                        // Firebase Vendor document
                        data = hashMapOf(
                            "AccountID" to accountDocRef.id,
                            "Name" to name,
                            "PhoneNumber" to phone,
                            "Address" to address,
                            "Rating" to 5
                        )
                        val customerDocRef = db.collection("Vendors").document()
                        customerDocRef.set(data)
                            .addOnSuccessListener { Log.d("TAG", "Vendor DocRef written!") }
                            .addOnFailureListener { e -> Log.d("TAG", "Error writing Vendor", e) }
                        // Exit this activity and launch the corresponding Vendor activity
                        val intent = Intent()
                        startActivity(intent)
                        finish()
                        //TODO: launch vendor activity
                    }
                } else {
                    // If sign in fails, display a message to the user.
                    //TODO: Implement a sign up failed message
                }
            }
    }

}