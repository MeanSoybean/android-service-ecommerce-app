package com.example.androiddevelopmentgroup7.models

import com.google.firebase.Timestamp

data class Order(
    var idVendor:String,
    var idCustomer:String,
    var idService:String,
    var timeOrder:Timestamp,
    var timeComing:String,
    var orderAddress:String,
    var orderCurrent:Int,
    var price:Long,
    var phoneNumber: String,
    var customerName: String,
){
    var idOrder: String = ""
}