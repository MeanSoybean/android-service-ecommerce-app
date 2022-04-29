package com.example.androiddevelopmentgroup7.views.fragments
import android.os.Bundle
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
import com.example.androiddevelopmentgroup7.utils.OrderTabValue
import com.example.androiddevelopmentgroup7.utils.Utils
import com.example.androiddevelopmentgroup7.viewModels.OrderListModel

class MyOrderAdapter(private var OrderList:ArrayList<Order>): RecyclerView.Adapter<MyOrderAdapter.ViewHolder>(){
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
        val contact: Order = OrderList.get(position)
        holder.serviceName.text = OrderList.get(position).serviceName
        holder.nameVendor.text = OrderList.get(position).nameVendor
        holder.timeOrder.text = OrderList.get(position).timeOrder
        holder.timeComing.text = OrderList.get(position).timeComing
        holder.price.text = OrderList.get(position).price
        holder.orderCurrent.text = OrderList.get(position).orderCurrent
    }

    override fun getItemCount(): Int {
        return OrderList.size
    }
    interface OnClickListener {
        fun onCardClick(orderId: String)
    }
}


class TabFragmentOrderService() : Fragment() {
    private val orderListModel : OrderListModel by activityViewModels()
    private var tabIndex: Int? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            tabIndex = it.getInt("tabIndex")
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
                OrderTabValue.ALL -> orderListModel.setOrderList()
//                OrderTabValue.WAITING_ACCEPT -> orderListModel.setOrderListVendorWaiting()
//                OrderTabValue.ON_GOING -> orderListModel.setOrderListVendorOnGoing()
//                OrderTabValue.COMPLETE -> orderListModel.setOrderListVendorComplete()
//                OrderTabValue.CANCEL -> orderListModel.setOrderListVendorCancel()
            }
        } else {
            orderListModel.setOrderListForCustomer()
        }


        val adapter = MyOrderAdapter(orderListModel.selectedOrderList.value!!)
        recyclerView_services.adapter = adapter
        recyclerView_services.layoutManager = LinearLayoutManager(activity)

        orderListModel.selectedOrderList.observe(viewLifecycleOwner, Observer { list ->
            // Update the list UI
            //Log.i("SIZE", list.size.toString())
            adapter.notifyDataSetChanged()
            //Log.i("DATASET CHANGE", "data set change")
        })
        orderListModel.orderStatus.observe(viewLifecycleOwner, Observer { status ->
            // Update the list UI
            when(status){
                Utils.LOADER_LOADING -> {
                    loader.visibility = View.VISIBLE
                }
                Utils.LOADER_HIDE -> {
                    loader.visibility = View.GONE
                }
            }
        })
        adapter.onItemClick = { contact ->

            val temp = contact.serviceName +'%' + contact.timeComing+'%'+contact.orderAddress +'%' +contact.orderCurrent +'%'+ contact.price
            findNavController().navigate(
                R.id.action_orderServiceVendorFragment_to_orderDetailsFragment,
                bundleOf("OrderDetails" to temp)
            )
        }
        return rootView
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