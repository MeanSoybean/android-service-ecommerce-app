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
import android.widget.*
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.example.androiddevelopmentgroup7.R
import com.example.androiddevelopmentgroup7.models.Service
import com.example.androiddevelopmentgroup7.models.UserLocation
import com.example.androiddevelopmentgroup7.viewModels.ServiceViewModel
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
    // TODO: Rename and change types ofa parameters
    private var param1: String? = null
    private var param2: String? = null
    private var toolbar:MaterialToolbar? = null
    private var loader: FrameLayout? = null
    private var service_spinner: Spinner? = null
    private var radius_spinner: Spinner? = null

    private var mapFragment: CustomMapFragment? = null
    private var locationManager: LocationManager? = null
    private var myLatlng: LatLng =  LatLng(10.76253285, 106.68228373754832)
    private var userLocation: UserLocation? = null
    private val locationPermissionCode = 2
    private val database = Firebase.firestore
    private val auth = Firebase.auth

    private val serviceViewModel : ServiceViewModel by activityViewModels()
    private var serviceType = ArrayList<String>()
    private var radiusList = ArrayList<String>()

    private var serviceLatlngList = ArrayList<LatLng?>()
    private var radius = 20.0

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
        this.loader = view.findViewById(R.id.map_loader_layout)
        this.toolbar = view.findViewById(R.id.topAppBar)
        this.service_spinner = view.findViewById(R.id.filter_type_service_spinner)
        this.radius_spinner = view.findViewById(R.id.filter_radius_spinner)
        //toolbar
        this.toolbar?.setTitle(getString(R.string.map_app_tittle_text))
        this.toolbar?.setNavigationOnClickListener {
            findNavController().popBackStack()
        }

        //service view model
        setServiceViewModel()
        //spinner
        setDataForSpinner()
        setOnItemSelectedListenerForSpinner()
        // map fragment
        this.saveCurrentLocation()
        this.mapFragment = childFragmentManager.findFragmentById(R.id.fragment_map) as CustomMapFragment


    }

    private fun setDataForSpinner() {
        for (radius: Int in 0..10) { radiusList.add(radius.toString() + " kms") }
        var filterAdapter = ArrayAdapter(requireContext(), R.layout.layout_filter_spinner, radiusList)
        filterAdapter.setDropDownViewResource(R.layout.layout_filter_spinner)
        radius_spinner?.adapter = filterAdapter

        serviceViewModel.serviceTypeLivaData.observe(viewLifecycleOwner, Observer { serviceTypeList ->
//            serviceTypeList.add(0, getString(R.string.all_service))
            serviceType.addAll(serviceTypeList)
            filterAdapter = ArrayAdapter(requireContext(), R.layout.layout_filter_spinner, serviceTypeList)
            filterAdapter.setDropDownViewResource(R.layout.layout_filter_spinner)
            service_spinner?.adapter = filterAdapter
        })

    }

    private fun setServiceViewModel() {
        serviceViewModel.setServicesTypeFromDatabase()
        serviceViewModel.setServiceListForUser()
        serviceViewModel.status.value = "hide_loader"
    }

    private fun setLoaderObserveToDrawMap() {
        serviceViewModel.status.observe(viewLifecycleOwner, Observer { status ->
            when(status){
                "hide_loader" -> {
                    loader?.visibility = View.GONE

                    mapFragment!!.markerFrom(myLatlng, "Vị trí hiện tại")
                    getAndMarkerServiceList(radius)
                    mapFragment!!.drawCircle(myLatlng, radius)
                }
                "loading"-> {
                    loader?.visibility = View.VISIBLE
                }
            }
        })
    }

    private fun setOnItemSelectedListenerForSpinner() {
        service_spinner?.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(p0: AdapterView<*>?) {}
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, index: Int, p3: Long) {
                var typeOfService = service_spinner!!.getSelectedItem().toString()
                serviceViewModel.queryDataFilter("servicePrice", "des", typeOfService)
                mapFragment!!.clearMap()
                mapFragment!!.markerFrom(myLatlng, "Vị trí hiện tại")
                radius_spinner!!.setSelection(0)
//                setLoaderObserveToDrawMap()
            }
        }

        radius_spinner?.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(p0: AdapterView<*>?) {}
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, index: Int, p3: Long) {
                mapFragment!!.clearMap()
                mapFragment!!.markerFrom(myLatlng, "Vị trí hiện tại")

                radius = index * 1.0
                mapFragment!!.drawCircle(myLatlng, radius)

                getAndMarkerServiceList(radius)
            }
        }
    }


    private fun getAndMarkerServiceList(radius: Double){
        try {
            var latlng: LatLng? = null

            for (service: Service in serviceViewModel.selectedServiceList.value!!) {
                this.database.collection("Locations")
                    .whereEqualTo("accountID", service.vendorID)
                    .get()
                    .addOnSuccessListener { result ->
                        for (document in result) { // 1
                            latlng = LatLng(
                                document.data.get("latitude").toString().toDouble(),
                                document.data.get("longitude").toString().toDouble()
                            )
                        }
                        serviceLatlngList.add(latlng!!)
                    }
                    .addOnFailureListener { exception ->
                        Log.w(ContentValues.TAG, "Error getting documents.", exception)
                    }
            }
            Log.w("Service latlng list:", serviceLatlngList.toString())

            if (!serviceLatlngList.isEmpty()) {
                for (i: Int in 0..serviceLatlngList.size) {
                    if (serviceLatlngList[i] != null && mapFragment!!.distance(myLatlng, serviceLatlngList[i]!!) / 1000.0 <= radius) {
                        mapFragment!!.markerTo(
                            serviceLatlngList[i]!!,
                            serviceViewModel.selectedServiceList.value!![i]
                        )
                    }
                }
            }
        } catch (e: IndexOutOfBoundsException) {
            Log.i("IndexOutOfBounds:", e.toString())
        }
        serviceLatlngList = ArrayList<LatLng?>()
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
        this.myLatlng = LatLng(location.latitude, location.longitude)
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
        this.getCurrentLocation()

        this.database.collection("Locations")
            .whereEqualTo("accountID", auth.currentUser!!.uid)
            .get()
            .addOnSuccessListener { result ->
                for (document in result) {
                    this.userLocation = UserLocation(
                        document.data.get("accountID").toString(),
                        document.data.get("address").toString(),
                        this.myLatlng.latitude.toString(),
                        this.myLatlng.longitude.toString()
                    )
                }
//                Log.i("HIHI", this.userLocation.toString())
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