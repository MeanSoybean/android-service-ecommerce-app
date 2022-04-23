package com.example.androiddevelopmentgroup7.test

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.androiddevelopmentgroup7.R
import com.example.androiddevelopmentgroup7.map.MapActivity
import com.example.androiddevelopmentgroup7.profile.ProfileManager
import com.google.android.gms.maps.model.LatLng
import java.io.IOException
import java.net.HttpURLConnection
import java.net.MalformedURLException
import java.net.URL


class TestActivity : AppCompatActivity(), LocationListener {
    private lateinit var locationManager: LocationManager
    private lateinit var latlng: LatLng
    private val locationPermissionCode = 2

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tét)

        val profile_btn: Button = findViewById<Button>(R.id.profile_btn)
        val map_btn: Button = findViewById<Button>(R.id.map_btn)
        val location_btn: Button = findViewById<Button>(R.id.location_btn)

        profile_btn.setOnClickListener() {
            val intent = Intent(this, ProfileManager::class.java)
            startActivity(intent)
        }

        map_btn.setOnClickListener() {
            val intent = Intent(this, MapActivity::class.java)
            startActivity(intent)
        }

        location_btn.setOnClickListener() {
            getLocation()
        }

        var i4: String? = null
        Thread {
            i4 = getFinalRedirectedUrl("https://www.google.com/maps/place/dai+hoc+kinh+te/").toString()
            Log.i("URL-RESPONSE",  i4.toString())
        }.start()


    }

    fun getRedirectUrl(url: String?): String? {
        var urlTmp: URL? = null
        var redUrl: String? = null
        var connection: HttpURLConnection? = null
        try {
            urlTmp = URL(url)
        } catch (e1: MalformedURLException) {
            e1.printStackTrace()
        }
        try {
            connection = urlTmp!!.openConnection() as HttpURLConnection
        } catch (e: IOException) {
            e.printStackTrace()
        }
        try {
            connection!!.responseCode
        } catch (e: IOException) {
            e.printStackTrace()
        }
        redUrl = connection!!.url.toString()
        connection.disconnect()
        return redUrl
    }

    fun getFinalRedirectedUrl(url: String): String? {
        var connection: HttpURLConnection
        var finalUrl = url
        try {
            do {
                connection = URL(finalUrl)
                    .openConnection() as HttpURLConnection
                connection.instanceFollowRedirects = false
                connection.useCaches = false
                connection.requestMethod = "GET"
                connection.connect()
                val responseCode = connection.responseCode
                if (responseCode >= 300 && responseCode < 400) {
                    val redirectedUrl = connection.getHeaderField("Location") ?: break
                    finalUrl = redirectedUrl
                    println("redirected url: $finalUrl")
                } else break
            } while (connection.responseCode != HttpURLConnection.HTTP_OK)
            connection.disconnect()
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return finalUrl
    }

    private fun getLocation() {
        try {
            locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
            if ((ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED)) {
                ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), locationPermissionCode)
            }

            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000, 5f, this)
        } catch (e: SecurityException) {
            e.printStackTrace()
        }
    }

    override fun onLocationChanged(location: Location) {
        latlng = LatLng(location.latitude, location.longitude)
//        Log.i(ContentValues.TAG, "Location: " + latlng.toString())
        Toast.makeText(this, "Location: " + latlng.toString(), Toast.LENGTH_SHORT).show()
    }

    @SuppressLint("MissingPermission")
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == locationPermissionCode) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000, 5f, this)
                Toast.makeText(this, "Permission Granted", Toast.LENGTH_SHORT).show()
            }
            else {
                Toast.makeText(this, "Permission Denied", Toast.LENGTH_SHORT).show()
            }
        }
    }
}