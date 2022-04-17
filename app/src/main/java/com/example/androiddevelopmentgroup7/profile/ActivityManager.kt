package com.example.androiddevelopmentgroup7.profile

import android.content.ContentValues
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.androiddevelopmentgroup7.R
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class ActivityManager : AppCompatActivity() {
    lateinit var database: FirebaseFirestore
    lateinit var profile: Profile

    var changePasswordText: TextView ?= null
    var profile_mobile_tv: TextView ?= null
    var profile_name_tv: TextView ?= null
    var profile_address_tv: TextView ?= null
    var profile_payment_detail_tv: TextView ?= null
    var profile_email_tv: TextView ?= null
    var profile_rating_tv: TextView ?= null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fragment_profile)
        setComponent()

        getProfile("fminhtu@gmail.com")
        setProfileDataForScreen()
    }

    fun setComponent() {
        database = Firebase.firestore

        this.changePasswordText = this.findViewById<TextView>(R.id.profile_password_tv)
        this.profile_mobile_tv = this.findViewById<TextView>(R.id.profile_mobile_tv)
        this.profile_name_tv= this.findViewById<TextView>(R.id.profile_name_tv)
        this.profile_address_tv = this.findViewById<TextView>(R.id.profile_address_tv)
        this.profile_payment_detail_tv = this.findViewById<TextView>(R.id.profile_payment_details_tv)
        this.profile_email_tv = this.findViewById<TextView>(R.id.profile_email_tv)
        this.profile_rating_tv = this.findViewById<TextView>(R.id.profile_rating_tv)

        this.changePasswordText!!.setOnClickListener() {
            val intent = Intent(this, ChangePasswordActivity::class.java)
            startActivity(intent)
        }
    }

    fun getProfile(accountID: String) {
        database.collection("Customers")
            .whereEqualTo("AccountID", accountID)
            .get()
            .addOnSuccessListener { result ->
                for (document in result) {
                    val data =  document.data
                    profile = Profile(
                        data.get("AccountID") as String,
                        data.get("Name") as String,
                        data.get("PhoneNumber") as String,
                        data.get("Address") as String,
                        data.get("Rating") as Long,
                        data.get("PaymentDetails") as String
                    )
                    profile!!.showAccountID()
                }
            }
            .addOnFailureListener { exception ->
                Log.w(ContentValues.TAG, "Error getting documents.", exception)
            }
    }

    fun setProfileDataForScreen() {
        this.profile_mobile_tv!!.setText(profile.phoneNumber)
        this.profile_name_tv!!.setText(profile.name)
        this.profile_address_tv!!.setText(profile.address)
        this.profile_payment_detail_tv!!.setText(profile.paymentDetails)
        this.profile_email_tv!!.setText(profile.accountID)
        this.profile_rating_tv!!.setText(profile.rating as Int)
    }

    fun updateProfile(accountID: String) {
        var profile_mobile_tv: TextView = this.findViewById<TextView>(R.id.profile_mobile_tv)
        var profile_name_tv: TextView = this.findViewById<TextView>(R.id.profile_name_tv)
        var profile_address_tv: TextView = this.findViewById<TextView>(R.id.profile_address_tv)
        var payment_detail_tv: TextView = this.findViewById<TextView>(R.id.profile_payment_details_tv)

        var new_profile: Profile = Profile(
            profile.accountID,
            profile_name_tv.text.toString(),
            profile_mobile_tv.text.toString(),
            profile_address_tv.text.toString(),
            profile.rating,
            payment_detail_tv.text.toString(),
        )

        profile = new_profile



    }
}