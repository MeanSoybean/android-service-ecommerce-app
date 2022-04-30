package com.example.androiddevelopmentgroup7.views.fragments

import android.annotation.SuppressLint
import com.google.android.gms.maps.*
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.CircleOptions
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.maps.android.SphericalUtil
import java.text.DecimalFormat


class CustomMapFragment : SupportMapFragment(), OnMapReadyCallback {
    private var googleMap: GoogleMap? = null
    private var circleOptions: CustomMapFragment? = null
    lateinit var fromLatLng: LatLng

    @SuppressLint("PotentialBehaviorOverride")
    override fun onMapReady(gmap: GoogleMap) {
        googleMap = gmap

        // Set default position // hcmus => latlng() // ten
        val hadilao = LatLng(10.907290, 106.643590)
        val hcmus = LatLng(10.76253285, 106.68228373754832)
        val uef = LatLng(10.7970171, 106.7031929)
        val uel = LatLng(10.87038795, 106.77833429198822)

        markerFrom(hcmus, "HCMUS")
        markerTo(hadilao, "Hadilao")
        markerTo(uef, "UEF")
        markerTo(uel, "UEL")

        googleMap!!.moveCamera(CameraUpdateFactory.newLatLngZoom(hcmus, 11.0f))
        drawCircle(hcmus, 20000.0)

        googleMap!!.setOnMapClickListener { latLng ->
            val randomLatLng = LatLng(latLng.latitude, latLng.longitude)
            val address = "Unknown"
            markerRandom(randomLatLng, address)
            // Clear previously click position.
//            googleMap!!.clear()
            // Zoom the Marker
//            googleMap!!.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 11.0f))
            // Add Marker on Map
        }

//        val line: Polyline = googleMap!!.addPolyline(
//            PolylineOptions()
//                .add(vietnam, cambodia)
//                .width(5f)
//                .color(Color.RED)
//        )

        googleMap!!.setOnMarkerClickListener { marker ->
            if (marker.isInfoWindowShown) {
                marker.hideInfoWindow()
            } else {
                marker.showInfoWindow()
            }
            true
        }
    }

    fun markerFrom(location: LatLng, address: String) {
        fromLatLng = location

        googleMap!!.addMarker(MarkerOptions()
            .position(location)
            .title("Marker in " + address))
    }

    fun markerTo(location: LatLng, address: String) {
        val distance = roundTwoDecimals(distance(fromLatLng, location) / 1000)

        googleMap!!.addMarker(MarkerOptions()
            .position(location)
            .title(address + " - distance: " + distance + " km")
            .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)))
    }

    fun markerRandom(location: LatLng, address: String) {
        val distance = roundTwoDecimals(distance(fromLatLng, location) / 1000)

        googleMap!!.addMarker(MarkerOptions()
            .position(location)
            .title(address + " - distance: " + distance + " km")
            .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)))
    }

    fun distance(from: LatLng, to: LatLng): Double { //meter
        return SphericalUtil.computeDistanceBetween(from, to)
    }

    fun drawCircle(location: LatLng, radius: Double) { // radius unit: meter
        googleMap!!.addCircle(
            CircleOptions()
                .center(location)
                .radius(radius)
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