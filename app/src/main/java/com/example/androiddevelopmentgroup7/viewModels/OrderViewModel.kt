package com.example.androiddevelopmentgroup7.viewModels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.androiddevelopmentgroup7.MainActivity
import com.example.androiddevelopmentgroup7.Utils
import com.example.androiddevelopmentgroup7.dataModels.Order
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase


class OrderListModel :  ViewModel(){
    private val db = Firebase.firestore
    private val OrderList: MutableLiveData<ArrayList<Order>> = MutableLiveData<ArrayList<Order>>()

    val orderStatus: MutableLiveData<String> = MutableLiveData("loading")

    val selectedOrderList: LiveData<ArrayList<Order>> get() = OrderList


    fun setOrderList(){
        val orderListTemp = ArrayList<Order>()
        OrderList.value = orderListTemp
        orderStatus.value = Utils.LOADER_LOADING
        //Log.i("VENDROID", Utils.vendor.id)
        db.collection("OrderListing")
            .whereEqualTo("idVendor", Utils.vendor.id)
            .get()
            .addOnSuccessListener { orders ->
                Log.i("NUMBER ORDER", orders.documents.size.toString())
                for(order in orders){
                    orderListTemp.add(
                        Order(
                            order.data.get("idOrder").toString(),
                            order.data.get("idVendor").toString(),
                            order.data.get("idCustomer").toString(),
                            order.data.get("serviceName").toString(),
                            order.data.get("nameVendor").toString(),
                            order.data.get("timeOrder").toString(),
                            order.data.get("timeComing").toString(),
                            order.data.get("orderAddress").toString(),
                            order.data.get("orderCurrent").toString(),
                            order.data.get("price").toString(),
                            order.data.get("timeOrder").toString(),
                            order.data.get("serviceImage").toString(),
                        )
                    )

                }
                OrderList.value = orderListTemp
                orderStatus.value = Utils.LOADER_HIDE
            }
    }


    fun setOrderListForCustomer(){
        val orderListTemp = ArrayList<Order>()
        OrderList.value = orderListTemp
        orderStatus.value = Utils.LOADER_LOADING
        //Log.i("VENDROID", Utils.vendor.id)
        db.collection("OrderListing")
            .whereEqualTo("idCustomer", Utils.customer.id)
            .get()
            .addOnSuccessListener { orders ->
                Log.i("NUMBER ORDER", orders.documents.size.toString())
                for(order in orders){
                    orderListTemp.add(
                        Order(
                            order.data.get("idOrder").toString(),
                            order.data.get("idVendor").toString(),
                            order.data.get("idCustomer").toString(),
                            order.data.get("serviceName").toString(),
                            order.data.get("nameVendor").toString(),
                            order.data.get("timeOrder").toString(),
                            order.data.get("timeComing").toString(),
                            order.data.get("orderAddress").toString(),
                            order.data.get("orderCurrent").toString(),
                            order.data.get("price").toString(),
                            order.data.get("timeOrder").toString(),
                            order.data.get("serviceImage").toString(),
                        )
                    )

                }
                OrderList.value = orderListTemp
                orderStatus.value = Utils.LOADER_HIDE
            }
    }
    fun addOrderToList(service: Order){
        OrderList.value?.add(service)
        Log.i("AAA", OrderList.value?.size.toString())
    }
}

