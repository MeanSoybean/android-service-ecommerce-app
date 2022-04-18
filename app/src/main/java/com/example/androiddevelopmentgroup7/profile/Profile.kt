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


//    @JvmName("setAccountID1")
//    fun setAccountID(accountID: String) {
//        this.accountID = accountID
//    }
//
//    @JvmName("setName1")
//    fun setName(name: String) {
//        this.name = name
//    }
//
//    @JvmName("setPhoneNumber1")
//    fun setPhoneNumber(phoneNumber: String) {
//        this.phoneNumber = phoneNumber
//    }
//
//    @JvmName("setAddress1")
//    fun setAddress(address: String) {
//        this.address = address
//    }
//
//    @JvmName("setRating1")
//    fun setRating(rating: Long) {
//        this.rating = rating
//    }
//
//    @JvmName("setPaymentDetails1")
//    fun setPaymentDetails(paymentDetails: String) {
//        this.paymentDetails = paymentDetails
//    }

    fun showAccountID() {
        Log.w(ContentValues.TAG, "Profile: " + accountID)
    }

}