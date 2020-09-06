package com.example.quarantinelogin.fragments

import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import com.example.quarantinelogin.LoginActivity
import com.example.quarantinelogin.MainActivity
import com.example.quarantinelogin.R
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.CircleOptions
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions

class GoogleMapsFragment : Fragment() {

    @RequiresApi(Build.VERSION_CODES.O)
    private val callback = OnMapReadyCallback { googleMap ->
        var latitude: Double? = null
        var longitude: Double? = null
        try {
            latitude = LoginActivity.Companion.user?.getJSONObject("Item")
                ?.getJSONObject("latitude")?.getDouble("N")
            longitude = LoginActivity.Companion.user?.getJSONObject("Item")
                ?.getJSONObject("longitude")?.getDouble("N")
        } catch (error: Exception) {
            println("Silently fail due to no issue")
        }

        if (latitude == null || longitude == null) {
            latitude = MainActivity.Companion.latitude
            longitude = MainActivity.Companion.longitude
        }
        /**
         * Manipulates the map once available.
         * This callback is triggered when the map is ready to be used.
         * This is where we can add markers or lines, add listeners or move the camera.
         * In this case, we just add a marker near Sydney, Australia.
         * If Google Play services is not installed on the device, the user will be prompted to
         * install it inside the SupportMapFragment. This method will only be triggered once the
         * user has installed Google Play services and returned to the app.
         */
        if (latitude != null && longitude != null) {
            val currentLocation = LatLng(latitude, longitude)

            googleMap.addMarker(MarkerOptions().position(currentLocation).title("Geofence Marker"))
            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLocation, 20.0f))

            // Showing GeoFence
            val circleOptions: CircleOptions = CircleOptions()
            circleOptions.center(currentLocation)
            circleOptions.radius(25.0)
            circleOptions.visible(true)
            circleOptions.fillColor(Color.valueOf(0.0f, 0.0f, 1.0f, 0.2f).toArgb())
            googleMap.addCircle(circleOptions)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_google_maps, container, false)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment?.getMapAsync(callback)
    }
}