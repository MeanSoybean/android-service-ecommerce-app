package com.example.androiddevelopmentgroup7

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.androiddevelopmentgroup7.map.MapActivity
import com.example.androiddevelopmentgroup7.profile.ProfileManager
import com.google.android.gms.maps.model.LatLng


class MainActivity : AppCompatActivity(), LocationListener {
    private lateinit var locationManager: LocationManager
    private lateinit var latlng: LatLng
    private val locationPermissionCode = 2

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.test_activity)

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

    }

    private fun getLocation() {
        locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        if ((ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED)) {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), locationPermissionCode)
        }
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000, 5f, this)
    }

    override fun onLocationChanged(location: Location) {
        latlng = LatLng(location.latitude, location.longitude)
//        Log.i(ContentValues.TAG, "Location: " + latlng.toString())
        Toast.makeText(this, "Location: " + latlng.toString(), Toast.LENGTH_SHORT).show()
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == locationPermissionCode) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Permission Granted", Toast.LENGTH_SHORT).show()
            }
            else {
                Toast.makeText(this, "Permission Denied", Toast.LENGTH_SHORT).show()
            }
        }
    }
}

