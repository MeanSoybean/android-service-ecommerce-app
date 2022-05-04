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
import com.google.firebase.firestore.Query
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
    private var noneItemTextView:TextView? = null
    private var recyclerView_services:RecyclerView? = null
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
        recyclerView_services = rootView.findViewById(R.id.orders_list_recycler_view)
        val loader = rootView.findViewById<FrameLayout>(R.id.loader_layout)
        noneItemTextView = rootView.findViewById(R.id.none_service_textview)
        noneItemTextView?.visibility = View.GONE
        //0:Customer 1: Vendor
        if(Utils.typeUser == 1){
            when(tabIndex){
                OrderTabValue.ALL -> createListItem("idVendor", Utils.vendor.accountID, OrderTabValue.ALL,recyclerView_services!!, loader)
                OrderTabValue.WAITING_ACCEPT -> createListItem("idVendor", Utils.vendor.accountID, OrderTabValue.WAITING_ACCEPT,recyclerView_services!!, loader)
                OrderTabValue.ON_GOING -> createListItem("idVendor", Utils.vendor.accountID, OrderTabValue.ON_GOING,recyclerView_services!!, loader)
                OrderTabValue.COMPLETE -> createListItem("idVendor", Utils.vendor.accountID, OrderTabValue.COMPLETE,recyclerView_services!!, loader)
                OrderTabValue.CANCEL -> createListItem("idVendor", Utils.vendor.accountID, OrderTabValue.CANCEL,recyclerView_services!!, loader)
            }
        } else {
            when(tabIndex){
                OrderTabValue.ALL -> createListItem("idCustomer", Utils.customer.accountID, OrderTabValue.ALL,recyclerView_services!!, loader)
                OrderTabValue.WAITING_ACCEPT -> createListItem("idCustomer", Utils.customer.accountID, OrderTabValue.WAITING_ACCEPT,recyclerView_services!!, loader)
                OrderTabValue.ON_GOING -> createListItem("idCustomer", Utils.customer.accountID, OrderTabValue.ON_GOING,recyclerView_services!!, loader)
                OrderTabValue.COMPLETE -> createListItem("idCustomer", Utils.customer.accountID, OrderTabValue.COMPLETE,recyclerView_services!!, loader)
                OrderTabValue.CANCEL -> createListItem("idCustomer", Utils.customer.accountID, OrderTabValue.CANCEL,recyclerView_services!!, loader)
            }
        }


        return rootView
    }

    private fun itemClickApdater(adapter: MyOrderAdapter){
        adapter.onItemClick = { hasmap ->
            val bundle = Bundle()
            Log.i("IDORDER", hasmap.get("idOrder").toString())
            bundle.putString("idOrder", hasmap.get("idOrder").toString())
            findNavController().navigate(R.id.action_orderServiceVendorFragment_to_orderDetailsFragment, bundle)
        }
    }

    private fun createListItem(typeUserID:String, userID:String, filter:Int, recyclerView: RecyclerView, loader:FrameLayout){
        val orderList = ArrayList<Order>()
        val inforList = ArrayList<HashMap<String, Any>>()
        val adapter = MyOrderAdapter(requireContext(), inforList)
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(activity)
        itemClickApdater(adapter)
        var query = db.collection("OrderListing").whereEqualTo(typeUserID, userID)
        if(filter != OrderTabValue.ALL){
            query = query.whereEqualTo("orderCurrent", filter)
        }
        query.orderBy("timeOrder", Query.Direction.DESCENDING).get()
            .addOnSuccessListener {  snapshot ->
                val idServiceList = ArrayList<String>()
                if(snapshot.size() == 0){
                    loader.visibility = View.GONE
                    noneItemTextView?.visibility = View.VISIBLE
                    recyclerView_services?.visibility = View.GONE
                }

                for(i in 0..(snapshot.size() - 1)){

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
                    idServiceList.add(tempOrder.idService)
                }
                if(idServiceList.size > 0) {
                    db.collection("ServiceListings").whereIn(FieldPath.documentId(), idServiceList).get()
                        .addOnSuccessListener { snapshot ->
                            var count = 0
                            for(order in orderList){
                                for(service in snapshot){
                                    if(order.idService.equals(service.id)){
                                        val hashMap = HashMap<String, Any>()
                                        hashMap.put("idOrder", order.idOrder)
                                        hashMap.put("idVendor", order.idVendor)
                                        hashMap.put("idCustomer", order.idCustomer)
                                        hashMap.put("idService", order.idService)
                                        hashMap.put("timeOrder", order.timeOrder)
                                        hashMap.put("timeComing", order.timeComing)
                                        hashMap.put("orderAddress", order.orderAddress)
                                        hashMap.put("orderCurrent", order.orderCurrent)
                                        hashMap.put("price", order.price)
                                        hashMap.put("phoneNumber", order.phoneNumber)
                                        hashMap.put("serviceImage", service.data.get("serviceImage")!!)
                                        hashMap.put("serviceName", service.data.get("serviceName")!!)
                                        hashMap.put("vendorName",service.data.get("vendorName")!!)
                                        hashMap.put("serviceType", service.data.get("serviceType")!!)
                                        hashMap.put("serviceDescription", service.data.get("serviceDescription")!!)
                                        hashMap.put("customerName", order.customerName)
                                        Log.i("ASDD", order.idService)
                                        Log.i("ASDD", service.id)
                                        inforList.add(hashMap)
                                        adapter.notifyItemInserted(count)
                                    }
                                }
                                count++
                                if(count == snapshot.size()){
                                    loader.visibility = View.GONE
                                }

                            }

                        }
                }
            }
            .addOnFailureListener { exception ->
                Log.i("ERROR", "Error getting documents.", exception)
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