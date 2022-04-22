package com.example.androiddevelopmentgroup7

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.AsyncTask
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.example.androiddevelopmentgroup7.viewModels.ServiceViewModel
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
//private const val ARG_PARAM1 = "param1"
//private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [service_details_vendor_fragment.newInstance] factory method to
 * create an instance of this fragment.
 */

class service_details_vendor_fragment : Fragment(){

    private inner class DownloadImageFromInternet(var imageView: ImageView) : AsyncTask<String, Void, Bitmap?>() {
        override fun doInBackground(vararg urls: String): Bitmap? {
            val imageURL = urls[0]
            var image: Bitmap? = null
            try {
                val `in` = java.net.URL(imageURL).openStream()
                image = BitmapFactory.decodeStream(`in`)
            }
            catch (e: Exception) {
                Log.e("Error Message", e.message.toString())
                e.printStackTrace()
            }
            return image
        }
        override fun onPostExecute(result: Bitmap?) {
            imageView.setImageBitmap(result)
        }
    }

//    // TODO: Rename and change types of parameters
    private var typeActivity: String? = null
    private var type:String? = null
    private var name:String? = null
    private var description:String? = null
    private var price:String? = null
    private var contact:String? = null
    private var image:String? = null
    private var position:Int? = null


    private val vendorID = "CbcUnjIZh9tqHrxeuxEP"

    //val db = Firebase.firestore
    private val serviceViewModel : ServiceViewModel by activityViewModels()
    lateinit var loader:FrameLayout
    private lateinit var viewBinding: View
    private lateinit var addBtn : Button
    private lateinit var toolbar: MaterialToolbar

    private lateinit var serviceType: AutoCompleteTextView
    private lateinit var serviceName: TextInputEditText
    private lateinit var serviceDescription: TextInputEditText
    private lateinit var serviceCost: TextInputEditText
    private lateinit var serviceContact: TextInputEditText
    private lateinit var serviceImage: ImageView


    private lateinit var serviceTypeLayout: TextInputLayout
    private lateinit var serviceNameLayout: TextInputLayout
    private lateinit var serviceDescriptionLayout: TextInputLayout
    private lateinit var serviceCostLayout: TextInputLayout
    private lateinit var serviceContactLayout: TextInputLayout
    private lateinit var serviceImageLayout: TextInputLayout
    private lateinit var imageUri:Uri

    private var isSetImage: Boolean = false
    companion object{
        val INTENT_SELECT_IMAGE: Int = 10000
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            typeActivity = it.getString("type_activity")
            type = it.getString("type")
            name = it.getString("name")
            description = it.getString("description")
            price = it.getString("price")
            contact = it.getString("contact")
            image = it.getString("image")
            position = it.getInt("position")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        viewBinding =  inflater.inflate(R.layout.service_details_vendor_fragment, container, false)

        toolbar = viewBinding.findViewById(R.id.topAppBar)
        addBtn = viewBinding.findViewById(R.id.add_service_save_btn)
        loader = viewBinding.findViewById(R.id.loader_layout)


        serviceType = viewBinding.findViewById(R.id.vendor_type_service_edit_text)
        serviceName = viewBinding.findViewById(R.id.vendor_name_service_edit_text)
        serviceDescription = viewBinding.findViewById(R.id.vendor_description_service_edit_text)
        serviceCost = viewBinding.findViewById(R.id.vendor_cost_service_edit_text)
        serviceContact = viewBinding.findViewById(R.id.vendor_contact_edit_text)
        serviceImage = viewBinding.findViewById(R.id.descriptionImage)

        serviceNameLayout = viewBinding.findViewById(R.id.nameServiceTextField)
        serviceDescriptionLayout = viewBinding.findViewById(R.id.descriptionServiceTextField)
        serviceCostLayout = viewBinding.findViewById(R.id.costServiceTextField)
        serviceContactLayout = viewBinding.findViewById(R.id.contactServiceTextField)
        serviceImageLayout = viewBinding.findViewById(R.id.imageServiceTextField)


        serviceViewModel.setServicesTypeFromDatabase() // custom adapter for select services

        //Event listener
        addBtn.setOnClickListener{
            if(processingData())
            {
                val service = hashMapOf(
                "serviceType" to serviceType.text.toString(),
                "serviceName" to serviceName.text.toString(),
                "serviceDescription" to  serviceDescription.text.toString(),
                "servicePrice" to  serviceCost.text.toString(),
                "serviceImage" to "",
                "servicePhoneNumber" to serviceContact.text.toString(),
                "vendorName" to "Tran Tuan Kha",
                "vendorID" to vendorID,
                "serviceRating" to "5",)
                serviceViewModel.uploadFileAndSaveService(imageUri, vendorID, service)
            }
        }
        serviceImage.setOnClickListener { selectFileFromIntent() } //start intent filechooser
        toolbar.setNavigationOnClickListener { findNavController().popBackStack() }
        setFocusChangeListener()


        serviceViewModel.status.observe(viewLifecycleOwner, Observer { status ->
            // Update the list UI
            when(status){
                "hide_loader" -> {
                    loader.visibility = View.GONE
                    addBtn.isClickable = true
                }
                "loading"-> {
                    loader.visibility = View.VISIBLE
                    addBtn.isClickable = false
                }
                "add_service_sucess" -> {
                    loader.visibility = View.GONE
                    findNavController().navigate(R.id.action_service_details_vendor_fragment_to_home_vendor_fragment)
                }
            }
        })


        serviceViewModel.serviceTypeLivaData.observe(viewLifecycleOwner, Observer { type ->
            // Update the list UI
            serviceType.setText(type.get(0), false)
            val adapter = ArrayAdapter(requireActivity(), R.layout.service_list_item, type)
            serviceType.setAdapter(adapter)
        })


        if(typeActivity.equals("edit")){
            toolbar.setTitle(R.string.edit_tittle_string)
            serviceType.setText(type, false)
            serviceName.setText(name)
            serviceDescription.setText(description)
            serviceCost.setText(price)
            serviceContact.setText(contact)
            DownloadImageFromInternet(serviceImage).execute(image)

            addBtn.setText(R.string.save_edit_string)
            addBtn.setOnClickListener {
                if(processingDataForUpdate()) {
                    val service = hashMapOf(
                        "serviceType" to serviceType.text.toString(),
                        "serviceName" to serviceName.text.toString(),
                        "serviceDescription" to  serviceDescription.text.toString(),
                        "servicePrice" to  serviceCost.text.toString(),
                        "serviceImage" to image!!,
                        "servicePhoneNumber" to serviceContact.text.toString(),
                        "vendorName" to "Tran Tuan Kha",
                        "vendorID" to vendorID,
                        "serviceRating" to "5",
                    )
                    if(!isSetImage){
                        serviceViewModel.updateService(position!!, service)
                    }
                    else serviceViewModel.uploadFileAndUpdateService(imageUri, vendorID, service, position!!)
                }
            }

        }


        return viewBinding
    }



    private fun processingData():Boolean{
        var isSuccess: Boolean = true
        serviceName.clearFocus()
        serviceCost.clearFocus()
        serviceContact.clearFocus()
        serviceDescription.clearFocus()
        if(serviceName.text.toString().isBlank() || serviceName.text.toString().isEmpty()){
            serviceNameLayout.error = getString(R.string.name_service_emty_string)
            isSuccess = false
        }
        if(serviceDescription.text.toString().isBlank() || serviceDescription.text.toString().isEmpty()){
            serviceDescriptionLayout.error = getString(R.string.description_service_emty_string)
            isSuccess = false
        }
        if(serviceCost.text.toString().isBlank() || serviceCost.text.toString().isEmpty()){
            serviceCostLayout.error = getString(R.string.cost_service_emty_string)
            isSuccess = false
        }
        if(serviceContact.text.toString().isBlank() || serviceContact.text.toString().isEmpty()){
            serviceContactLayout.error = getString(R.string.contact_service_emty_string)
            isSuccess = false
        }
        if(!isSetImage) {
            serviceImageLayout.error = getString(R.string.image_service_emty_string)
            isSuccess = false
        }
        return isSuccess
    }



    private fun processingDataForUpdate():Boolean{
        var isSuccess: Boolean = true
        serviceName.clearFocus()
        serviceCost.clearFocus()
        serviceContact.clearFocus()
        serviceDescription.clearFocus()
        if(serviceName.text.toString().isBlank() || serviceName.text.toString().isEmpty()){
            serviceNameLayout.error = getString(R.string.name_service_emty_string)
            isSuccess = false
        }
        if(serviceDescription.text.toString().isBlank() || serviceDescription.text.toString().isEmpty()){
            serviceDescriptionLayout.error = getString(R.string.description_service_emty_string)
            isSuccess = false
        }
        if(serviceCost.text.toString().isBlank() || serviceCost.text.toString().isEmpty()){
            serviceCostLayout.error = getString(R.string.cost_service_emty_string)
            isSuccess = false
        }
        if(serviceContact.text.toString().isBlank() || serviceContact.text.toString().isEmpty()){
            serviceContactLayout.error = getString(R.string.contact_service_emty_string)
            isSuccess = false
        }
        return isSuccess
    }
    private fun setFocusChangeListener(){
        serviceName.setOnFocusChangeListener{v, b ->
            if(b){
                serviceNameLayout.error = null
            }
        }
        serviceDescription.setOnFocusChangeListener{v, b ->
            if(b){
                serviceDescriptionLayout.error = null
            }
        }
        serviceCost.setOnFocusChangeListener{v, b ->
            if(b){
                serviceCostLayout.error = null
            }
        }
        serviceContact.setOnFocusChangeListener{v, b ->
            if(b){
                serviceContactLayout.error = null
            }
        }
    }

    fun selectFileFromIntent(){
        serviceImageLayout.error = null
        val intent = Intent(Intent.ACTION_OPEN_DOCUMENT)
        intent.addCategory(Intent.CATEGORY_OPENABLE)
        intent.setType("image/*")
        startActivityForResult(intent, INTENT_SELECT_IMAGE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == INTENT_SELECT_IMAGE){
            if(resultCode == Activity.RESULT_OK){
                if(data != null){
//                    Log.i("123",data.data.toString())
                    val bitmap = MediaStore.Images.Media.getBitmap(requireActivity().contentResolver, data.data);
                    serviceImage.setImageBitmap(bitmap);
                    isSetImage = true
                    imageUri = data.data!!
                }
            }
        }
    }

}