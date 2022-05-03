package com.example.androiddevelopmentgroup7.views.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.navigation.fragment.findNavController
import com.example.androiddevelopmentgroup7.R
import com.example.androiddevelopmentgroup7.utils.DownloadImageFromInternet
import com.example.androiddevelopmentgroup7.utils.OrderTabValue
import com.example.androiddevelopmentgroup7.utils.Utils
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.Timestamp
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

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
    private var price:Long? = null
    private var contact:String? = null
    private var image:String? = null
    private var position:Int? = null
    private var negotiate:Boolean? = null
    private var vendorID:String? = null
    private var vendorName:String? = null
    private var rating:Float? = null
    private var serviceID:String? = null
    private var loader: FrameLayout? = null
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
    private var ratingDetailBtn:Button? = null
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
            price = it.getLong("price")
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
        setDateFreeList()
        return view
    }

    private fun setEventBtnClick(){
        orderBtn?.setOnClickListener {
            if(processingData()){
                showAcceptDialog()
            }
        }
    }

    private fun nowPlusDate(countDay: Int):Date{
        var temp = Date()
        val calendar = Calendar.getInstance()
        calendar.time = temp
        calendar.add(Calendar.DATE, countDay)
        temp = calendar.time
        return temp
    }
//    private fun convertStringToTimestamp(str:String):String{
//        val formatter = SimpleDateFormat("dd/MM/yyyy")
//        return formatter.parse(str)!!
//    }

    private fun setDateFreeList(){

        val formatter = SimpleDateFormat("dd/MM/yyyy")
        val freeDay = ArrayList<String>()
        for(i in 1..7){
            freeDay.add(formatter.format(nowPlusDate(i)).toString())
        }
        db.collection("OrderListing")
            .whereEqualTo("idVendor", vendorID)
            .whereEqualTo("orderCurrent", OrderTabValue.ON_GOING)
            .get()
            .addOnSuccessListener { snapshot ->
                Log.i("size", snapshot.documents.size.toString())
                val deleteDays = ArrayList<String>()
                val busyDays = ArrayList<String>()
                //find all day have a order
                for(doc in snapshot){
                    val day = doc.data.get("timeComing").toString()
                    busyDays.add(day)
                }
                //check all day in next 7 day, if have contain a day in list order, then delete this day
                for(day in freeDay){
                    if(busyDays.contains(day)){
                        deleteDays.add(day)
                    }
                }
                freeDay.removeAll(deleteDays)
                if(freeDay.size > 0){
                    dateOrder?.setText(freeDay.get(0), false)
                    val adapter = ArrayAdapter(requireActivity(), R.layout.service_list_item, freeDay)
                    dateOrder?.setAdapter(adapter)
                } else {
                    dateOrder?.setText(getString(R.string.full_order_schedule), false)
                    orderBtn?.setBackgroundColor(resources.getColor(R.color.blue_shadow_color))
                    orderBtn?.isEnabled = false
                    customerName?.isEnabled = false
                    customerAddress?.isEnabled = false
                    customerPhone?.isEnabled = false
                    dateOrder?.isEnabled = false
                }
                loader?.visibility = View.GONE
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
                    loader?.visibility = View.VISIBLE
                    db.collection("OrderListing").add(hashMapOf(
                        "idCustomer" to Utils.customer.id,
                        "idService" to serviceID,
                        "idVendor" to vendorID,
                        "orderAddress" to customerAddress?.text.toString(),
                        "orderCurrent" to OrderTabValue.WAITING_ACCEPT,
                        "price" to price,
                        "timeComing" to dateOrder?.text.toString(),
                        "timeOrder" to Timestamp(Date()),
                        "phoneNumber" to customerPhone?.text.toString(),
                        "customerName" to customerName?.text.toString()
                    )).addOnSuccessListener {
                        loader?.visibility = View.GONE
                        val tittle = getString(R.string.new_order_service)
                        val message = getString(R.string.customer_text) + " " + customerName?.text.toString() + " " + getString(R.string.ordered_service) + name
                        db.collection("Vendors").document(vendorID!!).get().addOnSuccessListener { doc ->
                            db.collection("Notifications").add(hashMapOf(
                                "Name" to tittle,
                                "Description" to message,
                                "accountID" to doc.data!!.get("AccountID"),
                                "time" to Timestamp(Date()),
                            ))
                        }
                        findNavController().popBackStack()

                        //findNavController().navigate(R.id.action_fragment_service_detail_to_orderServiceFragment)
                        Toast.makeText(requireContext(), R.string.success_message, Toast.LENGTH_LONG).show();
                    }
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
        ratingDetailBtn = view.findViewById(R.id.service_rating_btn)
        serviceInformationVendorBtn = view.findViewById(R.id.information_vendor_btn)

        customerName = view.findViewById(R.id.customer_name_edit_text)
        customerAddress = view.findViewById(R.id.customer_address_edit_text)
        customerPhone = view.findViewById(R.id.customer_phone_edit_text)
        dateOrder = view.findViewById(R.id.date_order_service_edit_text)

        customerNameLayout = view.findViewById(R.id.nameServiceTextField)
        customerAddressLayout = view.findViewById(R.id.addressCustomerTextField)
        customerPhoneLayout = view.findViewById(R.id.phoneCustomerTextField)
        dateOrderLayout = view.findViewById(R.id.dateServiceTextField)

        loader = view.findViewById(R.id.loader_layout)

    }

    private fun setValue(){
        DownloadImageFromInternet(serviceImageView!!).execute(image)
        serviceName?.setText(name)
        serviceType?.setText(type)
        serviceRating?.rating = rating!!
        serviceContact?.setText(contact)
        serviceDescription?.setText(description)
        serviceVendorName?.setText(vendorName)
        servicePrice?.setText(price.toString() + getString(R.string.vietnamdong))
        toolbar?.setTitle(getString(R.string.detail_service_app_tittle_text))
        toolbar?.setNavigationOnClickListener { findNavController().popBackStack() }
        serviceInformationVendorBtn?.setOnClickListener {
            val bundle = Bundle()
            bundle.putString("vendorID", vendorID)
            findNavController().navigate(R.id.action_fragment_service_detail_to_fragment_vendor_information, bundle)
        }

        ratingDetailBtn?.setOnClickListener {
            val bundle = Bundle()
            bundle.putString("serviceID", serviceID)
            findNavController().navigate(R.id.action_fragment_service_detail_to_fragment_rating_detail, bundle)
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