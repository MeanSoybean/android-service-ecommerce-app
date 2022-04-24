package com.example.androiddevelopmentgroup7.dataModels

data class Service(

    var serviceType:String,
    var serviceName:String,
    var serviceDescription:String,
    var servicePrice: String,
    var servicePhoneNumber:String,
    var serviceImage:String,
    var serviceRating: Float,
    var vendorID: String,
    var vendorName: String,
) {
    var serviceID: String = ""
}

