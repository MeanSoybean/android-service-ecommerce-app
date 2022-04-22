package com.example.androidgr7.viewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.androidgr7.dataModels.Notification
import com.example.androidgr7.dataModels.UserCustomer


internal class NotificationViewModel  : ViewModel(){
    val notiList: MutableLiveData<ArrayList<Notification>> =
        MutableLiveData<ArrayList<Notification>>()
    val selectedServiceList: LiveData<ArrayList<Notification>> get() = notiList
    fun setServiceList(serviceListArg: ArrayList<Notification>) {
        notiList.value = serviceListArg
    }
}