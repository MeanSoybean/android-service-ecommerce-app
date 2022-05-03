package com.example.androiddevelopmentgroup7.viewModels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.androiddevelopmentgroup7.models.Notification
import com.example.androiddevelopmentgroup7.models.Service
import com.example.androiddevelopmentgroup7.utils.Utils
import com.google.firebase.Timestamp
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class NotificationViewModel : ViewModel()  {
    private val db = Firebase.firestore
    val auth = Firebase.auth
    private val serviceList: MutableLiveData<ArrayList<Notification>> =
        MutableLiveData<ArrayList<Notification>>()
    val status: MutableLiveData<String> = MutableLiveData("loading")
    val selectedServiceList: LiveData<ArrayList<Notification>> get() = serviceList
    fun setServiceList() {
        status.value = "loading"
//        serviceList.value = serviceListArg
        val temp = ArrayList<Notification>()
        serviceList.value = temp
        var tempID :String =auth.currentUser!!.uid
//        Log.i("VENDORID", Utils.vendor.id)
        db.collection("Notifications")
            .whereEqualTo("accountID", tempID)
            .orderBy("time", Query.Direction.DESCENDING)
            .get()
            .addOnSuccessListener { notifications ->
                Log.i("success", notifications.size().toString())
                for (notification in notifications) {
                    val notificationTemp = Notification(
                        notification.id,
                        notification.data.get("accountID").toString(),
                        notification.data.get("Name").toString(),
                        notification.data.get("Description").toString(),
                        notification.data.get("time") as Timestamp,
                    )
                    //Log.i("ServiceRating", serviceTemp.serviceRating.toString())
                    temp.add(notificationTemp)
                }
                serviceList.value = temp
                status.value = "hide_loader"
            }
            .addOnFailureListener { exception ->
                Log.i("ERROR", "Error getting documents.", exception)
            }

    }


}