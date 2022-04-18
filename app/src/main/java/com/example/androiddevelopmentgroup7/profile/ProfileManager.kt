package com.example.androiddevelopmentgroup7.profile

import android.content.ContentValues
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.androiddevelopmentgroup7.R
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class ProfileManager: AppCompatActivity() {
    lateinit var database: FirebaseFirestore
    var profile: Profile ?= null

    lateinit var changePasswordText: TextView
    lateinit var update_btn: Button

    var profile_email_tv: TextView?= null
    var profile_mobile_tv: TextView?= null
    var profile_name_tv: TextView?= null
    var profile_address_tv: TextView?= null
    var profile_rating_tv: TextView?= null
    var profile_payment_details_tv: TextView?= null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fragment_profile)

        initComponent()
        getProfile("fminhtu@gmail.com")
//        updateProfile("fminhtu@gmail.com")
    }

    private fun initComponent() {
        database = Firebase.firestore
        this.profile_email_tv = this.findViewById<TextView>(R.id.profile_email_tv)
        this.profile_mobile_tv = this.findViewById<TextView>(R.id.profile_mobile_tv)
        this.profile_name_tv = this.findViewById<TextView>(R.id.profile_name_tv)
        this.profile_address_tv = this.findViewById<TextView>(R.id.profile_address_tv)
        this.profile_rating_tv = this.findViewById<TextView>(R.id.profile_rating_tv)
        this.profile_payment_details_tv = this.findViewById<TextView>(R.id.profile_payment_details_tv)
        this.changePasswordText = this.findViewById<TextView>(R.id.profile_password_tv)
        this.update_btn = this.findViewById<Button>(R.id.update_btn)

        this.changePasswordText.setOnClickListener() {
            val intent = Intent(this, ChangePasswordActivity::class.java)
            startActivity(intent)
        }

        this.update_btn.setOnClickListener() {
            updateProfile()
            Toast.makeText(this, "The information were updated successfully.", Toast.LENGTH_SHORT).show()
        }
    }

    private fun displayDataInFragment(profile: Profile) {
        profile_email_tv!!.setText(profile.accountID)
        profile_mobile_tv!!.setText(profile.phoneNumber)
        profile_name_tv!!.setText(profile.name)
        profile_address_tv!!.setText(profile.address)
        profile_rating_tv!!.setText(profile.rating.toString())
        profile_payment_details_tv!!.setText(profile.paymentDetails)
    }


    private fun getProfile(accountID: String) {
        database.collection("Customers")
            .whereEqualTo("accountID", accountID)
            .get()
            .addOnSuccessListener { result ->
                for (document in result) {
                    this.profile = Profile(
                        document.data.get("accountID") as String,
                        document.data.get("name") as String,
                        document.data.get("phoneNumber") as String,
                        document.data.get("address") as String,
                        document.data.get("rating") as Long,
                        document.data.get("paymentDetails") as String
                    )
                    profile!!.showAccountID()
                    displayDataInFragment(profile!!)
                }
            }
            .addOnFailureListener { exception ->
                Log.w(ContentValues.TAG, "Error getting documents.", exception)
            }
    }

    private fun updateProfile() {
        val new_profile = Profile(
            this.profile_email_tv!!.getText().toString(),
            profile_name_tv!!.getText().toString(),
            profile_mobile_tv!!.getText().toString(),
            profile_address_tv!!.getText().toString(),
            profile_rating_tv!!.getText().toString().toLong(),
            profile_payment_details_tv!!.getText().toString())

        database.collection("Customers")
            .document("Customer")
            .set(new_profile)

    }
}