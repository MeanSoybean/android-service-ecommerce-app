package com.example.androiddevelopmentgroup7.viewModels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.androiddevelopmentgroup7.dataModels.Service
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase


class ServiceViewModel :  ViewModel(){
    val db = Firebase.firestore
    private val serviceList: MutableLiveData<ArrayList<Service>> = MutableLiveData<ArrayList<Service>>()

    val selectedServiceList: LiveData<ArrayList<Service>> get() = serviceList

    fun setServiceList(serviceListArg: ArrayList<Service>){
//        serviceList.value = serviceListArg
        val temp = ArrayList<Service>()
        serviceList.value = ArrayList<Service>()
        db.collection("Vendor").document("CbcUnjIZh9tqHrxeuxEP")
            .collection("ServiceListing")
            .get()
            .addOnSuccessListener { services ->
                Log.i("success", services.size().toString())
                for(service in services){
                    val serviceTemp = Service(
                        "Sua Ong Nuoc",
                        service.data.get("Name").toString(),
                        service.data.get("Description").toString(),
                        service.data.get("Price").toString(),
                        "",
                        "",
                        )
                    temp.add(serviceTemp)
                    Log.i("aaa", temp.size.toString())
                }

                serviceList.value = temp
            }
            .addOnFailureListener { exception ->
                Log.i("ASD", "Error getting documents.", exception)
            }

//        val service = Service(
//            "Sửa đồ gia dụng",
//            "Sửa tivi tận nơi",
//            "Nhận sửa các loại tivi công nghệ cao, an toàn, nhanh chống",
//            "Giá cả thương lượng","123",
//            "")
//        val temp = ArrayList<Service>()
//        temp.add(service)
//        temp.add(service)
//        temp.add(service)
        Log.i("lam", "Lam f")

    }

    fun addServiceToList(service: Service){
        serviceList.value?.add(service)
        Log.i("AAA", serviceList.value?.size.toString())
    }
}

