package com.example.androiddevelopmentgroup7.views.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.example.androiddevelopmentgroup7.R
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class ForgotPasswordActivity : AppCompatActivity() {
    private lateinit var emailET: EditText
    private lateinit var confirmButton: Button
    private lateinit var cancelTV: TextView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forgot_password)

        setupElements()
    }

    private fun setupElements() {
        emailET = findViewById(R.id.email_edit_text)
        confirmButton = findViewById(R.id.confirm_button)
        confirmButton.setOnClickListener {
            val email = emailET.text.toString()
            if (!email.isBlank()) {
                Firebase.auth.sendPasswordResetEmail(email)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            Toast.makeText(
                                this,
                                getString(R.string.reset_email_sent),
                                Toast.LENGTH_SHORT
                            ).show()
                        } else {
                            Toast.makeText(
                                this,
                                getString(R.string.reset_email_failed),
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
            }
            else {

                Toast.makeText(this, getString(R.string.reset_email_empty), Toast.LENGTH_SHORT).show()
            }
        }
        cancelTV = findViewById(R.id.go_back)
        cancelTV.setOnClickListener { startActivity(Intent(this, SignInActivity::class.java)) }
    }
}