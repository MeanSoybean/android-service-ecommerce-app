package com.example.androiddevelopmentgroup7.views.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.navigation.fragment.findNavController
import com.example.androiddevelopmentgroup7.R
import com.google.android.material.appbar.MaterialToolbar

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"
class OrderDetailsFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    var cart_product_title_tv: TextView? = null
    var cart_product_price_tv: TextView? = null
    var ship_date_value_tv: TextView? = null
    var ship_address_value_tv: TextView? = null
    var ship_curr_status_value_tv: TextView? = null
    var price_total_amount_tv: TextView? = null
    var product_image_view: ImageView?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val service1 = arguments?.getString("OrderDetails")
        val rootView = inflater.inflate(R.layout.fragment_order_details, container, false )
        val temp = service1?.split('%')

        val toolbar:MaterialToolbar = rootView.findViewById(R.id.topAppBar)
        toolbar.setTitle(getString(R.string.order_detail_top_app_bar_text))
        toolbar.setNavigationOnClickListener { findNavController().popBackStack() }


        cart_product_title_tv = rootView.findViewById(R.id.cart_product_title_tv)
        ship_date_value_tv = rootView.findViewById(R.id.ship_date_value_tv)
        cart_product_price_tv = rootView.findViewById(R.id.cart_product_price_tv)
        ship_address_value_tv = rootView.findViewById(R.id.ship_address_value_tv)
        ship_curr_status_value_tv = rootView.findViewById(R.id.ship_curr_status_value_tv)
        price_total_amount_tv = rootView.findViewById(R.id.price_total_amount_tv)
        product_image_view = rootView.findViewById(R.id.product_image_view)


        product_image_view?.setImageResource(R.drawable.ava)
        cart_product_title_tv?.text = temp?.get(0)
        ship_date_value_tv?.text = temp?.get(1)
        cart_product_price_tv?.text=(temp?.get(4))
        ship_address_value_tv?.text= (temp?.get(2))
        ship_curr_status_value_tv?.text=(temp?.get(3))
        price_total_amount_tv?.text=(temp?.get(4))
        return rootView
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
        fun newInstance(param1: String, param2: String) =
            OrderDetailsFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}