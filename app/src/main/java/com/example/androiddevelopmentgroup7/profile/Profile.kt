package com.example.androiddevelopmentgroup7.profile

import android.content.ContentValues
import android.util.Log

data class Profile(
    var accountID: String,
    var name: String,
    var phoneNumber: String,
    var address: String,
    var rating: Long,
    var paymentDetails: String
) {

    fun showAccountID() {
        Log.w(ContentValues.TAG, "Profile: " + accountID)
    }

}