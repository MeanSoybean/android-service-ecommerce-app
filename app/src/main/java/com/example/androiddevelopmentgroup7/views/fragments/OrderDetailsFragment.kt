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
import com.example.androiddevelopmentgroup7.models.Order
import com.example.androiddevelopmentgroup7.models.Service
import com.example.androiddevelopmentgroup7.utils.DownloadImageFromInternet
import com.example.androiddevelopmentgroup7.utils.OrderTabValue
import com.example.androiddevelopmentgroup7.utils.Utils
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.firebase.Timestamp
import com.google.firebase.firestore.SetOptions
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"
class OrderDetailsFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var idOrder: String? = null
    private var toolbar:MaterialToolbar? = null
    private val db = Firebase.firestore
    private var order: Order? = null
    private var service: Service? = null
    private var loader : FrameLayout? = null

    private var serviceImageView: ImageView? = null
    private var serviceNameTextView: TextView? = null
    private var serviceTypeTextView: TextView? = null
    private var orderComingTextView: TextView? = null
    private var customerNameTextView: TextView? = null
    private var customerPhoneTextView: TextView? = null
    private var customerAddressTextView: TextView? = null
    private var vendorNameTextView: TextView? = null
    private var vendorPhoneNumberTextView: TextView? = null
    private var currentStateTextView: TextView? = null
    private var priceServiceTextView: TextView? = null
    private var acceptBtn: Button? = null
    private var cancelBtn: Button? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            idOrder = it.getString("idOrder")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val rootView = inflater.inflate(R.layout.fragment_order_details, container, false )

        loader = rootView.findViewById(R.id.loader_layout)

        toolbar = rootView.findViewById(R.id.topAppBar)
        toolbar?.setTitle(getString(R.string.order_detail_top_app_bar_text))
        toolbar?.setNavigationOnClickListener { findNavController().popBackStack() }

        initComponent(rootView)

        setDetailOrder()

        return rootView
    }

    private fun initComponent(rootView: View){
        serviceImageView = rootView.findViewById(R.id.product_image_view)
        serviceNameTextView = rootView.findViewById(R.id.cart_product_title_tv)
        serviceTypeTextView= rootView.findViewById(R.id.cart_product_type_tv)
        orderComingTextView = rootView.findViewById(R.id.time_coming_text)
        customerNameTextView = rootView.findViewById(R.id.name_customer_text)
        customerPhoneTextView = rootView.findViewById(R.id.phone_number_customer_text)
        customerAddressTextView = rootView.findViewById(R.id.customer_address_text)
        vendorNameTextView = rootView.findViewById(R.id.name_vendor_text)
        vendorPhoneNumberTextView = rootView.findViewById(R.id.phone_number_vendor_text)
        currentStateTextView = rootView.findViewById(R.id.ship_curr_status_value_tv)
        priceServiceTextView = rootView.findViewById(R.id.price_total_amount_tv)
        acceptBtn = rootView.findViewById(R.id.accept_service_btn)
        cancelBtn = rootView.findViewById(R.id.cancle_service_btn)
    }
    private fun setDetailOrder(){
        db.collection("OrderListing").document(idOrder!!)
            .get().addOnSuccessListener { doc ->
                Log.i("ASD", doc.get("idVendor").toString())
                order = Order(
                    doc.get("idVendor").toString(),
                    doc.get("idCustomer").toString(),
                    doc.get("idService").toString(),
                    doc.get("timeOrder") as Timestamp,
                    doc.get("timeComing").toString(),
                    doc.get("orderAddress").toString(),
                    doc.get("orderCurrent").toString().toInt(),
                    doc.get("price").toString().toLong(),
                    doc.get("phoneNumber").toString(),
                    doc.get("customerName").toString(),
                )
                order?.idOrder = doc.id
                db.collection("ServiceListings").document(order!!.idService)
                    .get().addOnSuccessListener { docSnap ->
                        Log.i("ASD",docSnap.get("serviceName").toString())
                        service = Service(
                            docSnap.get("serviceType").toString(),
                            docSnap.get("serviceName").toString(),
                            docSnap.get("serviceDescription").toString(),
                            docSnap.get("servicePrice").toString().toLong(),
                            docSnap.get("servicePhoneNumber").toString(),
                            docSnap.get("serviceImage").toString(),
                            docSnap.get("serviceRating").toString().toFloat(),
                            docSnap.get("vendorID").toString(),
                            docSnap.get("vendorName").toString(),
                            //docSnap.get("negotiate").toString().toBoolean()
                        )
                        service?.serviceID = docSnap.id
                        setDataForView()
                        loader?.visibility = View.GONE
                    }
            }
    }

    private fun setDataForView(){
        DownloadImageFromInternet(serviceImageView!!).execute(service!!.serviceImage)
        serviceNameTextView?.text = service?.serviceName
        serviceTypeTextView?.text = service?.serviceType
        orderComingTextView?.text = order?.timeComing
        customerNameTextView?.text = order?.customerName
        customerPhoneTextView?.text = order?.phoneNumber
        customerAddressTextView?.text = order?.orderAddress
        vendorNameTextView?.text = service?.vendorName
        vendorPhoneNumberTextView?.text = service?.servicePhoneNumber
        when(order?.orderCurrent){
            OrderTabValue.WAITING_ACCEPT -> {
                if(Utils.typeUser == 0){
                    acceptBtn?.text = getString(R.string.information_vendor_text)
                    val bundle = Bundle()
                    bundle.putString("vendorID", order!!.idVendor)
                    acceptBtn?.setOnClickListener { findNavController().navigate(R.id.action_orderDetailsFragment_to_fragment_vendor_information, bundle) }

                    cancelBtn?.text = getString(R.string.cancle_order_for_customer)
                    cancelBtn?.setOnClickListener { alertBuilder(OrderTabValue.CANCEL, R.string.cancel_order_title_text, R.string.cancel_order_message_text) }
                } else {
                    acceptBtn?.text = getString(R.string.accept_order_text)
                    acceptBtn?.setOnClickListener {
                        updateState(OrderTabValue.ON_GOING)
                    }

                    cancelBtn?.text = getString(R.string.cancle_order_text)
                    cancelBtn?.setOnClickListener { alertBuilder(OrderTabValue.CANCEL, R.string.cancel_order_title_text, R.string.cancel_order_message_text) }
                }
                currentStateTextView?.text = requireActivity().getString(R.string.accept_tab_text)
            }
            OrderTabValue.ON_GOING -> {
                if(Utils.typeUser == 1){
                    acceptBtn?.text = getString(R.string.confirm_service_success)
                    acceptBtn?.setOnClickListener {
                        alertBuilder(OrderTabValue.COMPLETE, R.string.is_success_service_title,R.string.is_success_service_message )
                    }

                } else {
                    acceptBtn?.text = getString(R.string.information_vendor_text)
                    val bundle = Bundle()
                    bundle.putString("vendorID", order!!.idVendor)
                    acceptBtn?.setOnClickListener { findNavController().navigate(R.id.action_orderDetailsFragment_to_fragment_vendor_information, bundle) }
                }
                cancelBtn?.visibility = View.GONE
                currentStateTextView?.text = requireActivity().getString(R.string.on_board_tab_text)
            }
            OrderTabValue.COMPLETE -> {
                cancelBtn?.visibility = View.GONE
                acceptBtn?.visibility = View.GONE
                currentStateTextView?.text = requireActivity().getString(R.string.complete_tab_text)
            }
            OrderTabValue.CANCEL -> {
                cancelBtn?.visibility = View.GONE
                acceptBtn?.visibility = View.GONE
                currentStateTextView?.text = requireActivity().getString(R.string.cancel_tab_text)
            }
            else -> {
                currentStateTextView?.text = requireActivity().getString(R.string.accept_tab_text)
            }
        }
        priceServiceTextView?.text = order?.price.toString()

//        acceptBtn = rootView.findViewById(R.id.accept_service_btn)
//        cancelBtn = rootView.findViewById(R.id.cancle_service_btn)
    }


    private fun alertBuilder(state:Int, titleText:Int, messageText:Int){
        context?.let {
            MaterialAlertDialogBuilder(requireContext())
                .setTitle(getString(titleText))
                .setMessage(getString(messageText))
                .setNeutralButton(getString(R.string.pro_cat_dialog_cancel_btn)) { dialog, _ ->
                    dialog.cancel()
                }
                .setPositiveButton(getString(R.string.accept_btn_text)) { dialog, _ ->
                    updateState(state)
                    dialog.cancel()
                }
                .show()
        }
    }

    private fun updateState(state:Int){
        loader?.visibility = View.VISIBLE
        db.collection("OrderListing").document(order!!.idOrder)
            .update("orderCurrent" , state)
            .addOnSuccessListener {
                loader?.visibility = View.GONE
                Toast.makeText(requireContext(), R.string.success_message, Toast.LENGTH_LONG).show();
//                findNavController().navigate(R.id.action_orderDetailsFragment_to_orderServiceFragment)
                findNavController().popBackStack()
            }
    }
    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment OrderDetailsFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(idOrder: String) =
            OrderDetailsFragment().apply {
                arguments = Bundle().apply {
                    putString("idOrder", idOrder)
                }
            }
    }
}