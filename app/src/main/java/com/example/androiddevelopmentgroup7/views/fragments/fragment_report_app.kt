package com.example.androiddevelopmentgroup7.views.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.widget.AppCompatRatingBar
import androidx.navigation.fragment.findNavController
import com.example.androiddevelopmentgroup7.R
import com.example.androiddevelopmentgroup7.utils.Utils
import com.google.firebase.Timestamp
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import java.text.SimpleDateFormat
import java.util.*

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [fragment_report_app.newInstance] factory method to
 * create an instance of this fragment.
 */
class fragment_report_app : Fragment() {
    val db = Firebase.firestore
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private var serviceRatingBar: AppCompatRatingBar? = null
    private var feedbackMessage: EditText? = null
    private var sendBtn: Button? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }
    val auth = Firebase.auth
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_report_app, container, false)
        // Inflate the layout for this fragment
        serviceRatingBar = view.findViewById(R.id.service_rating_bar)
        feedbackMessage = view.findViewById(R.id.feedback_service_text)
        sendBtn = view.findViewById(R.id.send_btn)

        sendBtn?.setOnClickListener {
            if(feedbackMessage?.text.toString().equals("")){
                Toast.makeText(requireContext(), R.string.empty_message_feedback, Toast.LENGTH_LONG).show()
            } else {
                var sumRating = 0.0;
                sumRating = sumRating!! + serviceRatingBar!!.rating.toDouble()
                val data_ADD = hashMapOf(
                    "accountID" to auth.currentUser!!.uid.toString(),
                    "content" to feedbackMessage?.text.toString(),
                    "rating" to sumRating as Number,
                    "time" to Timestamp(Date()) as Timestamp
                )
                db.collection("ReportListings").add(data_ADD)
                    .addOnSuccessListener {
                        Log.i("ASD", "SUCCESS")
                        Toast.makeText(requireContext(), getString(R.string.report_success), Toast.LENGTH_LONG).show()
                        findNavController().popBackStack()
                    }
                    .addOnFailureListener { exception ->
                        Log.i("ASD", "Error getting documents: ", exception)
                    }
            }

        }
        return view
    }


}