package com.krshubham.mealsonwheels.ui

import android.location.Address
import android.location.Geocoder
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapsInitializer
import com.google.android.gms.maps.model.LatLng
import com.krshubham.mealsonwheels.R
import kotlinx.android.synthetic.main.activity_location_detail.*
import java.util.*

class LocationDetail : AppCompatActivity() {


    private lateinit var position: LatLng
    private lateinit var geocoder: Geocoder
    private lateinit var addresses: List<Address>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_location_detail)

        geocoder = Geocoder(this, Locale.getDefault())

        position = LatLng(intent.getDoubleExtra("lat", 0.0), intent.getDoubleExtra("lng", 0.0))
        with(mapView) {

            onCreate(null)

            getMapAsync {

                MapsInitializer.initialize(applicationContext)
                setMapLocation(it)

            }
        }
    }

    private fun setMapLocation(map: GoogleMap) {

        with(map) {

            animateCamera(CameraUpdateFactory.newLatLngZoom(position,17f))
            mapType = GoogleMap.MAP_TYPE_NORMAL

            isMyLocationEnabled = true

            setOnCameraIdleListener {

                var a = cameraPosition.target.latitude
                var b = cameraPosition.target.longitude

                animateCamera(CameraUpdateFactory.newLatLngZoom(cameraPosition.target,17f))
                addresses = geocoder.getFromLocation(cameraPosition.target.latitude,cameraPosition.target.latitude,1)
                address.text = addresses[0].locality

            }

        }
    }

    override fun onResume() {
        super.onResume()
        mapView.onResume()
    }

    override fun onPause() {
        super.onPause()
        mapView.onPause()
    }

    override fun onDestroy() {
        super.onDestroy()
        mapView.onDestroy()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        mapView.onLowMemory()
    }


}
