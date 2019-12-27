package com.krshubham.mealsonwheels

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.SearchView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.krshubham.mealsonwheels.db.CartDataSource
import com.krshubham.mealsonwheels.db.CartDataSourceImpl
import com.krshubham.mealsonwheels.db.CartDatabase
import com.krshubham.mealsonwheels.models.User
import com.krshubham.mealsonwheels.ui.*
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.bottom_sheet_search_location.*
import kotlinx.android.synthetic.main.cart_bottom_sheet.*
import kotlinx.android.synthetic.main.cooking_instruction_dialog_layout.*
import kotlinx.android.synthetic.main.cooking_instruction_dialog_layout.view.*
import java.util.*

class BottomSheetFragment : BottomSheetDialogFragment() {

    private lateinit var cartDataSource: CartDataSource
    private lateinit var compositeDisposable: CompositeDisposable

    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var location: Location
    private lateinit var geocoder: Geocoder
    private lateinit var addresses: List<Address>
    private lateinit var databaseReference: DatabaseReference
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var resReference: DatabaseReference

    private lateinit var userName: String
    private lateinit var phone_no: String
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View =
        inflater.inflate(R.layout.cart_bottom_sheet, container, false)


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        var itemTotal = 0.0
        var tax = 0.0

        cartDataSource = CartDataSourceImpl(CartDatabase.getInstance(context!!).cartDao())
        compositeDisposable = CompositeDisposable()

        change_order_info.setOnClickListener {

            val intent = Intent(this.activity, UserDetail::class.java)
            intent.putExtra("name", userName)
            intent.putExtra("phone_no", phone_no)
            startActivity(intent)
        }

        firebaseAuth = FirebaseAuth.getInstance()
        databaseReference =
            FirebaseDatabase.getInstance().getReference("user/${firebaseAuth.currentUser?.uid}")

        orders_recycler_view.layoutManager = LinearLayoutManager(
            context,
            LinearLayoutManager.VERTICAL, false
        )

        special_instruction_text.setOnClickListener {

            val alertDialog = AlertDialog.Builder(this.context!!).create()
            val dialogView =
                layoutInflater.inflate(R.layout.cooking_instruction_dialog_layout, null)
            alertDialog.setView(dialogView)
            alertDialog.show()

            dialogView.add_instruction.setOnClickListener {

                val textView = TextView(context)
                textView.text = alertDialog.instructions_edit_text.text.toString()

                instruction_list.visibility = View.VISIBLE
                instruction_list.addView(textView)

                alertDialog.dismiss()
            }

            alertDialog.cancel_dialog.setOnClickListener {

                alertDialog.dismiss()
            }


        }

        geocoder = Geocoder(context, Locale.getDefault())

        fusedLocationClient =
            LocationServices.getFusedLocationProviderClient(this.activity as Activity)

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


        location_change.setOnClickListener {

            bottomSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED
        }

        close.setOnClickListener {

            bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
        }

        use_location.setOnClickListener {

            fetchLocation()

        }

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {


                addresses = geocoder.getFromLocationName(query, 1)
                if (addresses.isNotEmpty()) {

                    val lat = addresses[0].latitude
                    val lng = addresses[0].longitude
                    val intent = Intent(context, LocationDetail::class.java)
                    intent.putExtra("lat", lat)
                    intent.putExtra("lng", lng)
                    startActivity(intent)
                } else {

                    Toast.makeText(
                        context,
                        "Try again with different name",
                        Toast.LENGTH_LONG
                    ).show()
                }

                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {

                return true
            }


        })


        compositeDisposable.add(
            cartDataSource.getAllCartItems()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    val adapter = CartListAdapter(context!!, it)
                    orders_recycler_view.adapter = adapter

                    itemTotal = 0.0
                    it.forEach { cartItem ->

                        val total = (cartItem.quantity) * (cartItem.price)
                        itemTotal += total
                    }


                    tax = (itemTotal * 18) / 100


                    total_price.text = itemTotal.toString()
                    taxes.text = tax.toString()

                    total_payable_price.text = (itemTotal + tax).toString()
                    total_cost.text = total_payable_price.text

                }, {
                    Toast.makeText(context, it.message, Toast.LENGTH_LONG).show()
                })
        )


        pay_options.setOnClickListener {

            val intent = Intent(this.context, ChoosePaymentModeActivity::class.java)
            startActivityForResult(intent, 0)
        }

        pay_bottom_sheet.setOnClickListener {


            databaseReference = FirebaseDatabase.getInstance().getReference("orders")
            val key =
                databaseReference.child("${FirebaseAuth.getInstance().currentUser?.uid}").push()

            var resId = ""
            var resAddress = ""
            compositeDisposable.add(cartDataSource.getAllCartItems()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe { cartList ->

                    cartList.forEach {

                        key.child(it.foodId).setValue(it.quantity)

                    }


                })
            //                payUsingUpi(total_cost_cart.text.toString(),"9162169596@paytm","Meals On Wheels","Payment at $name")
            startActivity(Intent(this.context, OrderTrackingActivity::class.java))



            key.child("total_cost").setValue(itemTotal)
            key.child("tax").setValue(tax)
            key.child("date")
                .setValue(Calendar.getInstance().time.toString().substringBefore("G"))
            key.child("phone")
                .setValue(usr_email_phone.text.toString().substringAfter(","))
            key
                .child("delivery_address").setValue(delivery_address.text.toString())

            key.child("res_address").setValue((this.activity as RestaurantDetailActivity).intent.getStringExtra("resAddress"))
            key.child("res_id").setValue((this.activity as RestaurantDetailActivity).intent.getStringExtra("id"))
            key.child("res_name").setValue((this.activity as RestaurantDetailActivity).intent.getStringExtra("name"))


            compositeDisposable.add(cartDataSource.cleanCart().subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe {

                })


        }

    }

    private fun fetchLocation() {

        if (ContextCompat.checkSelfPermission(
                context!!,
                Manifest.permission.ACCESS_FINE_LOCATION
            )
            != PackageManager.PERMISSION_GRANTED
        ) {


            ActivityCompat.requestPermissions(
                this.activity as Activity,
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

                val intent = Intent(context, LocationDetail::class.java)
                intent.putExtra("lat", location.latitude)
                intent.putExtra("lng", location.longitude)
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

                    if (use_current_location != null) {

                        use_current_location.text = "Detecting Location..."
                    }
                    val task = fusedLocationClient.lastLocation
                    task.addOnSuccessListener {

                        use_current_location.text = "Use current location"
                        if (it != null) {
                            location = it
                        }

                        val intent = Intent(context, LocationDetail::class.java)
                        intent.putExtra("lat", location.latitude)
                        intent.putExtra("lng", location.longitude)
                        startActivity(intent)
                    }


                } else {

                    AlertDialog.Builder(context!!)
                        .setTitle("Location Permission Denied")
                        .setMessage("We use this permission to detect your current location and show you great restaurants around you. Are you sure you want to deny the permission?")
                        .setPositiveButton("RETRY") { dialogInterface, i ->

                            ActivityCompat.requestPermissions(
                                this.activity as Activity,
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

    override fun onResume() {
        super.onResume()
        databaseReference.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {

            }

            override fun onDataChange(p0: DataSnapshot) {

                val user = p0.getValue(User::class.java)
                if (user != null) {
                    delivery_address.text =
                        geocoder.getFromLocation(user.lat, user.lng, 1)[0].subLocality

                    userName = user.name
                    phone_no = user.phone!!
                    usr_email_phone.text = "$userName, $phone_no"


                }

            }

        })
    }

    override fun onStop() {

        compositeDisposable.clear()
        super.onStop()

    }
}
