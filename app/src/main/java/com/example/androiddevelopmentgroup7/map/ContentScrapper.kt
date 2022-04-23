package com.example.androiddevelopmentgroup7.map

import androidx.appcompat.app.AppCompatActivity
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.MalformedURLException
import java.net.URL

object ContentScrapper {
    fun getHTMLData(activity: AppCompatActivity,url: String, scrapListener: ScrapListener) {
        Thread(Runnable {
            val google: URL?
            val `in`: BufferedReader?
            var input: String?
            val stringBuffer = StringBuffer()

            try {
                google = URL(url)
                `in` = BufferedReader(InputStreamReader(google.openStream()))
                while (true) {
                    if (`in`.readLine().also { input = it } == null)
                        break
                    stringBuffer.append(input)
                }
                `in`.close()

                activity.runOnUiThread {
                    scrapListener.onResponse(stringBuffer.toString())
                }
            } catch (e: MalformedURLException) {
                e.printStackTrace()
                activity.runOnUiThread {
                    scrapListener.onResponse(null)
                }
            }
        }).start()

    }

    interface ScrapListener {
        fun onResponse(html: String?)
    }
}