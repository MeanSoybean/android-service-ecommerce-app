package com.example.androiddevelopmentgroup7

import android.content.ContentValues
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import android.util.Log
import com.example.androiddevelopmentgroup7.profile.ChangePasswordActivity
import com.example.androiddevelopmentgroup7.profile.Profile
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase


class MainActivity : AppCompatActivity() {
    lateinit var database: FirebaseFirestore
    lateinit var profile: Profile

    lateinit var changePasswordText: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fragment_profile)
        setComponent()

        getProfile("fminhtu@gmail.com")
    }

    fun setComponent() {
        database = Firebase.firestore

        this.changePasswordText = this.findViewById<TextView>(R.id.profile_password_tv)
        this.changePasswordText.setOnClickListener() {
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

