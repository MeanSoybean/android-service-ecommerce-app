package com.example.androiddevelopmentgroup7.models

data class UserVendor(
    var accountID: String,
    var name:String,
    var phoneNumber:String,
    var address:String,
    var rating: Float,
){
    var id: String = ""
}