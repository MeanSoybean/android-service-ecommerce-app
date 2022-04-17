package com.example.androiddevelopmentgroup7.map

import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.util.Log
import android.widget.TextView
import com.google.android.gms.common.internal.ImagesContract.URL
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.GoogleMap.OnMapClickListener
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.maps.android.SphericalUtil
import java.util.*

class MapFragment : SupportMapFragment(), OnMapReadyCallback {
    private var googleMap: GoogleMap? = null

    override fun onMapReady(gmap: GoogleMap) {
        googleMap = gmap
        // Set default position
        val vietnam = LatLng(14.0583, 108.2772)
        val cambodia = LatLng(11.562108, 104.888535)

        markerFrom(vietnam, "Viet Nam")
        markerTo(cambodia, "Cambodia")

        googleMap!!.moveCamera(CameraUpdateFactory.newLatLng(vietnam))
        googleMap!!.setOnMapClickListener { latLng ->
            val markerOptions = MarkerOptions()
            markerOptions.position(latLng)
            markerOptions.title(latLng.latitude.toString() + " : " + latLng.longitude)
            // Clear previously click position.
            googleMap!!.clear()
            // Zoom the Marker
            googleMap!!.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 10f))
            // Add Marker on Map
            googleMap!!.addMarker(markerOptions)

            markerTo(cambodia, "Cambodia")
        }
//        Log.d("Distance gg", distance(vietnam, cambodia).toString())
    }

    fun markerFrom(location: LatLng, address: String) {
        googleMap!!.addMarker(MarkerOptions()
            .position(location)
            .title("Marker in " + address))
    }

    fun markerTo(location: LatLng, address: String) {
        googleMap!!.addMarker(MarkerOptions()
            .position(location)
            .title("Marker in " + address)
            .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)))
    }

    fun distance(from: LatLng, to: LatLng): Double {
        return SphericalUtil.computeDistanceBetween(from, to)
    }

    init {
        getMapAsync(this)
    }
}