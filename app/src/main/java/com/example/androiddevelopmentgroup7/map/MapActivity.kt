package com.example.androiddevelopmentgroup7.map

import android.location.Address
import android.location.Geocoder
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentManager
import com.example.androiddevelopmentgroup7.R
import com.google.android.gms.maps.model.LatLng
import java.util.*


class MapActivity : AppCompatActivity() {
    private var myMapFragment: MapFragment? = null

    override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            setContentView(R.layout.activity_map)

            val fragmentManager: FragmentManager = this.supportFragmentManager
            myMapFragment = fragmentManager.findFragmentById(R.id.fragment_map) as MapFragment?

//        val distanceTv = findViewById<TextView>(R.id.distanceTv)
//        val routeBtn: Button = findViewById<Button>(R.id.routeBtn)
//
//        routeBtn.setOnClickListener() {
//
//        }

    }

    private fun getAddress(latLng: LatLng): String {
        val geocoder = Geocoder(this, Locale.getDefault())
        val addresses: List<Address>?
        val address: Address?
        var addressText = ""

        addresses = geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1)

        if (addresses.isNotEmpty()) {
            address = addresses[0]
            addressText = address.getAddressLine(0)
        } else{
            addressText = "its not appear"
        }
        return addressText
    }
}