package com.example.androiddevelopmentgroup7.views.fragments

import android.content.ContentValues
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.example.androiddevelopmentgroup7.R
import com.example.androiddevelopmentgroup7.models.Profile
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
    var profile: Profile?= null

    lateinit var changePasswordText: TextView
    lateinit var update_btn: Button

    var profile_email_tv: TextView?= null
    var profile_mobile_tv: TextView?= null
    var profile_name_tv: TextView?= null
    var profile_address_tv: TextView?= null
    var profile_rating_tv: TextView?= null
    var profile_payment_details_tv: TextView?= null

    // TODO: Rename and change types of parameters
//    private var param1: String? = null
//    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        arguments?.let {
//            param1 = it.getString(ARG_PARAM1)
//            param2 = it.getString(ARG_PARAM2)
//        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_user_profile, container, false)
        initComponent(view)
        Log.i("UID", Firebase.auth.currentUser!!.uid.toString())

        return view
    }
    private fun initComponent(view: View) {
        this.profile_email_tv = view.findViewById(R.id.profile_email_tv)
        this.profile_mobile_tv = view.findViewById(R.id.profile_phone_tv)
        this.profile_name_tv = view.findViewById(R.id.profile_name_tv)
        this.profile_address_tv = view.findViewById(R.id.profile_address_tv)
        this.profile_rating_tv = view.findViewById(R.id.profile_rating_tv)
        this.profile_payment_details_tv = view.findViewById(R.id.profile_payment_detail_tv)
        this.changePasswordText = view.findViewById(R.id.profile_password_tv)
        this.update_btn = view.findViewById(R.id.update_btn)

        this.changePasswordText.setOnClickListener() {
            findNavController().navigate(R.id.action_profile_fragment_to_fragment_change_password)
        }

        this.update_btn.setOnClickListener() {
            updateProfile()
            Toast.makeText(activity, "The information were updated successfully.", Toast.LENGTH_SHORT).show()
        }
    }

    private fun displayDataInFragment(profile: Profile) {
        profile_email_tv!!.setText(profile.AccountID)
        profile_mobile_tv!!.setText(profile.PhoneNumber)
        profile_name_tv!!.setText(profile.Name)
        profile_address_tv!!.setText(profile.Address)
        profile_rating_tv!!.setText(profile.Rating.toString())
        profile_payment_details_tv!!.setText(profile.PaymentDetails)
    }


    private fun getProfile(accountID: String) {
        database.collection("Customers")
            .whereEqualTo("accountID", accountID)
            .get()
            .addOnSuccessListener { result ->
                for (document in result) {
                    this.profile = Profile(
                        document.data.get("AccountID") as String,
                        document.data.get("Name") as String,
                        document.data.get("PhoneNumber") as String,
                        document.data.get("Address") as String,
                        document.data.get("Rating") as Long,
                        document.data.get("PaymentDetails") as String
                    )
                    //profile!!.showAccountID()
                    displayDataInFragment(profile!!)
                }
            }
            .addOnFailureListener { exception ->
                Log.w(ContentValues.TAG, "Error getting documents.", exception)
            }
    }

    private fun updateProfile() {
        val new_profile = Profile(
            this.profile_email_tv!!.getText().toString(),
            profile_name_tv!!.getText().toString(),
            profile_mobile_tv!!.getText().toString(),
            profile_address_tv!!.getText().toString(),
            profile_rating_tv!!.getText().toString().toLong(),
            profile_payment_details_tv!!.getText().toString())

        database.collection("Customers")
            .document("Customer")
            .set(new_profile)

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