package com.example.androiddevelopmentgroup7

import com.example.androiddevelopmentgroup7.dataModels.UserCustomer
import com.example.androiddevelopmentgroup7.dataModels.UserVendor

class Utils{
    companion object{
        lateinit var customer:UserCustomer
        lateinit var vendor: UserVendor

        var typeUser:Int = 0 // 0: customer, 1: vendor

        val LOADER_LOADING = "loading"
        val LOADER_HIDE = "hide_loader"


    }
}

