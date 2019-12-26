package com.krshubham.mealsonwheels.ui

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapsInitializer
import com.google.android.gms.maps.model.LatLng
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.krshubham.mealsonwheels.R
import kotlinx.android.synthetic.main.activity_order_tracking.*

class OrderTrackingActivity : AppCompatActivity() {

    lateinit var uid: String
    private lateinit var databaseReference: DatabaseReference
    var position: LatLng? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTheme(R.style.AppTheme_OrderTrackingActivity)
        setContentView(R.layout.activity_order_tracking)


        uid  =  FirebaseAuth.getInstance().currentUser?.uid!!

        databaseReference = FirebaseDatabase.getInstance().getReference("user/$uid")

        databaseReference.addValueEventListener(object : ValueEventListener{
            override fun onCancelled(p0: DatabaseError) {

            }

            override fun onDataChange(p0: DataSnapshot) {

                position = LatLng(p0.child("lat").value as Double, p0.child("lng").value as Double)

            }


        })

        with(order_tracking_map_view) {

            onCreate(null)

            getMapAsync {

                MapsInitializer.initialize(applicationContext)
                setMapLocation(it)
            }
        }
    }


    private fun setMapLocation(map: GoogleMap) {

        with(map) {

            animateCamera(CameraUpdateFactory.newLatLngZoom(position ?: LatLng(0.0,0.0),15f))
            mapType = GoogleMap.MAP_TYPE_NORMAL

        }
    }


    override fun onResume() {
        super.onResume()
        order_tracking_map_view.onResume()
    }

    override fun onBackPressed() {
        super.onBackPressed()

        startActivity(Intent(this, HomeActivity::class.java))
    }
}
