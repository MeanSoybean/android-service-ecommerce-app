package com.example.androiddevelopmentgroup7.utils

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.AsyncTask
import android.util.Log
import android.widget.ImageView
import com.example.androiddevelopmentgroup7.models.UserCustomer
import com.example.androiddevelopmentgroup7.models.UserVendor
class DownloadImageFromInternet(var imageView: ImageView) : AsyncTask<String, Void, Bitmap?>() {
    override fun doInBackground(vararg urls: String): Bitmap? {
        val imageURL = urls[0]
        var image: Bitmap? = null
        try {
            val `in` = java.net.URL(imageURL).openStream()
            image = BitmapFactory.decodeStream(`in`)
        }
        catch (e: Exception) {
            Log.e("Error Message", e.message.toString())
            e.printStackTrace()
        }
        return image
    }
    override fun onPostExecute(result: Bitmap?) {
        imageView.setImageBitmap(result)
    }
}
class Utils{
    companion object{
        lateinit var customer:UserCustomer
        lateinit var vendor: UserVendor

        var typeUser:Int = 0 // 0: customer, 1: vendor

        val LOADER_LOADING = "loading"
        val LOADER_HIDE = "hide_loader"


    }
}

class OrderTabValue{
    companion object{
        val ALL:Int = 0
        val WAITING_ACCEPT:Int = 1
        val ON_GOING:Int = 2
        val COMPLETE:Int = 3
        val CANCEL:Int = 4
    }
}


