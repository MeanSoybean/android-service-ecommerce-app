package com.example.androiddevelopmentgroup7.views.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.example.androiddevelopmentgroup7.R
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [fragment_communication.newInstance] factory method to
 * create an instance of this fragment.
 */



class MyCommunicationFragmentStageAdapter(fragmentManager: FragmentManager, lifecycle: Lifecycle):
    FragmentStateAdapter(fragmentManager, lifecycle){

    override fun getItemCount(): Int {
        return 2
    }

    override fun createFragment(position: Int): Fragment {
//        val bundle = Bundle()
//        bundle.putInt("tabIndex", position)
//        val tabFragmentOrderService = TabFragmentOrderService()
//        tabFragmentOrderService.arguments = bundle
//        return tabFragmentOrderService
        when(position){
            0 -> { return fragment_notification()}
            1 -> {return  fragment_chat()}
            else -> return fragment_chat()
        }
    }
}
class fragment_communication : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private lateinit var tabTitle:Array<String>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val rootView =  inflater.inflate(R.layout.fragment_communication, container, false)
        val topAppBar = rootView.findViewById<MaterialToolbar>(R.id.topAppBarNotIcon)

        topAppBar.title = getString(R.string.commnication_app_tittle_text)

        val communicationViewPager = rootView.findViewById<ViewPager2>(R.id.communication_view_pager_2)
        val communicationTabLayout = rootView.findViewById<TabLayout>(R.id.tab_communication_layout)
        communicationViewPager.adapter = MyCommunicationFragmentStageAdapter(childFragmentManager, lifecycle)

        tabTitle = arrayOf(
            getString(R.string.notification_text_label),
            getString(R.string.message_text_label),
        )
        TabLayoutMediator(communicationTabLayout, communicationViewPager){ tab, position ->
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
         * @return A new instance of fragment fragment_communication.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            fragment_communication().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}