package com.example.androiddevelopmentgroup7

import android.app.ProgressDialog
import android.os.AsyncTask
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentManager
import com.example.androiddevelopmentgroup7.map.MapFragment
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import java.util.*


class MainActivity : AppCompatActivity() {
    private var myMapFragment: MapFragment? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.map)

        val fragmentManager: FragmentManager = this.supportFragmentManager
        myMapFragment = fragmentManager.findFragmentById(R.id.fragment_map) as MapFragment?

    }



}

