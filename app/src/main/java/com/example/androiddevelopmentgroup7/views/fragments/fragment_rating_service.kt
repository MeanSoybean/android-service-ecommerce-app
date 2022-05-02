package com.example.androiddevelopmentgroup7.views.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.widget.AppCompatRatingBar
import androidx.navigation.fragment.findNavController
import com.example.androiddevelopmentgroup7.R
import com.example.androiddevelopmentgroup7.models.Service
import com.example.androiddevelopmentgroup7.utils.DownloadImageFromInternet
import com.example.androiddevelopmentgroup7.utils.Utils
import com.google.android.material.appbar.MaterialToolbar
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [fragment_rating_service.newInstance] factory method to
 * create an instance of this fragment.
 */
class fragment_rating_service : Fragment() {
    // TODO: Rename and change types of parameters
    private var serviceID: String? = null
    private var loader: FrameLayout? = null
    private var serviceImage: ImageView? = null
    private var serviceNameTextview: TextView? = null
    private var serviceDescriptionTextview:TextView? = null
    private var namePhoneVendorTextview:TextView? = null
    private var serviceRatingBar: AppCompatRatingBar? = null
    private var feedbackMessage: EditText? = null
    private var sendBtn:Button? = null
    private var toolbar: MaterialToolbar? = null
    private var db = Firebase.firestore
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            serviceID = it.getString("serviceID")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_rating_service, container, false)
        initComponent(view)
        return view
    }
    private fun initComponent(view: View){
        loader = view.findViewById(R.id.loader_layout)
        serviceNameTextview = view.findViewById(R.id.name_service)
        serviceImage = view.findViewById(R.id.service_image_view)
        serviceDescriptionTextview = view.findViewById(R.id.description_service)
        namePhoneVendorTextview = view.findViewById(R.id.name_phone_vendor)
        serviceRatingBar = view.findViewById(R.id.service_rating_bar)
        feedbackMessage = view.findViewById(R.id.feedback_service_text)
        sendBtn = view.findViewById(R.id.send_btn)

        toolbar = view.findViewById(R.id.topAppBar)
        toolbar?.setTitle(getString(R.string.rating_app_tittle_text))
        toolbar?.setNavigationOnClickListener { findNavController().popBackStack() }

        sendBtn?.setOnClickListener {
            if(feedbackMessage?.text.toString().equals("")){
                Toast.makeText(requireContext(), R.string.empty_message_feedback, Toast.LENGTH_LONG).show()
            } else {
                val detailRating = mutableMapOf<String, MutableMap<String, Any>>()
                detailRating.put(Utils.customer.id , mutableMapOf("FeedBack" to "Qúa dữ", "Rating" to 3.5))
                //val serviceRating = mutableMapOf("RatingDetails" to detailRating)
                db.collection("ServiceRatings").document(serviceID!!).set(hashMapOf(
                    "RatingDetails" to detailRating,
                    "SumPerson" to 1,
                    "SumRating" to 5
                ))
                    .addOnSuccessListener {
                        Log.i("ASD", "SUCCESS")
                    }
                    .addOnFailureListener { exception ->
                        Log.i("ASD", "Error getting documents: ", exception)
                    }
            }

        }
        setServiceView()
        findPreviousRating()
    }

    private fun findPreviousRating(){
        db.collection("ServiceRatings").document(serviceID!!)
            .get().addOnSuccessListener { doc ->
                if(doc != null){
//                    val mapRating = doc.data!!.get("RatingDetails")
//                    if(mapRating != null){
//                        val personFeedback = (mapRating as Map<*,*>).get("FeedBack")
//                        val personRating = mapRating.get("Rating")
//                        serviceRatingBar?.rating = personRating.toString().toFloat()
//                        feedbackMessage?.setText(personFeedback.toString())
//                        Log.i("ASD", personRating.toString())
//                        Log.i("ASD", personFeedback.toString())
//                    }
                }
                loader?.visibility = View.GONE
            }
    }
    private fun setServiceView(){
        db.collection("ServiceListings").document(serviceID!!)
            .get().addOnSuccessListener { doc ->
                DownloadImageFromInternet(serviceImage!!).execute(doc.data?.get("serviceImage").toString())
                serviceNameTextview?.text = doc.data?.get("serviceName").toString()
                serviceDescriptionTextview?.text = doc.data?.get("serviceDescription").toString()
                (doc.data?.get("vendorName").toString() + " - " + doc.data?.get("servicePhoneNumber").toString()).also { namePhoneVendorTextview?.text = it }
                val service = Service(
                    doc.data?.get("serviceType").toString(),
                    doc.data?.get("serviceName").toString(),
                    doc.data?.get("serviceDescription").toString(),
                    doc.data?.get("servicePrice").toString().toLong(),
                    doc.data?.get("servicePhoneNumber").toString(),
                    doc.data?.get("serviceImage").toString(),
                    doc.data?.get("serviceRating").toString().toFloat(),
                    doc.data?.get("vendorID").toString(),
                    doc.data?.get("vendorName").toString(),
                    //service.data.get("negotiate").toString().toBoolean(),
                )
                service.serviceID = doc.id
            }
    }
    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment fragment_rating_service.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            fragment_rating_service().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}