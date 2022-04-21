package com.example.androidgr7



import android.app.AlertDialog
import android.content.DialogInterface
import android.os.Bundle
import android.text.InputType
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.activity.viewModels
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.androidgr7.dataModels.Service
import com.example.androidgr7.viewModels.ServiceViewModel
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.example.androidgr7.viewModels.UserViewModel
import com.example.androidgr7.dataModels.UserCustomer
import java.util.*
import kotlin.collections.ArrayList

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
//private const val ARG_PARAM1 = "param1"
//private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [home_customer_fragment.newInstance] factory method to
 * create an instance of this fragment.
 */


class MyServiceAdapter(private var serviceList:ArrayList<Service>): RecyclerView.Adapter<MyServiceAdapter.ViewHolder>(){
    inner class ViewHolder(listItemView: View):RecyclerView.ViewHolder(listItemView){
        var name_vendor_service: TextView = listItemView.findViewById(R.id.name_vendor_service)
        var name_of_vendor: TextView = listItemView.findViewById(R.id.name_of_vendor)
        var description_vendor_service: TextView = listItemView.findViewById(R.id.description_vendor_service)
        var cost_vendor_service: TextView = listItemView.findViewById(R.id.cost_vendor_service)
        var service_image_view: ImageView = listItemView.findViewById(R.id.service_image_view)
        var service_rating_bar: RatingBar = listItemView.findViewById(R.id.service_rating_bar)
        var service_edit_btn: Button = listItemView.findViewById(R.id.service_edit_btn)
        init {
            val messageBtnTemp = listItemView.findViewById<Button>(R.id.service_edit_btn)
            messageBtnTemp.setOnClickListener {
                onButtonClick?.invoke(serviceList[adapterPosition])
            }
        }
    }
    var onButtonClick: ((Service) -> Unit)? = null
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val context = parent.context
        val inflater = LayoutInflater.from(context)
        val serviceView = inflater.inflate(R.layout.layout_vendor_my_service, parent, false)
        return ViewHolder(serviceView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val servicetemp: Service =serviceList.get(position)
        (serviceList.get(position).serviceName + " - " + serviceList.get(position).serviceType).also { holder.name_vendor_service.text = it }
        holder.name_of_vendor.text = serviceList.get(position).Name
        holder.description_vendor_service.text = serviceList.get(position).serviceDescription
        holder.cost_vendor_service.text = serviceList.get(position).servicePrice
        holder.service_image_view.setBackgroundResource(R.drawable.ic_add_48)
        holder.service_rating_bar.rating = serviceList.get(position).serviceRating.toFloat()
//        holder.contact_vendor_service.text = serviceList.get(position).serviceContact
        //event

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

    private val serviceViewModel : ServiceViewModel by activityViewModels()
    private val userViewModel:UserViewModel by activityViewModels()
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
        val recyclerView_services = rootView.findViewById<RecyclerView>(R.id.services_recycler_view)


        val adapter = MyServiceAdapter(serviceViewModel.selectedServiceList.value!!)
        recyclerView_services.adapter = adapter
        recyclerView_services.layoutManager = LinearLayoutManager(activity)

        adapter.onButtonClick= {servicetemp ->
            val userList = userViewModel.selectedServiceList.value!!
            val user = userList[0]
            Log.d("CHECK",userViewModel.selectedServiceList.value!!.toString())
            val builder: AlertDialog.Builder = android.app.AlertDialog.Builder(context)
            builder.setTitle("Title")

// Set up the input
            val dateNow = Calendar.getInstance().time
            val input = EditText(context)
// Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
            input.setHint("Enter Text")
            input.inputType = InputType.TYPE_CLASS_TEXT
            builder.setView(input)

// Set up the buttons
            builder.setPositiveButton("OK", DialogInterface.OnClickListener { dialog, which ->
                // Here you get get input text from the Edittext
                m_text = input.text.toString()

                val id: String = db.collection("OrderListing").document().getId()
                val data_ADD = hashMapOf(
                    "nameVendor" to servicetemp.Name,
                    "idCustomer" to user.AccountID,
                    "orderAddress" to user.Address,
                    "orderCurrent" to "Đang chờ xác nhận",
                    "price" to "Thương lượng",
                    "serviceImage" to "",
                    "serviceName" to servicetemp.serviceName,
                    "timeOrder" to m_text,
                    "timeComing" to dateNow.toString()
                )
                db.collection("OrderListing").document(id).set(data_ADD)

            })
            builder.setNegativeButton("Cancel", DialogInterface.OnClickListener { dialog, which -> dialog.cancel() })

            builder.show()



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