package com.example.androiddevelopmentgroup7.views.fragments

import android.Manifest
import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.androiddevelopmentgroup7.R
import com.example.androiddevelopmentgroup7.models.Profile
import com.example.androiddevelopmentgroup7.models.UserLocation
import com.google.android.gms.maps.model.LatLng
import com.google.android.material.appbar.MaterialToolbar
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [fragment_near_service_location.newInstance] factory method to
 * create an instance of this fragment.
 */
class fragment_near_service_location : Fragment(), LocationListener {
    // TODO: Rename and change types of parameters
    private var param1: String  ? = null
    private var param2: String? = null
    private var toolbar:MaterialToolbar? = null
    private var mapFragment: CustomMapFragment? = null
    private var locationManager: LocationManager? = null
    private var latlng: LatLng? = null
    private var userLocation: UserLocation? = null
    private val locationPermissionCode = 2
    private val database = Firebase.firestore
    private val auth = Firebase.auth

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_near_service_location, container, false)
        initComponent(view)

        return view
    }


    @SuppressLint("PotentialBehaviorOverride")
    private fun initComponent(view:View){
        this.toolbar = view.findViewById(R.id.topAppBar)
        this.toolbar?.setTitle(getString(R.string.map_app_tittle_text))
        this.toolbar?.setNavigationOnClickListener {
            findNavController().popBackStack()
        }

        this.mapFragment = childFragmentManager.findFragmentById(R.id.fragment_map) as CustomMapFragment
        saveCurrentLocation()

    }

    private fun getCurrentLocation() {
        try {
            this.locationManager = getContext()?.getSystemService(Context.LOCATION_SERVICE) as LocationManager
            if ((ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED)) {
                ActivityCompat.requestPermissions(requireActivity(), arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), locationPermissionCode)
            }

            this.locationManager!!.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000, 5f, this)
        } catch (e: SecurityException) {
            e.printStackTrace()
        }
    }

    override fun onLocationChanged(location: Location) {
        this.latlng = LatLng(location.latitude, location.longitude)
//        Log.i(ContentValues.TAG, "Location: " + latlng.toString())
//        Toast.makeText(requireContext(), "Location: " + latlng.toString(), Toast.LENGTH_SHORT).show()
    }

    @Deprecated("Deprecated in Java")
    @SuppressLint("MissingPermission")
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        @Suppress("DEPRECATION")
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == this.locationPermissionCode) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                this.locationManager?.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000, 5f, this)
                Toast.makeText(requireContext(), "Permission Granted", Toast.LENGTH_SHORT).show()
            }
            else {
                Toast.makeText(requireContext(), "Permission Denied", Toast.LENGTH_SHORT).show()
            }
        }
    }

    fun saveCurrentLocation() {
        getCurrentLocation()
        this.database.collection("Locations")
            .whereEqualTo("accountID", auth.currentUser!!.uid)
            .get()
            .addOnSuccessListener { result ->
                for (document in result) {
                    this.userLocation = UserLocation(
                        document.data.get("accountID") as String,
                        document.data.get("address") as String,
                        this.latlng?.latitude.toString(),
                        this.latlng?.longitude.toString()
                    )
                }
                Log.i("HIHI", this.userLocation.toString())
                this.database.collection("Locations")
                    .document(auth.currentUser!!.uid)
                    .set(this.userLocation!!)

            }
            .addOnFailureListener { exception ->
                Log.w(ContentValues.TAG, "Error getting documents.", exception)
            }
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment fragment_near_service_location.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            fragment_near_service_location().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}