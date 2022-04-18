package com.example.androiddevelopmentgroup7

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.RatingBar
import android.widget.TextView
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.androiddevelopmentgroup7.R
import com.example.androiddevelopmentgroup7.dataModels.Service
import com.example.androiddevelopmentgroup7.viewModels.ServiceViewModel
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
//private const val ARG_PARAM1 = "param1"
//private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [home_vendor_fragment.newInstance] factory method to
 * create an instance of this fragment.
 */


class MyServiceAdapter(private var serviceList:ArrayList<Service>): RecyclerView.Adapter<MyServiceAdapter.ViewHolder>(){
    inner class ViewHolder(listItemView: View):RecyclerView.ViewHolder(listItemView){
        var name_vendor_service: TextView = listItemView.findViewById(R.id.name_vendor_service)
        var description_vendor_service: TextView = listItemView.findViewById(R.id.description_vendor_service)
        var cost_vendor_service: TextView = listItemView.findViewById(R.id.cost_vendor_service)
        var service_image_view: ImageView = listItemView.findViewById(R.id.service_image_view)
        var service_rating_bar: RatingBar = listItemView.findViewById(R.id.service_rating_bar)
        var service_edit_btn: Button = listItemView.findViewById(R.id.service_edit_btn)
        var service_delete_btn: ImageView = listItemView.findViewById(R.id.service_delete_btn)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val context = parent.context
        val inflater = LayoutInflater.from(context)
        val serviceView = inflater.inflate(R.layout.layout_vendor_my_service, parent, false)
        return ViewHolder(serviceView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        (serviceList.get(position).serviceName + " - " + serviceList.get(position).serviceType).also { holder.name_vendor_service.text = it }
        holder.description_vendor_service.text = serviceList.get(position).serviceDescription
        holder.cost_vendor_service.text = serviceList.get(position).servicePrice
        holder.service_image_view.setBackgroundResource(R.drawable.ic_add_48)
        holder.service_rating_bar.rating = (3.5).toFloat()
//        holder.contact_vendor_service.text = serviceList.get(position).serviceContact
        //event
        holder.service_edit_btn.setOnClickListener {
            Log.i("EDIT", "edit")
        }

        holder.service_delete_btn.setOnClickListener {
            Log.i("DELETE", "delete")
        }
    }

    override fun getItemCount(): Int {
        return serviceList.size
    }

}



class home_vendor_fragment : Fragment() {
    val db = Firebase.firestore
    // TODO: Rename and change types of parameters
//    private var param1: String? = null
//    private var param2: String? = null
    private val serviceViewModel : ServiceViewModel by activityViewModels()
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
        val rootView = inflater.inflate(R.layout.home_vendor_fragment, container, false)
        val recyclerView_services = rootView.findViewById<RecyclerView>(R.id.services_recycler_view)

        val adapter = MyServiceAdapter(serviceViewModel.selectedServiceList.value!!)
        recyclerView_services.adapter = adapter
        recyclerView_services.layoutManager = LinearLayoutManager(activity)

        val addBtn = rootView.findViewById<Button>(R.id.service_add_btn)
        addBtn.setOnClickListener {
            findNavController().navigate(R.id.action_home_vendor_fragment_to_service_details_vendor_fragment)
        }

        serviceViewModel.selectedServiceList.observe(viewLifecycleOwner, Observer { list ->
            // Update the list UI
            adapter.notifyItemInserted(list.size - 1)
        })
        return rootView
    }

//    companion object {
//        /**
//         * Use this factory method to create a new instance of
//         * this fragment using the provided parameters.
//         *
//         * @param param1 Parameter 1.
//         * @param param2 Parameter 2.
//         * @return A new instance of fragment home_vendor_fragment.
//         */
//        // TODO: Rename and change types and number of parameters
//        @JvmStatic
//        fun newInstance(param1: String, param2: String) =
//            home_vendor_fragment().apply {
//                arguments = Bundle().apply {
//                    putString(ARG_PARAM1, param1)
//                    putString(ARG_PARAM2, param2)
//                }
//            }
//    }
}