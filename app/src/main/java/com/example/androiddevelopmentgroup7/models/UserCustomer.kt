package com.example.androiddevelopmentgroup7.models

data class UserCustomer(
    var AccountID: String,
    var Name:String,
    var PhoneNumber:String,
    var Address:String,
    var Rating:Float,
    var PaymentDetails:String
){
    var id:String = ""
}
