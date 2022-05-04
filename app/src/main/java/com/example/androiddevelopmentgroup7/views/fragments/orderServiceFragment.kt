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