package com.test.my_attendance.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.test.my_attendance.R
import com.test.my_attendance.databinding.ActivityMainBinding
import com.test.my_attendance.util.Permissions
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if(Permissions.hasLocationPermission(this)){
            findNavController(R.id.navHostFragment).navigate(R.id.action_permissionFragment_to_mapsFragment)
        }

    }
}