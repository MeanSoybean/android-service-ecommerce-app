package com.example.androiddevelopmentgroup7.views.fragments

import android.content.ContentValues
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.example.androiddevelopmentgroup7.R
import com.example.androiddevelopmentgroup7.models.Profile
import com.example.androiddevelopmentgroup7.models.UserLocation
import com.google.android.material.appbar.MaterialToolbar
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [ProfileFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ProfileFragment : Fragment() {
    val database = Firebase.firestore
    val auth = Firebase.auth

    private var chang_password_text: TextView?= null
    private var profile_update_tv: TextView?= null
    private var profile_email_tv: TextView?= null
    private var profile_mobile_tv: TextView?= null
    private var profile_name_tv: TextView?= null
    private var profile_address_tv: TextView?= null
    private var profile_rating_tv: TextView?= null
    private var profile_payment_details_tv: TextView?= null
    private var toolbar: MaterialToolbar? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_user_profile, container, false)
        initComponent(view)
        getProfile(auth.currentUser!!.uid)
        return view
    }
    private fun initComponent(view: View) {
        this.profile_email_tv = view.findViewById(R.id.profile_email_tv)
        this.profile_mobile_tv = view.findViewById(R.id.profile_phone_tv)
        this.profile_name_tv = view.findViewById(R.id.profile_name_tv)
        this.profile_address_tv = view.findViewById(R.id.profile_address_tv)
        this.profile_rating_tv = view.findViewById(R.id.profile_rating_tv)
        this.profile_payment_details_tv = view.findViewById(R.id.profile_payment_detail_tv)
        this.chang_password_text = view.findViewById(R.id.profile_password_tv)
        this.profile_update_tv = view.findViewById(R.id.profile_update_tv)

        toolbar = view.findViewById(R.id.topAppBar)
        toolbar?.setTitle(getString(R.string.personal_information_top_app_bar_text))
        toolbar?.setNavigationOnClickListener {
            findNavController().popBackStack()
        }

        this.chang_password_text!!.setOnClickListener() {
            findNavController().navigate(R.id.action_profile_fragment_to_fragment_change_password)
        }

        this.profile_update_tv!!.setOnClickListener() {
            updateProfile()
            Toast.makeText(activity, "The information were updated successfully.", Toast.LENGTH_SHORT).show()
        }
    }

    private fun displayDataInFragment(profile: Profile) {
        this.profile_email_tv!!.setText(profile.email)
        this.profile_mobile_tv!!.setText(profile.phoneNumber)
        this.profile_name_tv!!.setText(profile.name)
        this.profile_address_tv!!.setText(profile.address)
        this.profile_rating_tv!!.setText(profile.rating)
        this.profile_payment_details_tv!!.setText(profile.paymentDetails)
    }

    private fun getProfile(accountID: String) {
        this.database.collection("Customers")
            .whereEqualTo("accountID", accountID)
            .get()
            .addOnSuccessListener { result ->
                for (document in result) {
                    val profile = Profile(
                        document.data.get("email").toString(),
                        document.data.get("name").toString(),
                        document.data.get("phoneNumber").toString(),
                        document.data.get("address").toString(),
                        document.data.get("rating").toString(),
                        document.data.get("paymentDetails").toString(),
                        document.data.get("accountID").toString()
                    )
                    displayDataInFragment(profile)
                }
            }
            .addOnFailureListener { exception ->
                Log.w(ContentValues.TAG, "Error getting documents.", exception)
            }
    }

    private fun updateProfile() {
        val new_profile = Profile(
            this.profile_email_tv!!.getText().toString(),
            this.profile_name_tv!!.getText().toString(),
            this.profile_mobile_tv!!.getText().toString(),
            this.profile_address_tv!!.getText().toString(),
            this.profile_rating_tv!!.getText().toString(),
            this.profile_payment_details_tv!!.getText().toString(),
            this.auth.currentUser!!.uid)

        database.collection("Customers")
            .document(auth.currentUser!!.uid)
            .set(new_profile)

        saveCurrentLocation()
    }

    private fun saveCurrentLocation() {
        this.database.collection("Locations")
            .whereEqualTo("accountID", auth.currentUser!!.uid)
            .get()
            .addOnSuccessListener { result ->
                for (document in result) {
                    val userLocation = UserLocation(
                        document.data.get("accountID").toString(),
                        this.profile_address_tv!!.getText().toString(),
                        document.data.get("latitude").toString(),
                        document.data.get("longitude").toString(),
                    )
//                    Log.i("HIHI", userLocation.toString())
                    this.database.collection("Locations")
                        .document(this.auth.currentUser!!.uid)
                        .set(userLocation)
                }
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
         * @return A new instance of fragment test2Fragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            ProfileFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}