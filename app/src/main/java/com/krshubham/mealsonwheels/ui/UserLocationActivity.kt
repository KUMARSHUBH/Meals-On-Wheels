package com.krshubham.mealsonwheels.ui

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.krshubham.mealsonwheels.R


open class UserLocationActivity : AppCompatActivity() {


    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var location: Location

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_location)

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        fetchLocation()


    }

    private fun fetchLocation() {

        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            )
            != PackageManager.PERMISSION_GRANTED
        ) {

            if (ActivityCompat.shouldShowRequestPermissionRationale(
                    this,
                    Manifest.permission.ACCESS_FINE_LOCATION
                )
            ) {

            } else {

                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                    0
                )

            }
        } else {

            val task = fusedLocationClient.lastLocation
            task.addOnSuccessListener {

                if (it != null){
                    location = it
                }

                val intent = Intent(this,LocationDetail::class.java)
                intent.putExtra("lat",location.latitude)
                intent.putExtra("lng",location.longitude)
                startActivity(intent)
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

                    val task = fusedLocationClient.lastLocation
                    task.addOnSuccessListener {

                        if (it != null){
                            location = it
                        }

                        val intent = Intent(this,LocationDetail::class.java)
                        intent.putExtra("lat",location.latitude)
                        intent.putExtra("lng",location.longitude)
                        startActivity(intent)
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
