package com.example.androiddevelopmentgroup7.views.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.navigation.fragment.findNavController
import com.example.androiddevelopmentgroup7.R
import com.example.androiddevelopmentgroup7.utils.DownloadImageFromInternet
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [fragment_service_detail.newInstance] factory method to
 * create an instance of this fragment.
 */
class fragment_service_detail : Fragment() {
    // TODO: Rename and change types of parameters
    private var typeActivity: String? = null
    private var type:String? = null
    private var name:String? = null
    private var description:String? = null
    private var price:String? = null
    private var contact:String? = null
    private var image:String? = null
    private var position:Int? = null
    private var negotiate:Boolean? = null
    private var vendorID:String? = null
    private var vendorName:String? = null
    private var rating:Float? = null
    private var serviceID:String? = null

    private var db = Firebase.firestore

    private var serviceImageView:ImageView? = null
    private var serviceType:TextView? = null
    private var serviceRating:RatingBar? = null
    private var serviceName:TextView? = null
    private var serviceDescription:TextView? = null
    private var serviceVendorName:TextView? = null
    private var serviceContact:TextView? = null
    private var servicePrice:TextView? = null
    private var orderBtn: Button? = null
    private var serviceInformationVendorBtn:Button? = null
    private var toolbar:MaterialToolbar? = null

    private var customerName:TextInputEditText? = null
    private var customerAddress:TextInputEditText? = null
    private var customerPhone:TextInputEditText? = null
    private var dateOrder:AutoCompleteTextView? = null

    private var customerNameLayout:TextInputLayout? = null
    private var customerAddressLayout:TextInputLayout? = null
    private var customerPhoneLayout:TextInputLayout? = null
    private var dateOrderLayout:TextInputLayout? = null

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
            negotiate = it.getBoolean("negotiate")
            vendorID = it.getString("vendorID")
            vendorName = it.getString("vendorName")
            rating = it.getFloat("rating")
            serviceID = it.getString("serviceID")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_service_detail, container, false)
        initComponent(view)
        setFocusChangeListener()
        setValue()
        setEventBtnClick()

        return view
    }

    private fun setEventBtnClick(){
        orderBtn?.setOnClickListener {
            if(processingData()){
                showAcceptDialog()
            }
        }
    }

    private fun processingData():Boolean{
        var isSuccess: Boolean = true
        customerName?.clearFocus()
        customerPhone?.clearFocus()
        customerAddress?.clearFocus()
        if(customerName?.text.toString().isBlank() || customerName?.text.toString().isEmpty()){
            customerNameLayout?.error = getString(R.string.name_customer_emty_string)
            isSuccess = false
        }
        if(customerPhone?.text.toString().isBlank() || customerPhone?.text.toString().isEmpty()){
            customerPhoneLayout?.error = getString(R.string.phone_customer_emty_string)
            isSuccess = false
        }
        if(customerAddress?.text.toString().isBlank() || customerAddress?.text.toString().isEmpty()){
            customerAddressLayout?.error = getString(R.string.address_customer_emty_string)
            isSuccess = false
        }
//        if(dateOrder?.text.toString().isBlank() || customerAddress?.text.toString().isEmpty()){
//            customerAddressLayout?.error = getString(R.string.cost_service_emty_string)
//            isSuccess = false
//        }
        return isSuccess
    }

    private fun showAcceptDialog() {
        context?.let {
            MaterialAlertDialogBuilder(requireContext())
                .setTitle(getString(R.string.order_dialog_title_text))
                .setMessage(getString(R.string.order_service_message_text))
                .setNeutralButton(getString(R.string.pro_cat_dialog_cancel_btn)) { dialog, _ ->
                    dialog.cancel()
                }
                .setPositiveButton(getString(R.string.order_dialog_btn_text)) { dialog, _ ->
//                    db.collection("OrderListing").add(hashMapOf(
//                        "idCustomer" to Utils.customer.id
//                        "idService" to
//                    ))
                    dialog.cancel()
                }
                .show()
        }
    }
    private fun initComponent(view:View){
        serviceName = view.findViewById(R.id.service_name)
        serviceImageView = view.findViewById(R.id.service_image_view)
        serviceContact = view.findViewById(R.id.vendor_phone_number_content)
        serviceVendorName = view.findViewById(R.id.vendor_name)
        serviceDescription = view.findViewById(R.id.service_description_content)
        serviceRating = view.findViewById(R.id.service_rating_bar)
        servicePrice = view.findViewById(R.id.service_price)
        serviceType = view.findViewById(R.id.service_type_text)
        toolbar = view.findViewById(R.id.topAppBar)
        orderBtn = view.findViewById(R.id.accept_service_btn)
        serviceInformationVendorBtn = view.findViewById(R.id.information_vendor_btn)

        customerName = view.findViewById(R.id.customer_name_edit_text)
        customerAddress = view.findViewById(R.id.customer_address_edit_text)
        customerPhone = view.findViewById(R.id.customer_phone_edit_text)
        dateOrder = view.findViewById(R.id.date_order_service_edit_text)

        customerNameLayout = view.findViewById(R.id.nameServiceTextField)
        customerAddressLayout = view.findViewById(R.id.addressCustomerTextField)
        customerPhoneLayout = view.findViewById(R.id.phoneCustomerTextField)
        dateOrderLayout = view.findViewById(R.id.dateServiceTextField)

    }
    private fun setValue(){
        DownloadImageFromInternet(serviceImageView!!).execute(image)
        serviceName?.setText(name)
        serviceType?.setText(type)
        serviceRating?.rating = rating!!
        serviceContact?.setText(contact)
        serviceDescription?.setText(description)
        serviceVendorName?.setText(vendorName)
        servicePrice?.setText(price + " VNĐ")
        toolbar?.setTitle(getString(R.string.detail_service_app_tittle_text))
        toolbar?.setNavigationOnClickListener { findNavController().popBackStack() }
        serviceInformationVendorBtn?.setOnClickListener {
            val bundle = Bundle()
            bundle.putString("vendorID", vendorID)
            findNavController().navigate(R.id.action_fragment_service_detail_to_fragment_vendor_information, bundle)
        }
    }

    private fun setFocusChangeListener(){
        customerName?.setOnFocusChangeListener{v, b ->
            if(b){
                customerNameLayout?.error = null
            }
        }
        customerAddress?.setOnFocusChangeListener{v, b ->
            if(b){
                customerAddressLayout?.error = null
            }
        }
        customerPhone?.setOnFocusChangeListener{v, b ->
            if(b){
                customerPhoneLayout?.error = null
            }
        }
//        serviceContact.setOnFocusChangeListener{v, b ->
//            if(b){
//                serviceContactLayout.error = null
//            }
//        }
    }
    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment fragment_service_detail.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            fragment_service_detail().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}