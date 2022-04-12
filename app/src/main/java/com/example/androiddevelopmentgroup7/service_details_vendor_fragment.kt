package com.example.androiddevelopmentgroup7

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Button
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.androiddevelopmentgroup7.R.id
import com.example.androiddevelopmentgroup7.dataModels.Service
import com.example.androiddevelopmentgroup7.viewModels.ServiceViewModel
import com.google.android.material.appbar.MaterialToolbar

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
//private const val ARG_PARAM1 = "param1"
//private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [service_details_vendor_fragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class service_details_vendor_fragment : Fragment(){
//    // TODO: Rename and change types of parameters
//    private var param1: String? = null
//    private var param2: String? = null
    private val serviceViewModel: ServiceViewModel by activityViewModels()
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
        var view =  inflater.inflate(R.layout.service_details_vendor_fragment, container, false)
        var toolbar = view.findViewById<MaterialToolbar>(R.id.topAppBar)
        val addBtn = view.findViewById<Button>(R.id.add_service_save_btn)
        addBtn.setOnClickListener{
            processingData(view)
        }
        var serviceTextView = view.findViewById<AutoCompleteTextView>(R.id.vendor_type_service_edit_text)
        serviceTextView.setText("Sửa ống nước",false)
        var serviceType = ArrayList<String>().also{
            it.add("Sửa máy may")
            it.add("Sửa bếp ga")
            it.add("Sửa nồi cơm điện")
            it.add("Sửa quạt gió")
            it.add("Sửa các loại mô tơ đây")
        }
        val adapter = ArrayAdapter<String>(requireActivity(), R.layout.service_list_item, serviceType)
        serviceTextView.setAdapter(adapter)

        toolbar.setNavigationOnClickListener {
            findNavController().popBackStack()
        }
        return view
    }


    fun processingData(view: View){
        Log.i("AAA", "btn click insert service")
        val service = Service(
            "Sửa đồ gia dụng",
            "Sửa tivi tận nơi",
            "Nhận sửa các loại tivi công nghệ cao, an toàn, nhanh chống",
            "Giá cả thương lượng","123",
            "")
        serviceViewModel.addServiceToList(service)
        findNavController().navigate(R.id.action_service_details_vendor_fragment_to_home_vendor_fragment)
    }

//    companion object {
//        /**
//         * Use this factory method to create a new instance of
//         * this fragment using the provided parameters.
//         *
//         * @param param1 Parameter 1.
//         * @param param2 Parameter 2.
//         * @return A new instance of fragment service_details_vendor_fragment.
//         */
//        // TODO: Rename and change types and number of parameters
//        @JvmStatic
//        fun newInstance(param1: String, param2: String) =
//            service_details_vendor_fragment().apply {
//                arguments = Bundle().apply {
//                    putString(ARG_PARAM1, param1)
//                    putString(ARG_PARAM2, param2)
//                }
//            }
//    }
}