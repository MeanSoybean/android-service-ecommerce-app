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

class Utils {
    companion object {
        lateinit var customer: UserCustomer
        lateinit var vendor: UserVendor

        var typeUser: Int = 0 // 0: customer, 1: vendor

        const val LOADER_LOADING = "loading"
        const val LOADER_HIDE = "hide_loader"


    }
}

class OrderTabValue {
    companion object {
        const val ALL:Int = 0
        const val WAITING_ACCEPT:Int = 1
        const val ON_GOING:Int = 2
        const val COMPLETE:Int = 3
        const val CANCEL:Int = 4
    }
}

class SortingType {
    companion object {
        const val ASCENDING:Int = 1
        const val DESCENDING:Int = 0
    }
}
class SortingAccording {
    companion object {
        const val RATING:Int = 1
        const val PRICE:Int = 0
    }
}


