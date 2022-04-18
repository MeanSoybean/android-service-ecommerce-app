package com.example.androidgr7.dataModels

data class Service(
    var serviceType:String,
    var serviceName:String,
    var serviceDescription:String,
    var servicePrice: String,
    var serviceContact:String,
    var serviceImage:String,
){
    var serviceRating:Double = 0.0
}

