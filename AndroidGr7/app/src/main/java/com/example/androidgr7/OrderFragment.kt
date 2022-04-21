package com.example.androidgr7

import android.content.Intent
import androidx.lifecycle.Observer
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
import androidx.core.os.bundleOf
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.androidgr7.R
import com.example.androidgr7.dataModels.Order
import com.example.androidgr7.dataModels.Service
import com.example.androidgr7.viewModels.ServiceViewModel
import com.example.androidgr7.viewModels.OrderListModel


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [OrderFragment.newInstance] factory method to
 * create an instance of this fragment.
 */



class MyOrderAdapter(private var OrderList:ArrayList<Order>): RecyclerView.Adapter<MyOrderAdapter.ViewHolder>(){
    var onItemClick: ((Order) -> Unit)? = null
    inner class ViewHolder(listItemView: View): RecyclerView.ViewHolder(listItemView){
        var serviceName: TextView = listItemView.findViewById(R.id.order_summary_id_tv)
        var nameVendor:TextView = listItemView.findViewById(R.id.name_fix_value_tv)
        var timeOrder:TextView = listItemView.findViewById(R.id.order_summary_date_tv)
        var timeComing:TextView = listItemView.findViewById(R.id.order_time_coming_tv)
        var price:TextView = listItemView.findViewById(R.id.order_summary_total_amount_tv)
        var orderCurrent:TextView = listItemView.findViewById(R.id.order_summary_status_value_tv)
        init {
            listItemView.setOnClickListener {onItemClick?.invoke(OrderList[adapterPosition]) }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val context = parent.context
        val inflater = LayoutInflater.from(context)
        val serviceView = inflater.inflate(R.layout.layout_order_summary_card, parent, false)
        return ViewHolder(serviceView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val contact: Order =OrderList.get(position)
        holder.serviceName.text = OrderList.get(position).serviceName
        holder.nameVendor.text = OrderList.get(position).nameVendor
        holder.timeOrder.text = OrderList.get(position).timeOrder
        holder.timeComing.text = OrderList.get(position).timeComing
        holder.price.text = OrderList.get(position).price.toString()
        holder.orderCurrent.text = OrderList.get(position).orderCurrent


    }

    override fun getItemCount(): Int {
        return OrderList.size
    }
    interface OnClickListener {
        fun onCardClick(orderId: String)
    }
}






class OrderFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private val orderListModel : OrderListModel by activityViewModels()
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
        val rootView = inflater.inflate(R.layout.fragment_order, container, false)
        val recyclerView_services = rootView.findViewById<RecyclerView>(R.id.order_all_orders_recycler_view)


        val adapter = MyOrderAdapter(orderListModel.selectedOrderList.value!!)
        recyclerView_services.adapter = adapter
        recyclerView_services.layoutManager = LinearLayoutManager(activity)


        orderListModel.selectedOrderList.observe(viewLifecycleOwner, Observer { list ->
            // Update the list UI
            adapter.notifyItemInserted(list.size - 1)
        })

        adapter.onItemClick = { contact ->

            val temp = contact.serviceName +'%' + contact.timeComing+'%'+contact.orderAddress +'%' +contact.orderCurrent +'%'+ contact.price
            findNavController().navigate(
                R.id.action_OrderFragment_to_DetailsFragment,
                bundleOf("OrderDetails" to temp)
            )
        }
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