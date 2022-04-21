package com.example.androiddevelopmentgroup7.viewModels

import android.net.Uri
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.androiddevelopmentgroup7.dataModels.Service
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage


class ServiceViewModel :  ViewModel() {
    val db = Firebase.firestore
    private val serviceList: MutableLiveData<ArrayList<Service>> =
        MutableLiveData<ArrayList<Service>>()

    val selectedServiceList: LiveData<ArrayList<Service>> get() = serviceList

    val status: MutableLiveData<String> = MutableLiveData("loading")

    val serviceTypeLivaData: MutableLiveData<ArrayList<String>> =
        MutableLiveData<ArrayList<String>>()

    val serviceID: MutableLiveData<ArrayList<String>> =
        MutableLiveData<ArrayList<String>>()

    val storage = Firebase.storage
    val storageRef = storage.reference
    fun setServiceList(vendorID: String) {
        status.value = "loading"
//        serviceList.value = serviceListArg
        val temp = ArrayList<Service>()
        val tempID = ArrayList<String>()
        serviceList.value = temp
        db.collection("ServiceListings")
            .whereEqualTo("vendorID", vendorID)
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
                    )
                    serviceTemp.serviceRating = service.data.get("serviceRating").toString().toFloat()
                    tempID.add(service.id)
                    temp.add(serviceTemp)
                }
                serviceList.value = temp
                serviceID.value = tempID
                status.value = "hide_loader"
            }
            .addOnFailureListener { exception ->
                Log.i("ERROR", "Error getting documents.", exception)
            }

    }

    fun addServiceToList(vendorID: String, service: HashMap<String, String>) {

        db.collection("ServiceListings")
            .add(service)
            .addOnSuccessListener { documentRefer ->
                status.value = "add_service_sucess"
            }

    }

    fun uploadFileAndSaveService(imageUri: Uri, vendorID: String, service: HashMap<String, String>){
        // Create a reference to ""
        status.value = "loading"
        val file = imageUri
        val fileRef = storageRef.child("service_image/${file.lastPathSegment}")

        val uploadTask = fileRef.putFile(file)

        // Register observers to listen for when the download is done or if it fails
        uploadTask.addOnFailureListener {
            // Handle unsuccessful uploads
        }.addOnSuccessListener { taskSnapshot ->
            //uploadTask.getResult().
            fileRef.downloadUrl.addOnSuccessListener { url ->
                Log.i("URL", url.toString())
                service.set("serviceImage", url.toString())
                addServiceToList(vendorID, service)
            }
        }
    }


    fun uploadFileAndUpdateService(imageUri: Uri, vendorID: String, service: HashMap<String, String>, position: Int){
        // Create a reference to ""
        status.value = "loading"
        val file = imageUri
        val fileRef = storageRef.child("service_image/${file.lastPathSegment}")

        val uploadTask = fileRef.putFile(file)

        // Register observers to listen for when the download is done or if it fails
        uploadTask.addOnFailureListener {
            // Handle unsuccessful uploads
        }.addOnSuccessListener { taskSnapshot ->
            //uploadTask.getResult().
            fileRef.downloadUrl.addOnSuccessListener { url ->
                Log.i("URL", url.toString())
                service.set("serviceImage", url.toString())
                updateService(position, service)
            }
        }
    }

    fun updateService(position: Int, service: HashMap<String, String>) {

        db.collection("ServiceListings")
            .document(serviceID.value!!.get(position))
            .set(service)
            .addOnSuccessListener { documentRefer ->
                status.value = "add_service_sucess"
                val temp = serviceList.value
                serviceList.value = temp!!
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

    fun deleteService(position: Int, vendorID: String){
        status.value = "loading"
        db.collection("ServiceListings").document(serviceID.value!!.get(position)).delete()
            .addOnSuccessListener {
                status.value = "delete_success"
                val serviceListTemp = serviceList.value!!
                serviceListTemp.removeAt(position)
                serviceList.value = serviceListTemp

                val serviceIDTemp = serviceID.value!!
                serviceIDTemp.removeAt(position)
                serviceID.value = serviceIDTemp
            }
    }
}

