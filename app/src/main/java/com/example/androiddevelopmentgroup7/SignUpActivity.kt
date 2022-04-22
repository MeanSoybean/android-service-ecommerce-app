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
                    Log.d("TAG", "Successful task!")
                    user?.let {
                        Log.d("TAG", "Non-null user!")
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
                            .addOnSuccessListener { Log.i("TAG", "Account DocRef written!") }
                            .addOnFailureListener { e -> Log.i("TAG", "Error writing Account", e) }
                        // Firebase Customer document
                        data = hashMapOf(
                            "AccountID" to accountDocRef.id,
                            "Name" to name,
                            "Rating" to 5
                        )
                        val customerDocRef = db.collection("Customers").document()
                        customerDocRef.set(data)
                            .addOnSuccessListener { Log.i("TAG", "Customer DocRef written!") }
                            .addOnFailureListener { e -> Log.i("TAG", "Error writing Customer", e) }
                        // Exit this activity and launch the corresponding Customer activity
                        //val intent = Intent()
                        //startActivity(intent)
                        // finish()
                        //TODO: launch customer activity AFTER data has been written
                        //TODO: what to do when data has not yet been written?
                        //TODO: wait for data to be written, if not written, fail and exit
                    }
                } else {
                    // If sign in fails, display a message to the user.
                    //TODO: Implement a sign up failed message
                    Log.d("TAG", "Successful task!")
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
                            .addOnSuccessListener { Log.i("TAG", "Vendor DocRef written!") }
                            .addOnFailureListener { e -> Log.i("TAG", "Error writing Vendor", e) }
                        // Exit this activity and launch the corresponding Vendor activity
                        //val intent = Intent()
                        //startActivity(intent)
                        //finish()
                        //TODO: launch vendor activity
                    }
                } else {
                    // If sign in fails, display a message to the user.
                    //TODO: Implement a sign up failed message
                }
            }
    }

}