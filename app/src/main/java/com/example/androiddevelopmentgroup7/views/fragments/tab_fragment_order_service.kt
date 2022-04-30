package com.example.androiddevelopmentgroup7.views.fragments
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.TextView
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.androiddevelopmentgroup7.R
import com.example.androiddevelopmentgroup7.models.Order
import com.example.androiddevelopmentgroup7.models.Service
import com.example.androiddevelopmentgroup7.utils.OrderTabValue
import com.example.androiddevelopmentgroup7.utils.Utils
import com.google.firebase.Timestamp
import com.google.firebase.firestore.FieldPath
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import java.text.SimpleDateFormat
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

class MyOrderAdapter(var context: Context, private var inforList:ArrayList<HashMap<String, Any>>): RecyclerView.Adapter<MyOrderAdapter.ViewHolder>(){
    var onItemClick: ((HashMap<String, Any>) -> Unit)? = null
    inner class ViewHolder(listItemView: View): RecyclerView.ViewHolder(listItemView){
        var serviceName: TextView = listItemView.findViewById(R.id.order_summary_id_tv)
        var nameVendor: TextView = listItemView.findViewById(R.id.name_fix_value_tv)
        var timeOrder: TextView = listItemView.findViewById(R.id.order_summary_date_tv)
        var timeComing: TextView = listItemView.findViewById(R.id.order_time_coming_tv)
        var price: TextView = listItemView.findViewById(R.id.order_summary_total_amount_tv)
        var orderCurrent: TextView = listItemView.findViewById(R.id.order_summary_status_value_tv)
        var userLabe: TextView = listItemView.findViewById(R.id.name_fix_tv)
        init {
                listItemView.setOnClickListener {onItemClick?.invoke(inforList.get(adapterPosition))}
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val context = parent.context
        val inflater = LayoutInflater.from(context)
        val serviceView = inflater.inflate(R.layout.layout_order_service, parent, false)
        return ViewHolder(serviceView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val formatter = SimpleDateFormat("HH:mm:ss - dd/MM/yyyy")
        val date = (inforList.get(position).get("timeOrder") as Timestamp).toDate()
        val convertDay = formatter.format(date)

        holder.serviceName.text = inforList.get(position).get("serviceName").toString()
        //0: customer 1:vendor
        if(Utils.typeUser == 1){
            holder.userLabe.text = context.getString(R.string.name_customer_text)
            holder.nameVendor.text = inforList.get(position).get("customerName").toString()
        } else {
            holder.userLabe.text = context.getString(R.string.name_vendor_text)
            holder.nameVendor.text = inforList.get(position).get("vendorName").toString()
        }
        holder.timeOrder.text = convertDay
        holder.timeComing.text = inforList.get(position).get("timeComing").toString()
        holder.price.setText(inforList.get(position).get("price").toString() + " VNĐ")
        when(inforList.get(position).get("orderCurrent")){
            OrderTabValue.WAITING_ACCEPT -> holder.orderCurrent.setText(context.getString(R.string.accept_tab_text))
            OrderTabValue.ON_GOING -> holder.orderCurrent.setText(context.getString(R.string.on_board_tab_text))
            OrderTabValue.COMPLETE -> holder.orderCurrent.setText(context.getString(R.string.complete_tab_text))
            OrderTabValue.CANCEL -> holder.orderCurrent.setText(context.getString(R.string.cancel_tab_text))
        }
    }

    override fun getItemCount(): Int {
        return inforList.size
    }
    interface OnClickListener {
        fun onCardClick(orderId: String)
    }
}


class TabFragmentOrderService() : Fragment() {
    val db = Firebase.firestore
//    private val orderListModel : OrderListModel by activityViewModels()
//    private val serviceListModel : ServiceViewModel by activityViewModels()

    private var tabIndex: Int? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            tabIndex = it.getInt("tabIndex")
            Log.i("TABINDEX", tabIndex.toString())
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val rootView = inflater.inflate(R.layout.tab_fragment_order_service, container, false)
        val recyclerView_services = rootView.findViewById<RecyclerView>(R.id.orders_list_recycler_view)
        val loader = rootView.findViewById<FrameLayout>(R.id.loader_layout)
        //0:Customer 1: Vendor
        if(Utils.typeUser == 1){
            when(tabIndex){
                OrderTabValue.ALL -> createListItem("idVendor", Utils.vendor.id, OrderTabValue.ALL,recyclerView_services, loader)
                OrderTabValue.WAITING_ACCEPT -> createListItem("idVendor", Utils.vendor.id, OrderTabValue.WAITING_ACCEPT,recyclerView_services, loader)
                OrderTabValue.ON_GOING -> createListItem("idVendor", Utils.vendor.id, OrderTabValue.ON_GOING,recyclerView_services, loader)
                OrderTabValue.COMPLETE -> createListItem("idVendor", Utils.vendor.id, OrderTabValue.COMPLETE,recyclerView_services, loader)
                OrderTabValue.CANCEL -> createListItem("idVendor", Utils.vendor.id, OrderTabValue.CANCEL,recyclerView_services, loader)
            }
        } else {
            when(tabIndex){
                OrderTabValue.ALL -> createListItem("idCustomer", Utils.customer.id, OrderTabValue.ALL,recyclerView_services, loader)
                OrderTabValue.WAITING_ACCEPT -> createListItem("idCustomer", Utils.customer.id, OrderTabValue.WAITING_ACCEPT,recyclerView_services, loader)
                OrderTabValue.ON_GOING -> createListItem("idCustomer", Utils.customer.id, OrderTabValue.ON_GOING,recyclerView_services, loader)
                OrderTabValue.COMPLETE -> createListItem("idCustomer", Utils.customer.id, OrderTabValue.COMPLETE,recyclerView_services, loader)
                OrderTabValue.CANCEL -> createListItem("idCustomer", Utils.customer.id, OrderTabValue.CANCEL,recyclerView_services, loader)
            }
        }


        return rootView
    }

    private fun itemClickApdater(adapter: MyOrderAdapter){
        adapter.onItemClick = { hasmap ->
            val bundle = Bundle()
            Log.i("ASD", hasmap.get("idOrder").toString())
            bundle.putString("idOrder", hasmap.get("idOrder").toString())
            findNavController().navigate(R.id.action_orderServiceVendorFragment_to_orderDetailsFragment, bundle)
        }
    }

    private fun createListItem(typeUserID:String, userID:String, filter:Int, recyclerView: RecyclerView, loader:FrameLayout){
        val orderList = ArrayList<Order>()
        val serviceList = ArrayList<Service>()
        val inforList = ArrayList<HashMap<String, Any>>()
        val adapter = MyOrderAdapter(requireContext(), inforList)
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(activity)
        itemClickApdater(adapter)
        var query = db.collection("OrderListing").whereEqualTo(typeUserID, userID)
        if(filter != OrderTabValue.ALL){
            query = query.whereEqualTo("orderCurrent", filter)
        }
        query.get()
            .addOnSuccessListener {  snapshot ->
                Log.i("ASD",snapshot.documents.size.toString())
                var count = 0
                if(snapshot.size() == count){
                    loader.visibility = View.GONE
                }
                for(i in 0..(snapshot.size() - 1)){
                    Log.i("ASD", snapshot.documents[i].get("idVendor").toString(),)
                    val tempOrder = Order(
                        snapshot.documents[i].get("idVendor").toString(),
                        snapshot.documents[i].get("idCustomer").toString(),
                        snapshot.documents[i].get("idService").toString(),
                        snapshot.documents[i].get("timeOrder") as Timestamp,
                        snapshot.documents[i].get("timeComing").toString(),
                        snapshot.documents[i].get("orderAddress").toString(),
                        snapshot.documents[i].get("orderCurrent").toString().toInt(),
                        snapshot.documents[i].get("price").toString().toLong(),
                        snapshot.documents[i].get("phoneNumber").toString(),
                        snapshot.documents[i].get("customerName").toString(),
                    )
                    tempOrder.idOrder = snapshot.documents[i].id
                    orderList.add(tempOrder)
                    db.collection("ServiceListings").document(tempOrder.idService).get()
                        .addOnSuccessListener { doc ->
                            val serviceTemp = Service(
                                doc.get("serviceType").toString(),
                                doc.get("serviceName").toString(),
                                doc.get("serviceDescription").toString(),
                                doc.get("servicePrice").toString().toLong(),
                                doc.get("servicePhoneNumber").toString(),
                                doc.get("serviceImage").toString(),
                                doc.get("serviceRating").toString().toFloat(),
                                doc.get("vendorID").toString(),
                                doc.get("vendorName").toString(),
                                //service.data.get("negotiate").toString().toBoolean()
                            )
                            serviceTemp.serviceID = doc.id
                            serviceList.add(serviceTemp)
                            val hashMap = HashMap<String, Any>()
                            hashMap.put("idOrder", tempOrder.idOrder)
                            hashMap.put("idVendor", tempOrder.idVendor)
                            hashMap.put("idCustomer", tempOrder.idCustomer)
                            hashMap.put("idService", tempOrder.idService)
                            hashMap.put("timeOrder", tempOrder.timeOrder)
                            hashMap.put("timeComing", tempOrder.timeComing)
                            hashMap.put("orderAddress", tempOrder.orderAddress)
                            hashMap.put("orderCurrent", tempOrder.orderCurrent)
                            hashMap.put("price", tempOrder.price)
                            hashMap.put("phoneNumber", tempOrder.phoneNumber)
                            hashMap.put("serviceImage", serviceTemp.serviceImage)
                            hashMap.put("serviceName", serviceTemp.serviceName)
                            hashMap.put("vendorName", serviceTemp.vendorName)
                            hashMap.put("serviceType", serviceTemp.serviceType)
                            hashMap.put("serviceDescription", serviceTemp.serviceDescription)
                            hashMap.put("customerName", tempOrder.customerName)
                            inforList.add(hashMap)
                            adapter.notifyItemInserted(i)
                            count += 1
                            if(count == snapshot.size()){
                                loader.visibility = View.GONE
                            }
                        }
                }


            }
    }
    companion object {
        @JvmStatic
        fun newInstance(tabIndex: Int) =
            TabFragmentOrderService().apply {
                arguments = Bundle().apply {
                    putInt("tabIndex", tabIndex)
                }
            }
    }
}