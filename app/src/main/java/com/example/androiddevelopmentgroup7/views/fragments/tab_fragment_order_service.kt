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
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.androiddevelopmentgroup7.R
import com.example.androiddevelopmentgroup7.models.Order
import com.example.androiddevelopmentgroup7.models.Service
import com.example.androiddevelopmentgroup7.utils.OrderTabValue
import com.example.androiddevelopmentgroup7.utils.Utils
import com.example.androiddevelopmentgroup7.viewModels.OrderListModel
import com.example.androiddevelopmentgroup7.viewModels.ServiceViewModel
import com.google.firebase.firestore.FieldPath
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.withContext

class MyOrderAdapter(var context: Context, private var OrderList:ArrayList<Order>, private var serviceList:ArrayList<Service>): RecyclerView.Adapter<MyOrderAdapter.ViewHolder>(){
    var onItemClick: ((Order) -> Unit)? = null
    inner class ViewHolder(listItemView: View): RecyclerView.ViewHolder(listItemView){
        var serviceName: TextView = listItemView.findViewById(R.id.order_summary_id_tv)
        var nameVendor: TextView = listItemView.findViewById(R.id.name_fix_value_tv)
        var timeOrder: TextView = listItemView.findViewById(R.id.order_summary_date_tv)
        var timeComing: TextView = listItemView.findViewById(R.id.order_time_coming_tv)
        var price: TextView = listItemView.findViewById(R.id.order_summary_total_amount_tv)
        var orderCurrent: TextView = listItemView.findViewById(R.id.order_summary_status_value_tv)
        init {
            listItemView.setOnClickListener {onItemClick?.invoke(OrderList[adapterPosition]) }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val context = parent.context
        val inflater = LayoutInflater.from(context)
        val serviceView = inflater.inflate(R.layout.layout_order_service, parent, false)
        return ViewHolder(serviceView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.serviceName.text = serviceList.get(position).serviceName
        holder.nameVendor.text = serviceList.get(position).vendorName
        holder.timeOrder.text = OrderList.get(position).timeOrder
        holder.timeComing.text = OrderList.get(position).timeComing
        holder.price.text = OrderList.get(position).price.toString()
        when(OrderList.get(position).orderCurrent){
            OrderTabValue.WAITING_ACCEPT -> holder.orderCurrent.setText(context.getString(R.string.accept_tab_text))
            OrderTabValue.ON_GOING -> holder.orderCurrent.setText(context.getString(R.string.on_board_tab_text))
            OrderTabValue.COMPLETE -> holder.orderCurrent.setText(context.getString(R.string.complete_tab_text))
            OrderTabValue.CANCEL -> holder.orderCurrent.setText(context.getString(R.string.cancel_tab_text))
        }
    }

    override fun getItemCount(): Int {
        return OrderList.size
    }
    interface OnClickListener {
        fun onCardClick(orderId: String)
    }
}


class TabFragmentOrderService() : Fragment() {
    val db = Firebase.firestore
//    private val orderListModel : OrderListModel by activityViewModels()
//    private val serviceListModel : ServiceViewModel by activityViewModels()
    private var orderList = ArrayList<Order>()
    private var serviceList = ArrayList<Service>()
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




//        val adapter = MyOrderAdapter(requireContext(),orderListModel.selectedOrderList.value!!, )
//        recyclerView_services.adapter = adapter
//        recyclerView_services.layoutManager = LinearLayoutManager(activity)

//        orderListModel.selectedOrderList.observe(viewLifecycleOwner, Observer { list ->
//            // Update the list UI
//            //Log.i("SIZE", list.size.toString())
//            adapter.notifyDataSetChanged()
//            //Log.i("DATASET CHANGE", "data set change")
//        })
//        orderListModel.orderStatus.observe(viewLifecycleOwner, Observer { status ->
//            // Update the list UI
//            when(status){
//                Utils.LOADER_LOADING -> {
//                    loader.visibility = View.VISIBLE
//                }
//                Utils.LOADER_HIDE -> {
//                    loader.visibility = View.GONE
//                }
//            }
//        })
//        adapter.onItemClick = { contact ->
//
//            val temp = contact.serviceName +'%' + contact.timeComing+'%'+contact.orderAddress +'%' +contact.orderCurrent +'%'+ contact.price
//            findNavController().navigate(
//                R.id.action_orderServiceVendorFragment_to_orderDetailsFragment,
//                bundleOf("OrderDetails" to temp)
//            )
//        }
        return rootView
    }

    private fun createListItem(typeUserID:String, userID:String, filter:Int, recyclerView: RecyclerView, loader:FrameLayout){

        var query = db.collection("OrderListing").whereEqualTo(typeUserID, userID)
        if(filter != OrderTabValue.ALL){
            query = query.whereEqualTo("orderCurrent", filter)
        }
        query.get()
            .addOnSuccessListener {  snapshot ->
                Log.i("ASD",snapshot.documents.size.toString())
                val idServiceList = ArrayList<String>()
                for(order in snapshot){
                    Log.i("ASD", "Nhan Order")
                    val tempOrder = Order(
                        order.data.get("idVendor").toString(),
                        order.data.get("idCustomer").toString(),
                        order.data.get("idService").toString(),
                        order.data.get("timeOrder").toString(),
                        order.data.get("timeComing").toString(),
                        order.data.get("orderAddress").toString(),
                        order.data.get("orderCurrent").toString().toInt(),
                        order.data.get("price").toString().toLong(),
                        order.data.get("phoneNumber").toString(),
                    )
                    tempOrder.idOrder = order.id
                    idServiceList.add(order.data.get("idService").toString())
                    orderList.add(tempOrder)
                }
                Log.i("ASD", "NHAN ID" + idServiceList.size.toString())
                db.collection("ServiceListings")
                    .whereIn(FieldPath.documentId(), idServiceList)
                    .get()
                    .addOnSuccessListener { services ->

                        Log.i("ASD", "NHAN SERVICE: " + services.size().toString())
                        for (service in services) {
                            val serviceTemp = Service(
                                service.data.get("serviceType").toString(),
                                service.data.get("serviceName").toString(),
                                service.data.get("serviceDescription").toString(),
                                service.data.get("servicePrice").toString().toLong(),
                                service.data.get("servicePhoneNumber").toString(),
                                service.data.get("serviceImage").toString(),
                                service.data.get("serviceRating").toString().toFloat(),
                                service.data.get("vendorID").toString(),
                                service.data.get("vendorName").toString(),
                                service.data.get("negotiate").toString().toBoolean()
                            )
                            serviceTemp.serviceID = service.id
                            serviceList.add(serviceTemp)
                        }
                        Log.i("ASD", "TAO RECYCELVOEW")
                        val adapter = MyOrderAdapter(requireContext(), orderList, serviceList)
                        recyclerView.adapter = adapter
                        recyclerView.layoutManager = LinearLayoutManager(activity)
                        loader.visibility = View.GONE
                    }
                    .addOnFailureListener { exception ->
                        Log.i("ERROR", "Error getting documents.", exception)
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