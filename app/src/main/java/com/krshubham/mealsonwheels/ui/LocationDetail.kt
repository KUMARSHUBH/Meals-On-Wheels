package com.krshubham.mealsonwheels.ui

import android.content.Intent
import android.location.Address
import android.location.Geocoder
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapsInitializer
import com.google.android.gms.maps.model.LatLng
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.krshubham.mealsonwheels.R
import com.krshubham.mealsonwheels.models.User
import kotlinx.android.synthetic.main.activity_location_detail.*
import java.util.*

class LocationDetail : AppCompatActivity() {


    private lateinit var position: LatLng
    private lateinit var geocoder: Geocoder
    private lateinit var addresses: List<Address>
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var firebaseDatabase: FirebaseDatabase
    private lateinit var databaseReference: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_location_detail)

        firebaseAuth = FirebaseAuth.getInstance()
        firebaseDatabase = FirebaseDatabase.getInstance()
        databaseReference = firebaseDatabase.getReference("user")

        geocoder = Geocoder(this, Locale.getDefault())

        position = LatLng(intent.getDoubleExtra("lat", 0.0), intent.getDoubleExtra("lng", 0.0))
        with(mapView) {

            onCreate(null)

            getMapAsync {

                MapsInitializer.initialize(applicationContext)
                setMapLocation(it)
            }
        }

        confirm_location_button.setOnClickListener {

            databaseReference.addValueEventListener(object : ValueEventListener{
                override fun onCancelled(p0: DatabaseError) {

                }


                override fun onDataChange(p0: DataSnapshot) {

                    if(p0.child(firebaseAuth.currentUser!!.uid).exists()){


                        databaseReference.child(firebaseAuth.currentUser!!.uid).updateChildren(mapOf("lat" to addresses[0].latitude, "lng" to addresses[0].longitude, "fullAddress" to complete_detail.text.toString(),"howToReach" to how_to_reach.text.toString()))
                        val intent = Intent(this@LocationDetail, HomeActivity::class.java)
                        startActivity(intent)
                        intent.putExtra("locality",addresses[0].subLocality)
                        finish()

                    }
                    else{
                        val user = User(firebaseAuth.currentUser!!.displayName!!, firebaseAuth.currentUser!!.email!!, addresses[0].latitude, addresses[0].longitude,complete_detail.text.toString(),how_to_reach.text.toString())
                        databaseReference.child(firebaseAuth.currentUser!!.uid).setValue(user)

                        val intent = Intent(this@LocationDetail, HomeActivity::class.java)
                        startActivity(intent)
                        intent.putExtra("locality",addresses[0].subLocality)
                        finish()

                    }
                }


            })
        }


    }

    private fun setMapLocation(map: GoogleMap) {

        with(map) {

            animateCamera(CameraUpdateFactory.newLatLngZoom(position,17f))
            mapType = GoogleMap.MAP_TYPE_NORMAL

            isMyLocationEnabled = true

            addresses = geocoder.getFromLocation(position.latitude,position.longitude,1)
            address.text = addresses[0].subLocality

            setOnCameraIdleListener {

                addresses = geocoder.getFromLocation(cameraPosition.target.latitude,cameraPosition.target.longitude,1)
                address.text = addresses[0].subLocality
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
