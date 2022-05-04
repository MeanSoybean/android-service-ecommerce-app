package com.example.androiddevelopmentgroup7.views.fragments

import android.annotation.SuppressLint
import android.content.ContentValues
import android.util.Log
import com.example.androiddevelopmentgroup7.models.Service
import com.google.android.gms.maps.*
import com.google.android.gms.maps.model.*
import com.google.maps.android.SphericalUtil
import java.text.DecimalFormat


class CustomMapFragment : SupportMapFragment(), OnMapReadyCallback {
    private var googleMap: GoogleMap? = null
    private var circleOptions: CustomMapFragment? = null
    private var fromLatLng: LatLng? = null

    @SuppressLint("PotentialBehaviorOverride")
    override fun onMapReady(gmap: GoogleMap) {
        googleMap = gmap

        // Set default position // hcmus => latlng() // ten
//        val hadilao = LatLng(10.907290, 106.643590)
        val hcmus = LatLng(10.76253285, 106.68228373754832)
//        val uef = LatLng(10.7970171, 106.7031929)
//        val uel = LatLng(10.87038795, 106.77833429198822)
//
//        markerFrom(hcmus, "HCMUS")
//        markerTo(hadilao, "Hadilao")
//        markerTo(uef, "UEF")
//        markerTo(uel, "UEL")
//
        googleMap!!.moveCamera(CameraUpdateFactory.newLatLngZoom(hcmus, 11.0f))
//        drawCircle(hcmus, 20000.0)

        googleMap!!.setOnMapClickListener { latLng ->
//            val randomLatLng = LatLng(latLng.latitude, latLng.longitude)
//            val address = "Unknown"
//            markerRandom(randomLatLng, address)
            // Clear previously click position.
//            googleMap!!.clear()
            // Zoom the Marker
//            googleMap!!.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 11.0f))
            // Add Marker on Map
        }

        googleMap!!.setOnMarkerClickListener { marker ->
            if (marker.isInfoWindowShown) {
                marker.hideInfoWindow()
            } else {
                marker.showInfoWindow()
            }
            true
        }
    }

    fun clearMap() {
        googleMap!!.clear()
    }

    fun moveCamera(latlng: LatLng, zoom: Float) {
        googleMap!!.moveCamera(CameraUpdateFactory.newLatLngZoom(latlng, zoom))
    }

    fun markerFrom(location: LatLng, address: String) {
        fromLatLng = location

        val marker = googleMap!!.addMarker(MarkerOptions()
            .position(location)
            .title(address))
    }

    fun markerTo(location: LatLng, address: String) {
        val distance = roundTwoDecimals(distance(fromLatLng, location) / 1000)

        var marker = googleMap!!.addMarker(MarkerOptions()
            .position(location)
            .title(address + " - Khoảng cách: " + distance + " kms")
            .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)))
    }

    fun markerTo(location: LatLng, service: Service) {
        val distance = roundTwoDecimals(distance(fromLatLng, location) / 1000)

        val marker = googleMap!!.addMarker(MarkerOptions()
            .position(location)
            .title(service.serviceName + " - Khoảng cách: " + distance + " kms")
            .snippet(service.serviceDescription)
            .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)))

    }

    fun markerRandom(location: LatLng, address: String) {
        val distance = roundTwoDecimals(distance(fromLatLng, location) / 1000)

        googleMap!!.addMarker(MarkerOptions()
            .position(location)
            .title(address + " - Khoảng cách: " + distance + " kms")
            .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)))
    }

    fun distance(from: LatLng?, to: LatLng): Double { //meter
        return SphericalUtil.computeDistanceBetween(from, to)
    }

    fun drawCircle(location: LatLng, radius: Double) { // radius unit: meter
        googleMap!!.addCircle(
            CircleOptions()
                .center(location)
                .radius(radius * 1000.0)
                .strokeWidth(0f)
                .fillColor(0x550000FF)
        )
    }

    fun roundTwoDecimals(d: Double): Double {
        val twoDForm = DecimalFormat("#.##")
        return java.lang.Double.valueOf(twoDForm.format(d))
    }

    init {
        getMapAsync(this)
    }
}