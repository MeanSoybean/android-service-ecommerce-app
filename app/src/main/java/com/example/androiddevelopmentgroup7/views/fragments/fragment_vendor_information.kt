package com.example.androiddevelopmentgroup7.views.fragments

import android.content.ContentValues
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.navigation.fragment.findNavController
import com.example.androiddevelopmentgroup7.R
import com.example.androiddevelopmentgroup7.models.Profile
import com.google.android.material.appbar.MaterialToolbar
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [fragment_vendor_information.newInstance] factory method to
 * create an instance of this fragment.
 */
class fragment_vendor_information : Fragment() {
    // TODO: Rename and change types of parameters
    private var vendorID: String? = null
    private var toolbar:MaterialToolbar? = null
    private var profile_email_tv:TextView? = null
    private var profile_name_tv:TextView? = null
    private var profile_rating_tv:TextView? = null
    private var profile_mobile_tv:TextView? = null
    private var profile_address_tv:TextView? = null
    private val db = Firebase.firestore
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            vendorID = it.getString("vendorID")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        Log.i("vendorID", vendorID!!)
        val view =  inflater.inflate(R.layout.fragment_vendor_information, container, false)
        initComponent(view)
        getProfile()
        return view
    }
    private fun initComponent(view:View){
        toolbar = view.findViewById(R.id.topAppBar)
        toolbar?.setTitle(getString(R.string.vendor_information))
        toolbar?.setNavigationOnClickListener { findNavController().popBackStack() }

        this.profile_email_tv = view.findViewById(R.id.profile_email_tv)
        this.profile_mobile_tv = view.findViewById(R.id.profile_phone_tv)
        this.profile_name_tv = view.findViewById(R.id.profile_name_tv)
        this.profile_address_tv = view.findViewById(R.id.profile_address_tv)
        this.profile_rating_tv = view.findViewById(R.id.profile_rating_tv)

    }
    private fun displayDataInFragment(profile: Profile) {
        this.profile_email_tv!!.setText(profile.email)
        this.profile_mobile_tv!!.setText(profile.phoneNumber)
        this.profile_name_tv!!.setText(profile.name)
        this.profile_address_tv!!.setText(profile.address)
        this.profile_rating_tv!!.setText(profile.rating)
    }
    private fun getProfile() {
        db.collection("Vendors").document("qNLu7DVILPNpmw8ST8IjBdpgGXF2")
            .get()
            .addOnSuccessListener { result ->
                    val profile = Profile(
                        result.data!!.get("email").toString(),
                        result.data!!.get("name").toString(),
                        result.data!!.get("phoneNumber").toString(),
                        result.data!!.get("address").toString(),
                        result.data!!.get("rating").toString(),
                        result.data!!.get("paymentDetails").toString(),
                        result.data!!.get("accountID").toString()
                    )
                    displayDataInFragment(profile)
            }
            .addOnFailureListener { exception ->
                Log.w(ContentValues.TAG, "Error getting documents.", exception)
            }

    }
    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment fragment_vendor_information.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            fragment_vendor_information().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}