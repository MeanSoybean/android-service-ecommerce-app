package com.example.androiddevelopmentgroup7.views.fragments

import android.app.AlertDialog
import android.content.DialogInterface
import android.os.Bundle
import android.text.Editable
import android.text.InputType
import android.text.TextWatcher
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
import com.example.androiddevelopmentgroup7.utils.Utils
import com.example.androiddevelopmentgroup7.models.Service
import com.example.androiddevelopmentgroup7.utils.DownloadImageFromInternet
import com.example.androiddevelopmentgroup7.utils.SortingAccording
import com.example.androiddevelopmentgroup7.utils.SortingType
import com.example.androiddevelopmentgroup7.viewModels.ServiceViewModel
import com.example.androiddevelopmentgroup7.viewModels.UserViewModel
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import java.util.*
import kotlin.collections.ArrayList


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [home_customer_fragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class MyServiceAdapterCustomerPage(private var serviceList: ArrayList<Service>): RecyclerView.Adapter<MyServiceAdapterCustomerPage.ViewHolder>(){
    inner class ViewHolder(listItemView: View): RecyclerView.ViewHolder(listItemView){
        var name_vendor_service: TextView = listItemView.findViewById(R.id.name_vendor_service)
        var name_of_vendor: TextView = listItemView.findViewById(R.id.name_of_vendor)
        var description_vendor_service: TextView = listItemView.findViewById(R.id.description_vendor_service)
        var cost_vendor_service: TextView = listItemView.findViewById(R.id.cost_vendor_service)
        var service_image_view: ImageView = listItemView.findViewById(R.id.service_image_view)
        var service_rating_bar: RatingBar = listItemView.findViewById(R.id.service_rating_bar)
        init {
            val messageBtnTemp = listItemView.findViewById<Button>(R.id.service_edit_btn)
            messageBtnTemp.setOnClickListener {
                onButtonClick?.invoke(serviceList[adapterPosition])
            }
            listItemView.setOnClickListener { onClick?.invoke(serviceList[adapterPosition], adapterPosition) }
        }
    }


    var onButtonClick: ((Service) -> Unit)? = null
    var onClick:((Service, Int) -> Unit)? = null
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val context = parent.context
        val inflater = LayoutInflater.from(context)
        val serviceView = inflater.inflate(R.layout.layout_home, parent, false)


        return ViewHolder(serviceView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        (serviceList.get(position).serviceName + " - " + serviceList.get(position).serviceType).also { holder.name_vendor_service.text = it }
        holder.name_of_vendor.text = serviceList.get(position).serviceName
        holder.description_vendor_service.text = serviceList.get(position).serviceDescription
        holder.cost_vendor_service.text = serviceList.get(position).servicePrice.toString()
        DownloadImageFromInternet(holder.service_image_view).execute(serviceList.get(position).serviceImage)
        holder.service_rating_bar.rating = serviceList.get(position).serviceRating
    }

    override fun getItemCount(): Int {
        return serviceList.size
    }

}


class home_customer_fragment : Fragment() {
    val db = Firebase.firestore

    // TODO: Rename and change types of parameters
//    private var sortType:Int? = null
//    private var sortAccording:Int? = null
    private var mapBtn:Button? = null
    private var sortingSpinner: Spinner? = null
    private var filterSpnner:Spinner? = null
    private val serviceViewModel : ServiceViewModel by activityViewModels()
    private var loader:FrameLayout? = null
    private var serviceType = ArrayList<String>()

    private var searchBar: TextInputLayout? = null
    private var searchContent:TextInputEditText? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        arguments?.let {
//            sortType = it.getInt("SortingType")
//            sortAccording = it.getInt("SortingAccording")
//        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        Log.i("ASD", "Oncreate view")
        // Inflate the layout for this fragment
        val rootView = inflater.inflate(R.layout.home_customer_fragment, container, false)
        initComponent(rootView)
        serviceViewModel.setServicesTypeFromDatabase()
        val recyclerView_services = rootView.findViewById<RecyclerView>(R.id.services_recycler_view)
        mapBtn?.setOnClickListener { findNavController().navigate(R.id.action_home_customer_fragment_to_fragment_near_service_location) }
        serviceViewModel.setServiceListForUser()
        val adapter = MyServiceAdapterCustomerPage(serviceViewModel.selectedServiceList.value!!)
        recyclerView_services.adapter = adapter
        recyclerView_services.layoutManager = LinearLayoutManager(activity)
        adapter.onClick = {service, position -> cartItemClick(service, position)}
        serviceViewModel.selectedServiceList.observe(viewLifecycleOwner, Observer { list ->
            // Update the list UI
            adapter.notifyDataSetChanged()
        })

        serviceViewModel.serviceTypeLivaData.observe(viewLifecycleOwner, Observer { serviceTypeList ->
            serviceTypeList.add(0, getString(R.string.all_service))
            serviceType.addAll(serviceTypeList)
            val filterAdapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, serviceTypeList)
            filterAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            filterSpnner?.adapter = filterAdapter
        })
        serviceViewModel.status.observe(viewLifecycleOwner, Observer { status ->
            // Update the list UI
            when(status){
                "hide_loader" -> {
                    loader?.visibility = View.GONE
                }
                "loading"-> {
                    loader?.visibility = View.VISIBLE
                }
            }
        })
        return rootView
    }

    private fun cartItemClick(service:Service, position: Int){
        val bundle = Bundle()
        bundle.putInt("position", position)
        bundle.putString("type", service.serviceType)
        bundle.putString("name", service.serviceName)
        bundle.putString("description", service.serviceDescription)
        bundle.putLong("price", service.servicePrice)
        bundle.putString("contact", service.servicePhoneNumber)
        bundle.putString("image", service.serviceImage)
        //bundle.putBoolean("negotiate", service.negotiate)
        bundle.putString("vendorID", service.vendorID)
        bundle.putFloat("rating", service.serviceRating)
        bundle.putString("vendorName", service.vendorName)
        bundle.putString("serviceID", service.serviceID)
        findNavController().navigate(R.id.action_home_customer_fragment_to_fragment_service_detail, bundle)
    }

    private fun initComponent(view:View) {
        loader = view.findViewById(R.id.loader_layout)
        mapBtn = view.findViewById(R.id.view_on_map_btn)
        sortingSpinner = view.findViewById(R.id.sort_service_btn)
        filterSpnner = view.findViewById(R.id.filter_type_service_btn)
        searchBar = view.findViewById(R.id.searchOutlinedTextLayout)
        searchContent = view.findViewById(R.id.home_search_edit_text)

        searchBar?.setEndIconOnClickListener {
            Log.i("ASD", "Button click")
            val name = searchContent?.text.toString()
            sortingSpinner?.setSelection(0)
            filterSpnner?.setSelection(0)
            //serviceViewModel.findServiceByName(name)
        }
        val sortingAdapter = ArrayAdapter.createFromResource(
            requireContext(),
            R.array.sorting_list,
            android.R.layout.simple_spinner_item
        )
        sortingAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        sortingSpinner?.adapter = sortingAdapter


        filterSpnner?.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(p0: AdapterView<*>?) {}
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, index: Int, p3: Long) {runFilterStamentSorting(index) }
        }

        sortingSpinner?.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(p0: AdapterView<*>?) {}
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, index: Int, p3: Long) {runSortingStamentSorting(index) }
        }
    }


    private fun runSortingStamentSorting(index:Int){
        var typeOfService = ""
        var sortingType = ""
        var sortingAccording = ""
        when(filterSpnner?.selectedItem.toString()){
            serviceType.get(1) -> typeOfService = serviceType.get(1)
            serviceType.get(2) -> typeOfService = serviceType.get(2)
            serviceType.get(3) -> typeOfService = serviceType.get(3)
            serviceType.get(4) -> typeOfService = serviceType.get(4)
        }
        when(index){
            1 -> {
                sortingType = "servicePrice"
                sortingAccording = "asc"
            }
            2 -> {
                sortingType = "servicePrice"
                sortingAccording = "des"
            }
            3 -> {
                sortingType = "serviceRating"
                sortingAccording = "asc"
            }
            4 -> {
                sortingType = "serviceRating"
                sortingAccording = "des"
            }
        }
        serviceViewModel.queryDataFilter(sortingType, sortingAccording, typeOfService)
    }


    private fun runFilterStamentSorting(index:Int){
        var typeOfService = ""
        var sortingType = ""
        var sortingAccording = ""
        when(sortingSpinner?.selectedItemPosition){
            1 -> {
                sortingType = "servicePrice"
                sortingAccording = "asc"
            }
            2 -> {
                sortingType = "servicePrice"
                sortingAccording = "des"
            }
            3 -> {
                sortingType = "serviceRating"
                sortingAccording = "asc"
            }
            4 -> {
                sortingType = "serviceRating"
                sortingAccording = "des"
            }
        }

        if(index!=0){
            typeOfService = filterSpnner?.selectedItem.toString()
        }
        serviceViewModel.queryDataFilter(sortingType, sortingAccording, typeOfService)
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