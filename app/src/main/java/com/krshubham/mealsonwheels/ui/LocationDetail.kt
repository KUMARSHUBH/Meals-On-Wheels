package com.krshubham.mealsonwheels.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapsInitializer
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.krshubham.mealsonwheels.R
import kotlinx.android.synthetic.main.activity_location_detail.*

class LocationDetail : AppCompatActivity() {


    private lateinit var position: LatLng
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_location_detail)

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
            uiSettings.setAllGesturesEnabled(true)
            addMarker(MarkerOptions().position(position).draggable(true).icon(BitmapDescriptorFactory.defaultMarker()))
            mapType = GoogleMap.MAP_TYPE_NORMAL

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
