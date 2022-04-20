package com.example.androiddevelopmentgroup7

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.example.androiddevelopmentgroup7.dataModels.Service
import com.example.androiddevelopmentgroup7.viewModels.ServiceViewModel
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlin.collections.ArrayList

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
//    // TODO: Rename and change types of parameters
    private var typeActivity: String? = null
    lateinit var service:Service
    val db = Firebase.firestore
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
    private var isSetImage: Boolean = false

    companion object{
        val INTENT_SELECT_IMAGE: Int = 10000
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            typeActivity = it.getString("type_activity")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        viewBinding =  inflater.inflate(R.layout.service_details_vendor_fragment, container, false)
        serviceViewModel.status.value = "loading"

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


        setServicesTypeFromDatabase(serviceType) // custom adapter for select services

        //Event listener
        addBtn.setOnClickListener{ processingData() }
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
            }
        })
        return viewBinding
    }

    private fun setServicesTypeFromDatabase(service: AutoCompleteTextView){
        db.collection("ServiceType").get()
            .addOnSuccessListener { serviceTypes ->
                var nameType = ArrayList<String>()
                for(serviceType in serviceTypes){
                    nameType.add(serviceType.data.get("name").toString())
                }
                service.setText(nameType.get(0),false)
                val adapter = ArrayAdapter(requireActivity(), R.layout.service_list_item, nameType)
                service.setAdapter(adapter)
                serviceViewModel.status.value = "hide_loader"

            }
            .addOnFailureListener { exception ->
                Log.d("AAA", "get failed with ", exception)
            }
//        service.setText("Sửa ống nước",false)
//        var serviceType = ArrayList<String>().also{
//            it.add("Sửa máy may")
//            it.add("Sửa bếp ga")
//            it.add("Sửa nồi cơm điện")
//            it.add("Sửa quạt gió")
//            it.add("Sửa các loại mô tơ đây")
//        }
//        val adapter = ArrayAdapter(requireActivity(), R.layout.service_list_item, serviceType)
//        service.setAdapter(adapter)
    }

    private fun processingData(){
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
        if(!isSetImage){
            serviceImageLayout.error = getString(R.string.image_service_emty_string)
            isSuccess = false
        }
        Log.i("AAA", "btn click insert service")
        if(!isSuccess){
            return
        }


        val service = Service(
            "Sửa đồ gia dụng",
            "Sửa tivi tận nơi",
            "Nhận sửa các loại tivi công nghệ cao, an toàn, nhanh chống",
            "Giá cả thương lượng","123",
            "")
        serviceViewModel.addServiceToList(service)
        findNavController().navigate(R.id.action_service_details_vendor_fragment_to_home_vendor_fragment)
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
        Log.i("456","asdasd")
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == INTENT_SELECT_IMAGE){
            if(resultCode == Activity.RESULT_OK){
                if(data != null){
//                    Log.i("123",data.data.toString())
                    val bitmap = MediaStore.Images.Media.getBitmap(requireActivity().contentResolver, data.data);
                    serviceImage.setImageBitmap(bitmap);
                    isSetImage = true
                }
            }
        }
    }

}