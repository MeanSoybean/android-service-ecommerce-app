package com.example.androiddevelopmentgroup7.views.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.example.androiddevelopmentgroup7.R
import com.example.androiddevelopmentgroup7.utils.OrderTabValue
import com.example.androiddevelopmentgroup7.viewModels.OrderListModel
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator




//class MyOrderAdapter(private var OrderList:ArrayList<Order>): RecyclerView.Adapter<MyOrderAdapter.ViewHolder>(){
//    var onItemClick: ((Order) -> Unit)? = null
//    inner class ViewHolder(listItemView: View): RecyclerView.ViewHolder(listItemView){
//        var serviceName: TextView = listItemView.findViewById(R.id.order_summary_id_tv)
//        var nameVendor:TextView = listItemView.findViewById(R.id.name_fix_value_tv)
//        var timeOrder:TextView = listItemView.findViewById(R.id.order_summary_date_tv)
//        var timeComing:TextView = listItemView.findViewById(R.id.order_time_coming_tv)
//        var price:TextView = listItemView.findViewById(R.id.order_summary_total_amount_tv)
//        var orderCurrent:TextView = listItemView.findViewById(R.id.order_summary_status_value_tv)
//        init {
//            listItemView.setOnClickListener {onItemClick?.invoke(OrderList[adapterPosition]) }
//        }
//    }
//
//    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
//        val context = parent.context
//        val inflater = LayoutInflater.from(context)
//        val serviceView = inflater.inflate(R.layout.layout_order_service, parent, false)
//        return ViewHolder(serviceView)
//    }
//
//    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
//        val contact: Order = OrderList.get(position)
//        holder.serviceName.text = OrderList.get(position).serviceName
//        holder.nameVendor.text = OrderList.get(position).nameVendor
//        holder.timeOrder.text = OrderList.get(position).timeOrder
//        holder.timeComing.text = OrderList.get(position).timeComing
//        holder.price.text = OrderList.get(position).price
//        holder.orderCurrent.text = OrderList.get(position).orderCurrent
//    }
//
//    override fun getItemCount(): Int {
//        return OrderList.size
//    }
//    interface OnClickListener {
//        fun onCardClick(orderId: String)
//    }
//}


//class orderServiceFragment : Fragment() {
//    // TODO: Rename and change types of parameters
//    private val orderListModel : OrderListModel by activityViewModels()
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
////        arguments?.let {
////            param1 = it.getString(ARG_PARAM1)
////            param2 = it.getString(ARG_PARAM2)
////        }
//    }
//
//    override fun onCreateView(
//        inflater: LayoutInflater, container: ViewGroup?,
//        savedInstanceState: Bundle?
//    ): View? {
//        // Inflate the layout for this fragment
//        val rootView = inflater.inflate(R.layout.order_service_fragment, container, false)
//        val recyclerView_services = rootView.findViewById<RecyclerView>(R.id.order_all_orders_recycler_view)
//        val loader = rootView.findViewById<FrameLayout>(R.id.loader_layout)
//        val topAppBar = rootView.findViewById<MaterialToolbar>(R.id.topAppBarNotIcon)
//        topAppBar.title = getString(R.string.mange_order_app_tittle_text)
//
//
//        //0:Customer 1: Vendor
//        if(Utils.typeUser == 1){
//            orderListModel.setOrderList()
//        } else {
//            orderListModel.setOrderListForCustomer()
//        }
//
//        val adapter = MyOrderAdapter(orderListModel.selectedOrderList.value!!)
//        recyclerView_services.adapter = adapter
//        recyclerView_services.layoutManager = LinearLayoutManager(activity)
//
//        orderListModel.selectedOrderList.observe(viewLifecycleOwner, Observer { list ->
//            // Update the list UI
//            Log.i("SIZE", list.size.toString())
//            adapter.notifyDataSetChanged()
//            Log.i("DATASET CHANGE", "data set change")
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
//        return rootView
//    }
//}


class MyOrderFragmentStageAdapter(fragmentManager: FragmentManager, lifecycle: Lifecycle):FragmentStateAdapter(fragmentManager, lifecycle){

    override fun getItemCount(): Int {
        return 5
    }

    override fun createFragment(position: Int): Fragment {
        val bundle = Bundle()
        bundle.putInt("tabIndex", position)
        val tabFragmentOrderService = TabFragmentOrderService()
        tabFragmentOrderService.arguments = bundle
        return tabFragmentOrderService
//        when(position){
//            OrderTabValue.ALL -> { return TabFragmentOrderService()}
//            OrderTabValue.WAITING_ACCEPT -> {return TabFragmentOrderService()}
//            OrderTabValue.ON_GOING -> { return TabFragmentOrderService()}
//            OrderTabValue.COMPLETE -> {return TabFragmentOrderService()}
//            OrderTabValue.CANCEL -> {return TabFragmentOrderService()}
//            else -> return TabFragmentOrderService()
//        }
    }
}





class orderServiceFragment : Fragment() {
    private var navigate: Int? = null
    private lateinit var tabTitle:Array<String>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            navigate = it.getInt("navigate")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val rootView = inflater.inflate(R.layout.order_service_fragment, container, false)
        val topAppBar = rootView.findViewById<MaterialToolbar>(R.id.topAppBarNotIcon)

        topAppBar.title = getString(R.string.mange_order_app_tittle_text)

        val orderViewPager = rootView.findViewById<ViewPager2>(R.id.order_view_pager_2)
        val orderTabLayout = rootView.findViewById<TabLayout>(R.id.tab_service_layout)
        orderViewPager.adapter = MyOrderFragmentStageAdapter(childFragmentManager, lifecycle)

        tabTitle = arrayOf(
            getString(R.string.all_tab_text),
            getString(R.string.accept_tab_text),
            getString(R.string.on_board_tab_text),
            getString(R.string.complete_tab_text),
            getString(R.string.cancel_tab_text),
        )
//        if(navigate!=null){
//            orderTabLayout.selectTab(orderTabLayout.getTabAt(navigate!!))
//        }
        TabLayoutMediator(orderTabLayout, orderViewPager){ tab, position ->
            tab.text = tabTitle[position]
            Log.i("TAB", tab.text.toString())
        }.attach()
        return rootView
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment fragment_change_password.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(navigate: Int) =
            orderServiceFragment().apply {
                arguments = Bundle().apply {
                    putInt("navigate", navigate)
                }
            }
    }
}