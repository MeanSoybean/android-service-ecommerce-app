package com.example.androidgr7.viewModels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.androidgr7.dataModels.Service



class ServiceViewModel :  ViewModel(){
    private val serviceList: MutableLiveData<ArrayList<Service>> = MutableLiveData<ArrayList<Service>>()

    val selectedServiceList: LiveData<ArrayList<Service>> get() = serviceList

    fun setServiceList(serviceListArg: ArrayList<Service>){
          serviceList.value = serviceListArg
/*        val service = Service(
            "Sửa đồ gia dụng",
            "Sửa tivi tận nơi",
            "Nguyen Van A",
            "Nhận sửa các loại tivi công nghệ cao, an toàn, nhanh chống",
            "Giá cả thương lượng","123",
            "")
        val service1 = Service(
            "Sửa điện",
            "Sửa điện tận nhà",
            "Huynh Van C",
            "Nhận sửa điện tới tận nơi, nhiệt tình, an toàn",
            "Giá cả thương lượng","123",
            "")
        val service2 = Service(
            "Sửa ống nước",
            "Sửa ống nước tận nơi",
            "Tran Van D",
            "Nhận sửa các loại ống nước tại gia, uy tín",
            "Giá cả thương lượng","123",
            "")
        val temp = ArrayList<Service>()
        temp.add(service)
        temp.add(service1)
        temp.add(service2)
        serviceList.value = temp*/
    }

    fun addServiceToList(service: Service){
        serviceList.value?.add(service)
        Log.i("AAA", serviceList.value?.size.toString())
    }
}

