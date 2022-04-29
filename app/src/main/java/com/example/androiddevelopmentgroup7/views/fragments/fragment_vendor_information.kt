package com.example.androiddevelopmentgroup7.views.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.androiddevelopmentgroup7.R
import com.google.android.material.appbar.MaterialToolbar

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
        return view
    }
    private fun initComponent(view:View){
        toolbar = view.findViewById(R.id.topAppBar)
        toolbar?.setTitle(getString(R.string.map_app_tittle_text))
        toolbar?.setNavigationOnClickListener { findNavController().popBackStack() }
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