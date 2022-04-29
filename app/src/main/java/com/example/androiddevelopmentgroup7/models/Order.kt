package com.example.androiddevelopmentgroup7.models

data class Order(
    var idVendor:String,
    var idCustomer:String,
    var idService:String,
    var timeOrder:String,
    var timeComing:String,
    var orderAddress:String,
    var orderCurrent:Int,
    var price:Long,
    var phoneNumber: String,
){
    var idOrder: String = ""
}