package com.example.androiddevelopmentgroup7.viewModels

import android.net.Uri
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.androiddevelopmentgroup7.utils.Utils
import com.example.androiddevelopmentgroup7.models.Service
import com.google.firebase.Timestamp
import com.google.firebase.firestore.FieldPath
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap


class ServiceViewModel :  ViewModel() {
    private val db = Firebase.firestore
    private val serviceList: MutableLiveData<ArrayList<Service>> =
        MutableLiveData<ArrayList<Service>>()

    val selectedServiceList: LiveData<ArrayList<Service>> get() = serviceList

    val status: MutableLiveData<String> = MutableLiveData("loading")

    val serviceTypeLivaData: MutableLiveData<ArrayList<String>> =
        MutableLiveData<ArrayList<String>>()

    private val storage = Firebase.storage
    private val storageRef = storage.reference


    fun setServiceList() {
        status.value = "loading"
//        serviceList.value = serviceListArg
        val temp = ArrayList<Service>()
        serviceList.value = temp
//        Log.i("VENDORID", Utils.vendor.id)
        db.collection("ServiceListings")
            .whereEqualTo("vendorID", Utils.vendor.accountID)
            .orderBy("createdAt", Query.Direction.DESCENDING)
            .get()
            .addOnSuccessListener { services ->
                Log.i("success", services.size().toString())
                for (service in services) {
                    val serviceTemp = Service(
                        service.data.get("serviceType").toString(),
                        service.data.get("serviceName").toString(),
                        service.data.get("serviceDescription").toString(),
                        service.data.get("servicePrice").toString().toLong(),
                        service.data.get("servicePhoneNumber").toString(),
                        service.data.get("serviceImage").toString(),
                        service.data.get("serviceRating").toString().toFloat(),
                        service.data.get("vendorID").toString(),
                        service.data.get("vendorName").toString(),
                        //service.data.get("negotiate").toString().toBoolean()
                    )
                    serviceTemp.serviceID = service.id
                    //Log.i("ServiceRating", serviceTemp.serviceRating.toString())
                    temp.add(serviceTemp)
                }
                serviceList.value = temp
                status.value = "hide_loader"
            }
            .addOnFailureListener { exception ->
                Log.i("ERROR", "Error getting documents.", exception)
            }

    }


    fun setServiceListForUser() {
        status.value = "loading"
        val temp = ArrayList<Service>()
        serviceList.value = temp
        db.collection("ServiceListings")
            .get()
            .addOnSuccessListener { services ->
                Log.i("success", services.size().toString())
                for (service in services) {
                    val serviceTemp = Service(
                        service.data.get("serviceType").toString(),
                        service.data.get("serviceName").toString(),
                        service.data.get("serviceDescription").toString(),
                        service.data.get("servicePrice").toString().toLong(),
                        service.data.get("servicePhoneNumber").toString(),
                        service.data.get("serviceImage").toString(),
                        service.data.get("serviceRating").toString().toFloat(),
                        service.data.get("vendorID").toString(),
                        service.data.get("vendorName").toString(),
                        //service.data.get("negotiate").toString().toBoolean(),
                    )
                    serviceTemp.serviceID = service.id
                    temp.add(serviceTemp)
                }
                serviceList.value = temp
                status.value = "hide_loader"
            }
            .addOnFailureListener { exception ->
                Log.i("ERROR", "Error getting documents.", exception)
            }

    }

    fun addServiceToList(service:Service) {
        db.collection("ServiceListings")
            .add(service)
            .addOnSuccessListener { documentRefer ->
                val createdAt = hashMapOf<String,Any>("createdAt" to Timestamp(Date()))
                documentRefer.update(createdAt).addOnSuccessListener {
                    status.value = "add_service_sucess"
                }
            }
    }



    fun uploadFileAndSaveService(imageUri: Uri, service:Service){
        // Create a reference to ""

        status.value = "loading"
        val file = imageUri
        val formatter = SimpleDateFormat("yyyy_MM_dd_HH_mm_ss", Locale.TAIWAN)
        val now = Date()
        val fileRef = storageRef.child("service_image/${file.lastPathSegment + formatter.format(now)}")

        val uploadTask = fileRef.putFile(file)

        // Register observers to listen for when the download is done or if it fails
        uploadTask.addOnFailureListener {
            // Handle unsuccessful uploads
        }.addOnSuccessListener { taskSnapshot ->
            //uploadTask.getResult().
            fileRef.downloadUrl.addOnSuccessListener { url ->
                //Log.i("URL", url.toString())
//                service.set("serviceImage", url.toString())
                service.serviceImage = url.toString()
                addServiceToList(service)
            }
        }
    }

    fun deleteImageFromUrl(url: String){
        val oldImageRef = storage.getReferenceFromUrl(url)
        oldImageRef.delete().addOnSuccessListener {
            Log.i("DELETE IMAGE", "SUCCESS")
        }
            .addOnFailureListener { it ->
                Log.i("DELETE IMAGE", it.toString())
            }
    }



    fun uploadFileAndUpdateService(imageUri: Uri, service: Service, position: Int){
        // Create a reference to ""
        status.value = "loading"
//        deleteImageFromUrl(service.get("serviceImage")!!)
        deleteImageFromUrl(service.serviceImage)
        val formatter = SimpleDateFormat("yyyy_MM_dd_HH_mm_ss", Locale.TAIWAN)
        val now = Date()

        val file = imageUri
        val fileRef = storageRef.child("service_image/${file.lastPathSegment + formatter.format(now)}")

        val uploadTask = fileRef.putFile(file)

        // Register observers to listen for when the download is done or if it fails
        uploadTask.addOnFailureListener {
            // Handle unsuccessful uploads
        }.addOnSuccessListener { taskSnapshot ->
            //uploadTask.getResult().
            fileRef.downloadUrl.addOnSuccessListener { url ->

                Log.i("URL", url.toString())
                service.serviceImage = url.toString()
                //service.set("serviceImage", url.toString())
                updateService(position, service)
            }
        }
    }

    fun updateService(position: Int, service: Service) {
        db.collection("ServiceListings")
            .document(serviceList.value!!.get(position).serviceID)
            .update(
                "serviceType", service.serviceType,
                "serviceName", service.serviceName,
                "serviceDescription", service.serviceDescription,
                "servicePrice", service.servicePrice,
                "servicePhoneNumber", service.servicePhoneNumber,
                "serviceImage", service.serviceImage,
                "serviceRating", service.serviceRating,
                "vendorID", service.vendorID,
                "vendorName", service.vendorName
            )
            .addOnSuccessListener { documentRefer ->
                status.value = "add_service_sucess"
                serviceList.value = serviceList.value
//                val temp = serviceList.value
//                serviceList.value = temp!!
            }

    }
    fun setServicesTypeFromDatabase() {
        status.value = "loading"
        db.collection("ServiceType").get()
            .addOnSuccessListener { serviceTypes ->
                val temp = ArrayList<String>()
                for (serviceType in serviceTypes) {
                    temp.add(serviceType.data.get("name").toString())
                }
                serviceTypeLivaData.value = temp
                status.value = "hide_loader"
            }
            .addOnFailureListener { exception ->
                Log.d("AAA", "get failed with ", exception)
            }
    }

    fun deleteService(position: Int){
        status.value = "loading"
        db.collection("ServiceListings").document(serviceList.value!!.get(position).serviceID).get()
            .addOnSuccessListener { service ->
                deleteImageFromUrl(service.get("serviceImage").toString())
                db.collection("ServiceListings").document(serviceList.value!!.get(position).serviceID).delete()
                    .addOnSuccessListener {
                        status.value = "delete_success"
                        //notify observer that data changed
                        val serviceListTemp = serviceList.value!!
                        serviceListTemp.removeAt(position)
                        serviceList.value = serviceListTemp
                    }
            }
        db.collection("OrderListing")
            .whereEqualTo("idService",serviceList.value!!.get(position).serviceID)
            .whereEqualTo("idVendor",serviceList.value!!.get(position).vendorID)
            .get()
            .addOnSuccessListener { snapshot ->
                for(doc in snapshot){
                    db.collection("OrderListing").document(doc.id).delete()
                }
            }
        db.collection("ServiceRatings").document(serviceList.value!!.get(position).serviceID).delete()
    }


    fun queryDataFilter(sortingType:String, sortingAccording:String, typeOfService:String){
        status.value = "loading"


        var docRef = db.collection("ServiceListings").whereEqualTo("serviceID", "")
        if(!typeOfService.equals("")){
            docRef = docRef.whereEqualTo("serviceType", typeOfService)
            Log.i("ASD", "vao tyoep of service")
        }
        if(!sortingType.equals("")){
            if(sortingAccording.equals("des")){
                docRef = docRef.orderBy(sortingType, Query.Direction.DESCENDING)
            } else docRef = docRef.orderBy(sortingType)
            Log.i("ASD", "vao sortingtype")
        }
        docRef.get().addOnSuccessListener { snapshot ->
            val temp = ArrayList<Service>()
            Log.i("ASD", snapshot.size().toString())
            for(service in snapshot){
                val serviceTemp = Service(
                    service.data.get("serviceType").toString(),
                    service.data.get("serviceName").toString(),
                    service.data.get("serviceDescription").toString(),
                    service.data.get("servicePrice").toString().toLong(),
                    service.data.get("servicePhoneNumber").toString(),
                    service.data.get("serviceImage").toString(),
                    service.data.get("serviceRating").toString().toFloat(),
                    service.data.get("vendorID").toString(),
                    service.data.get("vendorName").toString(),
                    //service.data.get("negotiate").toString().toBoolean(),
                )
                serviceTemp.serviceID = service.id
                temp.add(serviceTemp)
            }
            Log.i("ASD", temp.size.toString())
            serviceList.value!!.clear()
            serviceList.value!!.addAll(temp)
            serviceList.value = serviceList.value
            status.value = "hide_loader"
        }
            .addOnFailureListener { exception ->
                Log.i("ERROR", "Error getting documents.", exception)
            }
    }

    fun queryDataFilter(typeOfService:String, radius: String){
        status.value = "loading"

        var docRef = db.collection("ServiceListings").whereEqualTo("serviceID", "")
        if(!typeOfService.equals("")){
            docRef = docRef.whereEqualTo("serviceType", typeOfService)
            Log.i("ASD", "vao tyoep of service")
        }

        docRef.get().addOnSuccessListener { snapshot ->
            val temp = ArrayList<Service>()
            Log.i("ASD", snapshot.size().toString())
            for(service in snapshot){
                val serviceTemp = Service(
                    service.data.get("serviceType").toString(),
                    service.data.get("serviceName").toString(),
                    service.data.get("serviceDescription").toString(),
                    service.data.get("servicePrice").toString().toLong(),
                    service.data.get("servicePhoneNumber").toString(),
                    service.data.get("serviceImage").toString(),
                    service.data.get("serviceRating").toString().toFloat(),
                    service.data.get("vendorID").toString(),
                    service.data.get("vendorName").toString(),
                )
                serviceTemp.serviceID = service.id
                temp.add(serviceTemp)
            }
            Log.i("ASD", temp.size.toString())
            serviceList.value!!.clear()
            serviceList.value!!.addAll(temp)
            serviceList.value = serviceList.value
            status.value = "hide_loader"
        }
            .addOnFailureListener { exception ->
                Log.i("ERROR", "Error getting documents.", exception)
            }
    }
}

