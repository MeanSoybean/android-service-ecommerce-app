package com.example.androidgr7.dataModels

import java.util.*

data class Order(
    var idOrder:String,
    var idCustomer:String,
    var serviceName:String,
    var nameVendor:String,
    var timeOrder:String,
    var timeComing:String,
    var orderAddress:String,
    var orderCurrent:String,
    var price:String,
    var serviceImage:String,
)