package com.krshubham.mealsonwheels.ui

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.krshubham.mealsonwheels.R
import kotlinx.android.synthetic.main.activity_user_location.*
import kotlinx.android.synthetic.main.bottom_sheet_search_location.*
import java.util.*


open class UserLocationActivity : AppCompatActivity() {


    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var location: Location
    private lateinit var geocoder: Geocoder
    private lateinit var addresses: List<Address>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_location)

        geocoder = Geocoder(this, Locale.getDefault())

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        fetchLocation()

        val bottomSheetBehavior = BottomSheetBehavior.from(bottom_search_sheet)
        bottomSheetBehavior.setBottomSheetCallback(object :
            BottomSheetBehavior.BottomSheetCallback() {
            override fun onSlide(p0: View, p1: Float) {

            }

            override fun onStateChanged(p0: View, p1: Int) {

                if (p1 == BottomSheetBehavior.STATE_DRAGGING) {
                    bottomSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED
                }
            }
        })


        location_button.setOnClickListener {

            bottomSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED
        }

        close.setOnClickListener {

            bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
        }

        use_location.setOnClickListener {

            fetchLocation()

        }

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {


                addresses = geocoder.getFromLocationName(query,1)
                if(addresses.isNotEmpty()){

                    val lat = addresses[0].latitude
                    val lng = addresses[0].longitude
                    val intent = Intent(this@UserLocationActivity, LocationDetail::class.java)
                    intent.putExtra("lat", lat)
                    intent.putExtra("lng",lng)
                    startActivity(intent)
                    finish()
                }

                else{

                    Toast.makeText(this@UserLocationActivity,"Try again with different name", Toast.LENGTH_LONG).show()
                }

                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {

                return true
            }


        })

    }

    private fun fetchLocation() {

        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            )
            != PackageManager.PERMISSION_GRANTED
        ) {


            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                0
            )

        } else {

            if (use_current_location != null) {

                use_current_location.text = "Detecting Location..."
            }
            val task = fusedLocationClient.lastLocation
            task.addOnSuccessListener {

                use_current_location.text = "Use current location"
                if (it != null) {
                    location = it
                }

                val intent = Intent(this, LocationDetail::class.java)
                intent.putExtra("lat", location.latitude)
                intent.putExtra("lng", location.longitude)
                startActivity(intent)
                finish()
            }


        }

    }


    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>, grantResults: IntArray
    ) {
        when (requestCode) {
            0 -> {

                if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {

                    if (use_current_location != null) {

                        use_current_location.text = "Detecting Location..."
                    }
                    val task = fusedLocationClient.lastLocation
                    task.addOnSuccessListener {

                        use_current_location.text = "Use current location"
                        if (it != null) {
                            location = it
                        }

                        val intent = Intent(this, LocationDetail::class.java)
                        intent.putExtra("lat", location.latitude)
                        intent.putExtra("lng", location.longitude)
                        startActivity(intent)
                        finish()
                    }


                } else {

                    AlertDialog.Builder(this)
                        .setTitle("Location Permission Denied")
                        .setMessage("We use this permission to detect your current location and show you great restaurants around you. Are you sure you want to deny the permission?")
                        .setPositiveButton("RETRY") { dialogInterface, i ->

                            ActivityCompat.requestPermissions(
                                this,
                                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                                0
                            )

                        }
                        .setNegativeButton("I'M SURE") { dialogInterface, i ->

                            dialogInterface.dismiss()
                        }
                        .create()
                        .show()
                }
                return
            }

            else -> {
                // Ignore all other requests.
            }
        }
    }


}
