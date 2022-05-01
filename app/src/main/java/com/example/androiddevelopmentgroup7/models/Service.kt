package com.example.androiddevelopmentgroup7.models

data class Service(
    var serviceType:String,
    var serviceName:String,
    var serviceDescription:String,
    var servicePrice: Long,
    var servicePhoneNumber:String,
    var serviceImage:String,
    var serviceRating: Float,
    var vendorID: String,
    var vendorName: String,
    //var negotiate: Boolean
) {
    var serviceID: String = ""
}

