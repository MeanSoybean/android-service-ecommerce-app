package com.example.androiddevelopmentgroup7.viewModels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.androiddevelopmentgroup7.dataModels.Service



class ServiceViewModel :  ViewModel(){
    private val serviceList: MutableLiveData<ArrayList<Service>> = MutableLiveData<ArrayList<Service>>()

    val selectedServiceList: LiveData<ArrayList<Service>> get() = serviceList

    fun setServiceList(serviceListArg: ArrayList<Service>){
//        serviceList.value = serviceListArg
        val service = Service(
            "Sửa đồ gia dụng",
            "Sửa tivi tận nơi",
            "Nhận sửa các loại tivi công nghệ cao, an toàn, nhanh chống",
            "Giá cả thương lượng","123",
            "")
        val temp = ArrayList<Service>()
        temp.add(service)
        temp.add(service)
        temp.add(service)
        serviceList.value = temp
    }

    fun addServiceToList(service: Service){
        serviceList.value?.add(service)
        Log.i("AAA", serviceList.value?.size.toString())
    }
}

