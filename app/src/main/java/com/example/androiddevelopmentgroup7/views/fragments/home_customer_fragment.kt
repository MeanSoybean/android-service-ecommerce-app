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
import com.example.androiddevelopmentgroup7.viewModels.ServiceViewModel
import com.example.androiddevelopmentgroup7.viewModels.UserViewModel
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
        val servicetemp: Service  = serviceList.get(position)
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
//    private var param1: String? = null
//    private var param2: String? = null
    var m_text= ""
    var mapBtn:Button? = null
    var autoCompleteTV: AutoCompleteTextView? = null
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
        val rootView = inflater.inflate(R.layout.home_customer_fragment, container, false)
        initComponent(rootView)
        val recyclerView_services = rootView.findViewById<RecyclerView>(R.id.services_recycler_view)
        mapBtn?.setOnClickListener { findNavController().navigate(R.id.action_home_customer_fragment_to_fragment_near_service_location) }
        serviceViewModel.setServiceListForUser()
        val adapter = MyServiceAdapterCustomerPage(serviceViewModel.selectedServiceList.value!!)
        recyclerView_services.adapter = adapter
        recyclerView_services.layoutManager = LinearLayoutManager(activity)
        adapter.onClick = { service, position ->
            val bundle = Bundle()
            bundle.putInt("position", position)
            bundle.putString("type", service.serviceType)
            bundle.putString("name", service.serviceName)
            bundle.putString("description", service.serviceDescription)
            bundle.putLong("price", service.servicePrice)
            bundle.putString("contact", service.servicePhoneNumber)
            bundle.putString("image", service.serviceImage)
            bundle.putBoolean("negotiate", service.negotiate)
            bundle.putString("vendorID", service.vendorID)
            bundle.putFloat("rating", service.serviceRating)
            bundle.putString("vendorName", service.vendorName)
            bundle.putString("serviceID", service.serviceID)
            findNavController().navigate(R.id.action_home_customer_fragment_to_fragment_service_detail, bundle)
        }
        adapter.onButtonClick = { servicetemp ->
//            val userList = userViewModel.selectedServiceList.value!!
//            val user = userList[0]
//            Log.i("CHECK", userViewModel.selectedServiceList.value!!.toString())
            val builder: AlertDialog.Builder = android.app.AlertDialog.Builder(context)
            builder.setTitle("Ngày đặt lịch")

// Set up the input
            val dateNow = Calendar.getInstance().time
            val input = EditText(context)
// Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
            input.setHint("Mời nhập ngày đặt lịch")
            input.inputType = InputType.TYPE_CLASS_TEXT
            builder.setView(input)

// Set up the buttons
            builder.setPositiveButton("OK", DialogInterface.OnClickListener { dialog, which ->
                // Here you get get input text from the Edittext
                m_text = input.text.toString()
                val calendar = Calendar.getInstance(TimeZone.getDefault())

                val currentYear = calendar[Calendar.YEAR]
                val currentMonth = calendar[Calendar.MONTH] + 1
                val currentDay = calendar[Calendar.DAY_OF_MONTH]
                val hour1 = calendar[Calendar.HOUR]
                val minute1 = calendar[Calendar.MINUTE]
                val time1 = hour1.toString()+':'+minute1.toString() + ' ' + currentDay.toString() + '/' +
                        currentMonth.toString() + '/' +currentYear.toString()
                //val id: String = db.collection("OrderListing").document().getId()
                val data_ADD = hashMapOf(
                    "nameVendor" to servicetemp.serviceName,
                    "idCustomer" to Utils.customer.id,
                    "orderAddress" to "Vẫn còn xung đột với login nên chưa để vô",
                    "orderCurrent" to "Đang chờ xác nhận",
                    "price" to "Thương lượng",
                    "serviceImage" to "",
                    "serviceName" to servicetemp.serviceName,
                    "timeOrder" to time1,
                    "timeComing" to m_text,
                )
                db.collection("OrderListing").add(data_ADD)
//                val intent = Intent(context, MainActivity::class.java)
//                startActivity(intent);

            })
            builder.setNegativeButton("Cancel", DialogInterface.OnClickListener { dialog, which -> dialog.cancel() })

            builder.show()


        }
//        val items = serviceViewModel.selectedServiceList.value!!
//        val name_service = ArrayList<String>()
//        for (item in items){
//            name_service.add(item.serviceName)
//        }

//        autoCompleteTV = rootView.findViewById(R.id.autoCompleteTextView)
//        val adapter1 = context?.let { ArrayAdapter<String>(it, android.R.layout.simple_list_item_single_choice, name_service) }
//        autoCompleteTV!!.setAdapter(adapter1)
//        autoCompleteTV!!.addTextChangedListener(object : TextWatcher {
//            override fun afterTextChanged(p0: Editable?) {}
//            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
//            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
//
//            }
//        })

        serviceViewModel.selectedServiceList.observe(viewLifecycleOwner, Observer { list ->
            // Update the list UI
            adapter.notifyDataSetChanged()
        })
        return rootView
    }

    private fun initComponent(view:View){
        mapBtn = view.findViewById(R.id.view_on_map_btn)
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