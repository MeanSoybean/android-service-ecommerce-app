package com.example.androidgr7.viewModels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.androidgr7.dataModels.Order
import java.util.*
import kotlin.collections.ArrayList


class OrderListModel :  ViewModel(){
    private val OrderList: MutableLiveData<ArrayList<Order>> = MutableLiveData<ArrayList<Order>>()

    val selectedOrderList: LiveData<ArrayList<Order>> get() = OrderList

    fun setServiceList(serviceListArg: ArrayList<Order>){
//        serviceList.value = serviceListArg
/*        val order = Order(
            "1",
            "1",
            "1",
        "Sửa chữa điện tại nhà",
        "Huỳnh Văn A",
            "Đặt đơn lúc 3.00PM 20/04/2022",
            "22/04/2022",
        "300 CTM8, HCM",
        "Đang chờ xác nhận",
            "Thương lượng tận nơi",
       "")
        val order1 = Order(
            "2",
            "2",
            "1",
            "Sửa chữa ống nước",
            "Trần C","Đặt đơn lúc 3.00PM 20/04/2022",
            "22/04/2022",
            "300 CTM8, HCM",
            "Hoàn thành","Thương lượng tận nơi",
            "")
        val order2 = Order(
            "3",
            "3",
            "1",
            "Sửa chữa đồ gia dụng",
            "Nguyễn D","Đặt đơn lúc 3.00PM 20/04/2022",
            "22/04/2022",
            "300 CTM8, HCM",
            "Đang chờ xác nhận","Thương lượng tận nơi",
            "")
        val temp = ArrayList<Order>()
        temp.add(order)
        temp.add(order1)
        temp.add(order2)
        OrderList.value = temp*/
    }

    fun addOrderToList(service: Order){
        OrderList.value?.add(service)
        Log.i("AAA", OrderList.value?.size.toString())
    }
}

