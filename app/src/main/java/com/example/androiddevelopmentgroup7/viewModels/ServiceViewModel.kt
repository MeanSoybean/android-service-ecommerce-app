package com.example.androiddevelopmentgroup7.viewModels

import android.net.Uri
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.androiddevelopmentgroup7.utils.Utils
import com.example.androiddevelopmentgroup7.models.Service
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
            .whereEqualTo("vendorID", Utils.vendor.id)
            .get()
            .addOnSuccessListener { services ->
                Log.i("success", services.size().toString())
                for (service in services) {
                    val serviceTemp = Service(
                        service.data.get("serviceType").toString(),
                        service.data.get("serviceName").toString(),
                        service.data.get("serviceDescription").toString(),
                        service.data.get("servicePrice").toString(),
                        service.data.get("servicePhoneNumber").toString(),
                        service.data.get("serviceImage").toString(),
                        service.data.get("serviceRating").toString().toFloat(),
                        service.data.get("vendorID").toString(),
                        service.data.get("vendorName").toString(),
                        service.data.get("negotiate").toString().toBoolean()
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
                        service.data.get("servicePrice").toString(),
                        service.data.get("servicePhoneNumber").toString(),
                        service.data.get("serviceImage").toString(),
                        service.data.get("serviceRating").toString().toFloat(),
                        service.data.get("vendorID").toString(),
                        service.data.get("vendorName").toString(),
                        service.data.get("negotiate").toString().toBoolean(),
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
                status.value = "add_service_sucess"
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
            .set(service)
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
        db.collection("Ser")
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
    }
}

