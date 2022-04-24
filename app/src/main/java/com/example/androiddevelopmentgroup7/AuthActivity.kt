package com.example.androiddevelopmentgroup7

import android.app.ProgressDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.FirebaseAuthUIActivityResultContract
import com.firebase.ui.auth.data.model.FirebaseAuthUIAuthenticationResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class AuthActivity : AppCompatActivity() {
    val db = Firebase.firestore

    private val signInLauncher = registerForActivityResult(
        FirebaseAuthUIActivityResultContract()
    ) { res ->
        this.onSignInResult(res)
    }

    private lateinit var buttonSignIn: Button
    private lateinit var buttonSignUp: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_auth)

        buttonSignIn = findViewById(R.id.button_sign_in)
        buttonSignIn.setOnClickListener { startSignInActivity() }

        buttonSignUp = findViewById(R.id.button_sign_up)
        buttonSignUp.setOnClickListener{ startSignUpActivity() }
    }

    private fun startSignUpActivity() {
        intent = Intent(this, SignUpActivity::class.java)
        startActivity(intent)
    }

    private fun startSignInActivity() {
        // Choose authentication providers
        val providers = arrayListOf(
            AuthUI.IdpConfig.EmailBuilder()
                .setRequireName(false)
                .setAllowNewAccounts(false)
                .build(),
            AuthUI.IdpConfig.PhoneBuilder().build(),
            AuthUI.IdpConfig.GoogleBuilder().build(),
            AuthUI.IdpConfig.FacebookBuilder().build()
        )

        // Create and launch sign-in intent
        val signInIntent = AuthUI.getInstance()
            .createSignInIntentBuilder()
            .setAvailableProviders(providers)
            .build()
        signInLauncher.launch(signInIntent)
    }

    private fun onSignInResult(result: FirebaseAuthUIAuthenticationResult) {
        val response = result.idpResponse
        if (result.resultCode == RESULT_OK) {
            // Successfully signed in
            val user = FirebaseAuth.getInstance().currentUser
            val pDialog: ProgressDialog =  ProgressDialog(this);
            pDialog.setMessage("please wait...");
            pDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            pDialog.setCancelable(false);
            pDialog.show();

            db.collection("Accounts")
                .whereEqualTo("UID", user!!.uid)
                .get()
                .addOnSuccessListener { acc ->
                    pDialog.dismiss()
                    if(acc.documents[0].data!!.get("Role").toString().equals("Vendor")){
                        val homeIntent = Intent(this, MainActivity::class.java)
                        startActivity(homeIntent)
                    }
                    else {
                        Toast.makeText(this, "You are customer", Toast.LENGTH_LONG).show();
                    }
                }
            //TODO: On Signed In
            // ...

        } else {
            // Sign in failed. If response is null the user canceled the
            // sign-in flow using the back button. Otherwise check
            // response.getError().getErrorCode() and handle the error.
            // ...
        }
    }
}
