package com.example.androiddevelopmentgroup7.models

data class UserCustomer(
    var accountID: String,
    var name:String,
    var phoneNumber:String,
    var address:String,
    var rating:Float,
    var paymentDetails:String
){
    var id:String = ""
}
