package com.example.androiddevelopmentgroup7.models

import com.google.firebase.Timestamp

data class Notification(
    var notiID:String,
    var accountID:String,
    var Name:String,
    var Description: String,
    var Time :Timestamp
)