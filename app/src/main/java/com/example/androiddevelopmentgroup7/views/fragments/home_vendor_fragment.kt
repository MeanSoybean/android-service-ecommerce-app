package com.example.androiddevelopmentgroup7.views.fragments

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.AsyncTask
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.androiddevelopmentgroup7.R
import com.example.androiddevelopmentgroup7.models.Service
import com.example.androiddevelopmentgroup7.utils.DownloadImageFromInternet
import com.example.androiddevelopmentgroup7.viewModels.ServiceViewModel
import com.google.android.material.dialog.MaterialAlertDialogBuilder
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
    lateinit var onClickListener: OnClickListener



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
        holder.cost_vendor_service.text = serviceList.get(position).servicePrice.toString()
        DownloadImageFromInternet(holder.service_image_view).execute(serviceList.get(position).serviceImage)
        holder.service_rating_bar.rating = serviceList.get(position).serviceRating


        //event
        holder.service_edit_btn.setOnClickListener {
            Log.i("EDIT", "edit")
            onClickListener.onEditClick(position)
        }

        holder.service_delete_btn.setOnClickListener {
            Log.i("DELETE", "delete")
            onClickListener.onDeleteClick(position)
        }
    }

    override fun getItemCount(): Int {
        return serviceList.size
    }


    interface OnClickListener {
        fun onEditClick(position: Int)
        fun onDeleteClick(position: Int)
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

        serviceViewModel.setServiceList()
        val adapter = MyServiceAdapter(serviceViewModel.selectedServiceList.value!!)
        adapter.onClickListener = object : MyServiceAdapter.OnClickListener {
            override fun onEditClick(position: Int) {
                val service = serviceViewModel.selectedServiceList.value!!.get(position)
                val bundle = Bundle()
                bundle.putString("type_activity", "edit")
                bundle.putInt("position", position)
                bundle.putString("type", service.serviceType)
                bundle.putString("name", service.serviceName)
                bundle.putString("description", service.serviceDescription)
                bundle.putLong("price", service.servicePrice)
                bundle.putString("contact", service.servicePhoneNumber)
                bundle.putString("image", service.serviceImage)
                bundle.putBoolean("negotiate", service.negotiate)
                bundle.putString("vendorID", service.vendorID)
                bundle.putString("vendorName", service.vendorName)
                bundle.putFloat("rating", service.serviceRating)
                findNavController().navigate(R.id.action_home_vendor_fragment_to_service_details_vendor_fragment, bundle)
            }
            override fun onDeleteClick(position: Int) {
                showDeleteDialog(position)
            }
        }
        recyclerView_services.adapter = adapter
        recyclerView_services.layoutManager = LinearLayoutManager(activity)

        val loader = rootView.findViewById<FrameLayout>(R.id.loader_layout)

        val addBtn = rootView.findViewById<Button>(R.id.service_add_btn)
        addBtn.setOnClickListener {
            val bundle = Bundle()
            bundle.putString("type_activity", "add")
            findNavController().navigate(R.id.action_home_vendor_fragment_to_service_details_vendor_fragment, bundle)
            //loader.visibility = View.GONE
        }

        serviceViewModel.selectedServiceList.observe(viewLifecycleOwner, Observer { list ->
            // Update the list UI
            adapter.notifyDataSetChanged()
            Log.i("DATASET CHANGE", "data set change")
        })

        serviceViewModel.status.observe(viewLifecycleOwner, Observer { status ->
            // Update the list UI
            when(status){
                "hide_loader" -> {
                    loader.visibility = View.GONE
                    addBtn.isClickable = true
                }
                "loading"-> {
                    loader.visibility = View.VISIBLE
                    addBtn.isClickable = false
                }
                "delete_success" -> {
                    loader.visibility = View.GONE
                    addBtn.isClickable = true
                }
            }
        })


        return rootView
    }

    private fun showDeleteDialog(position: Int) {
        context?.let {
            MaterialAlertDialogBuilder(requireContext())
                .setTitle(getString(R.string.delete_dialog_title_text))
                .setMessage(getString(R.string.delete_service_message_text))
                .setNeutralButton(getString(R.string.pro_cat_dialog_cancel_btn)) { dialog, _ ->
                    dialog.cancel()
                }
                .setPositiveButton(getString(R.string.delete_dialog_delete_btn_text)) { dialog, _ ->
                    serviceViewModel.deleteService(position)
                    dialog.cancel()
                }
                .show()
        }
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