package com.example.androiddevelopmentgroup7.viewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.androiddevelopmentgroup7.models.UserCustomer


class UserViewModel  : ViewModel(){
    private val userRoot: MutableLiveData<ArrayList<UserCustomer>> = MutableLiveData<ArrayList<UserCustomer>>()

    val selectedServiceList: LiveData<ArrayList<UserCustomer>> get() = userRoot

    fun setServiceList(serviceListArg: ArrayList<UserCustomer>){
        userRoot.value = serviceListArg}
}