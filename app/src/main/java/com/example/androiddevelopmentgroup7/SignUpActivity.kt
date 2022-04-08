package com.example.androiddevelopmentgroup7

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class SignUpActivity : AppCompatActivity() {
    private lateinit var emailET: EditText
    private lateinit var passwordET: EditText
    private lateinit var signUpButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)

        auth = Firebase.auth
        emailET = findViewById(R.id.activity_sign_up_email_ET)
        passwordET = findViewById(R.id.activity_sign_up_password_ET)
        signUpButton = findViewById(R.id.activity_sign_up_button)
        signUpButton.setOnClickListener{
            createAccount(emailET.text.toString(), passwordET.text.toString())
        }
    }

    private lateinit var auth: FirebaseAuth
    private fun createAccount(email: String, password: String) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    val user = auth.currentUser
                    user?.let{
                        val displayName = user.displayName
                        val emailAddress = user.email

                        // Check if user's email is verified
                        val emailVerified = user.isEmailVerified

                        Toast.makeText(baseContext, "Sign up successful.\nName: $displayName.\nEmail: $emailAddress\nVerified: $emailVerified",
                            Toast.LENGTH_LONG).show()
                    }
                } else {
                    // If sign in fails, display a message to the user.
                    Toast.makeText(baseContext, "Authentication failed.",
                        Toast.LENGTH_SHORT).show()
                }
            }
    }

}