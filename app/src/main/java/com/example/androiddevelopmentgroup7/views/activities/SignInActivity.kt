package com.example.androiddevelopmentgroup7.views.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.*
import com.example.androiddevelopmentgroup7.R
import com.example.androiddevelopmentgroup7.models.UserCustomer
import com.example.androiddevelopmentgroup7.models.UserVendor
import com.example.androiddevelopmentgroup7.utils.Utils
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class SignInActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    private lateinit var db: FirebaseFirestore
    private lateinit var emailET: EditText
    private lateinit var passwordET: EditText
    private lateinit var signupPrompt: TextView
    private lateinit var signinButton: Button
    private lateinit var loader: FrameLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_in)

        setupElements()
        signInIfAlreadyLoggedIn()
    }

    private fun signInIfAlreadyLoggedIn() {
        val user = auth.currentUser
        user?.let {
            loader.visibility = View.VISIBLE
            onSignInSucceeded()
        }
    }

    private fun setupElements() {
        auth = Firebase.auth
        db = Firebase.firestore
        loader = findViewById(R.id.loader_layout)
        emailET = findViewById(R.id.signin_email_edit_text)
        passwordET = findViewById(R.id.signin_password_edit_text)

        signupPrompt = findViewById(R.id.signup_prompt)
        signupPrompt.setOnClickListener {
            startActivity(Intent(this, SignUpActivity::class.java))
        }

        signinButton = findViewById(R.id.signin_confirm_button)
        signinButton.setOnClickListener { onClickSignInButton() }
    }

    private fun onClickSignInButton() {
        val email = emailET.text.toString()
        val password = passwordET.text.toString()
        if (isDataValid()) {
            loader.visibility = View.VISIBLE
            auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        onSignInSucceeded()
                    } else {
                        onSignInFailed()
                    }
                }
        } else {
            onSignInFailed()
        }
    }

    private fun onSignInFailed() {
        Toast.makeText(this, getString(R.string.could_not_sign_in), Toast.LENGTH_SHORT).show()
        loader.visibility = View.GONE
    }

    private fun onSignInSucceeded() {
        val user = auth.currentUser!!
        db.collection("Accounts")
            .document(user.uid)
            .get()
            .addOnSuccessListener{ onAccountDocumentFound(it) }
    }

    private fun onAccountDocumentFound(doc: DocumentSnapshot?) {
        when (doc?.getString("role")) {
            "Customer" -> handleRoleIsCustomerOnLogin(doc)
            "Vendor" -> handleRoleIsVendorOnLogin(doc)
            "Admin" -> handleRoleIsAdminOnLogin(doc)
            else -> throw Exception("Bad role on retrieved Account document!\n")
        }
    }

    private fun handleRoleIsAdminOnLogin(accountDocumentSnapshot: DocumentSnapshot) {
        TODO("Not yet implemented")
    }

    private fun handleRoleIsVendorOnLogin(accountDocumentSnapshot: DocumentSnapshot) {
        Utils.typeUser = 1

        db.collection("Vendors")
            .document(accountDocumentSnapshot.id)
            .get()
            .addOnSuccessListener { doc ->
                Utils.vendor = UserVendor(
                    doc.id,
                    doc.getString("name").toString(),
                    doc.getString("phoneNumber").toString(),
                    doc.getString("address").toString(),
                    doc.get("rating").toString().toFloat(),
                )
                startActivity(Intent(this, MainActivity::class.java))
            }
            .addOnFailureListener {
                onSignInFailed()
            }
    }

    private fun handleRoleIsCustomerOnLogin(accountDocumentSnapshot: DocumentSnapshot) {
        Utils.typeUser = 0

        db.collection("Customers")
            .document(accountDocumentSnapshot.id)
            .get()
            .addOnSuccessListener { doc ->
                Utils.customer = UserCustomer(
                    doc.id,
                    doc.getString("name").toString(),
                    doc.getString("phoneNumber").toString(),
                    doc.getString("address").toString(),
                    doc.get("rating").toString().toFloat(),
                    doc.getString("paymentDetails").toString(),
                )
                startActivity(Intent(this, MainActivity::class.java))
            }
            .addOnFailureListener {
                onSignInFailed()
            }
    }

    private fun isDataValid(): Boolean {
            val email = emailET.text.toString()
            val pass1 = passwordET.text.toString()
            return email.isNotBlank()
                    && pass1.isNotBlank()
    }

}