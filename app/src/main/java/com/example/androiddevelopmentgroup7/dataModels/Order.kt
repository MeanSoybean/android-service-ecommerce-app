package com.example.androiddevelopmentgroup7.dataModels

data class Order(
    var idOrder:String,
    var idVendor:String,
    var idCustomer:String,
    var serviceName:String,
    var nameVendor:String,
    var timeOrder:String,
    var timeComing:String,
    var orderAddress:String,
    var IdCurrent:String,
    var orderCurrent:String,
    var price:String,
    var serviceImage:String,
)