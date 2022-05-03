package com.example.androiddevelopmentgroup7.views.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.example.androiddevelopmentgroup7.R
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER


/**
 * A simple [Fragment] subclass.
 * Use the [notification_details.newInstance] factory method to
 * create an instance of this fragment.
 */
class notification_details : Fragment() {
    // TODO: Rename and change types of parameters


    private var name:String? = null
    private var time:String? = null
    private var des:String? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            name = it.getString("name")
            time = it.getString("time")
            des = it.getString("description")
        }
    }
    private var timeView: TextView? = null
    private var nameView: TextView? = null
    private var desView: TextView? = null
    private var imageView: ImageView? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.fragment_notification_details, container, false)
        nameView = view.findViewById(R.id.cart_product_title_tv)
        timeView = view.findViewById(R.id.cart_product_type_tv)
        desView = view.findViewById(R.id.time_coming_text)
        imageView = view.findViewById(R.id.product_image_view)
        nameView?.setText(name)
        timeView?.setText(time)
        desView?.setText(des)
        imageView?.setImageResource(R.drawable.noti)
        // Inflate the layout for this fragment
        return view
    }


}