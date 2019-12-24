package com.krshubham.mealsonwheels.ui.fragments.cart

import android.Manifest
import android.app.Activity
import android.app.Activity.RESULT_OK
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.net.ConnectivityManager
import android.net.Uri
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
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.krshubham.mealsonwheels.CartListAdapter
import com.krshubham.mealsonwheels.R
import com.krshubham.mealsonwheels.db.CartDataSource
import com.krshubham.mealsonwheels.db.CartDataSourceImpl
import com.krshubham.mealsonwheels.db.CartDatabase
import com.krshubham.mealsonwheels.models.Restaurant
import com.krshubham.mealsonwheels.models.User
import com.krshubham.mealsonwheels.ui.ChoosePaymentModeActivity
import com.krshubham.mealsonwheels.ui.LocationDetail
import com.krshubham.mealsonwheels.ui.RestaurantDetailActivity
import com.krshubham.mealsonwheels.ui.UserDetail
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.bottom_sheet_search_location.*
import kotlinx.android.synthetic.main.cooking_instruction_dialog_layout.*
import kotlinx.android.synthetic.main.cooking_instruction_dialog_layout.view.*
import kotlinx.android.synthetic.main.fragment_cart.*
import kotlinx.android.synthetic.main.layout_empty_cart.*
import java.util.*

class CartFragment : Fragment() {

    private lateinit var cartViewModel: CartViewModel
    private lateinit var cartDataSource: CartDataSource
    private lateinit var compositeDisposable: CompositeDisposable

    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private var location: Location? = null
    private lateinit var geocoder: Geocoder
    private lateinit var addresses: List<Address>
    private lateinit var databaseReference: DatabaseReference
    private lateinit var resReference: DatabaseReference
    private lateinit var firebaseAuth: FirebaseAuth

    private lateinit var userName: String
    private lateinit var phone_no: String
    private lateinit var sharedPreferences: SharedPreferences
    private var empty: Boolean = false


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        cartViewModel =
            ViewModelProviders.of(this).get(CartViewModel::class.java)

        cartDataSource = CartDataSourceImpl(CartDatabase.getInstance(context!!).cartDao())
        compositeDisposable = CompositeDisposable()
        sharedPreferences = context?.getSharedPreferences("OrderRestId", Context.MODE_PRIVATE)!!

        return if (sharedPreferences.getString(
                "resId",
                ""
            ).isNullOrEmpty() || sharedPreferences.getString("resId", "") == ""
        ) {
            empty = true
            inflater.inflate(R.layout.layout_empty_cart, container, false)
        } else {
            empty = false
            inflater.inflate(R.layout.fragment_cart, container, false)
        }


    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        if (!empty) {

            var itemTotal: Double
            var tax: Double


            var name = ""
            var id = ""
            var phone = ""
            var image = ""
            var rating = ""


            change_order_info_cart.setOnClickListener {

                val intent = Intent(this.activity, UserDetail::class.java)
                intent.putExtra("name", userName)
                intent.putExtra("phone_no", phone_no)
                startActivity(intent)
            }

            firebaseAuth = FirebaseAuth.getInstance()
            databaseReference =
                FirebaseDatabase.getInstance().getReference("user/${firebaseAuth.currentUser?.uid}")



            orders_recycler_view_cart.layoutManager = LinearLayoutManager(
                context,
                LinearLayoutManager.VERTICAL, false
            )

            special_instruction_text_cart.setOnClickListener {

                val alertDialog = AlertDialog.Builder(this.context!!).create()
                val dialogView =
                    layoutInflater.inflate(R.layout.cooking_instruction_dialog_layout, null)
                alertDialog.setView(dialogView)
                alertDialog.show()

                dialogView.add_instruction.setOnClickListener {

                    val textView = TextView(context)
                    textView.text = alertDialog.instructions_edit_text.text.toString()

                    instruction_list_cart.visibility = View.VISIBLE
                    instruction_list_cart.addView(textView)

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


            location_change_cart.setOnClickListener {

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
                        orders_recycler_view_cart.adapter = adapter

                        if (it.isNullOrEmpty()) {
                            sharedPreferences.edit().putString("resId", "").apply()

                        }

                        empty_cart.visibility = View.GONE

                        var idFromFood = ""
                        itemTotal = 0.0
                        it?.forEach { cartItem ->


                            idFromFood = "-${cartItem.foodId.substringAfter("-")}"
                            val total = (cartItem.quantity) * (cartItem.price)
                            itemTotal += total
                        }

                        resReference =
                            FirebaseDatabase.getInstance().getReference("restaurant/$idFromFood")

                        resReference.addListenerForSingleValueEvent(object : ValueEventListener {
                            override fun onCancelled(p0: DatabaseError) {

                            }

                            override fun onDataChange(ds: DataSnapshot) {

                                val restaurant = Restaurant()
                                restaurant.apply {

                                    id = ds.child("id").getValue(true).toString()
                                    name = ds.child("name").getValue(true).toString()
                                    rating = ds.child("rating").getValue(true).toString()
                                    image = ds.child("image").getValue(true).toString()
                                    phone = ds.child("phone").getValue(true).toString()

                                }


                                res_name_cart.text = name

                                if (context?.getSharedPreferences(
                                        "CartEmpty",
                                        Context.MODE_PRIVATE
                                    )?.getBoolean("empty", false)!!
                                ) {

                                    empty_cart.visibility = View.VISIBLE

                                    browse_res.setOnClickListener {

                                        Navigation.findNavController(it)
                                            .navigate(R.id.action_navigation_cart_to_navigation_food)
                                    }
                                }
                            }


                        })


                        res_name_cart.setOnClickListener {

                            val intent = Intent(this.context, RestaurantDetailActivity::class.java)
                            intent.putExtra("id", id)
                            intent.putExtra("name", name)
                            intent.putExtra("phone", phone)
                            intent.putExtra("image", image)
                            intent.putExtra("rating", rating)
                            startActivity(intent)
                        }
                        tax = (itemTotal * 18) / 100



                        total_price_cart.text = itemTotal.toString()
                        taxes_cart.text = tax.toString()

                        total_payable_price_cart.text = (itemTotal + tax).toString()
                        total_cost_cart.text = total_payable_price_cart.text

                    }, {
                        Toast.makeText(context, it.message, Toast.LENGTH_LONG).show()
                    })
            )

            payment_options.setOnClickListener {

                val intent = Intent(this.context,ChoosePaymentModeActivity::class.java)
                startActivityForResult(intent,0)
            }

            pay.setOnClickListener {

                payUsingUpi(total_cost_cart.text.toString(),"9162169596@paytm","Meals On Wheels","Payment at $name")
            }

        }
        else {

            geocoder = Geocoder(context, Locale.getDefault())
            fusedLocationClient = FusedLocationProviderClient(context!!)
            geocoder = Geocoder(context)
            databaseReference = FirebaseDatabase.getInstance().reference
            resReference = FirebaseDatabase.getInstance().reference
            firebaseAuth = FirebaseAuth.getInstance()
            userName = ""
            phone_no = ""

            browse.setOnClickListener {

                Navigation.findNavController(it)
                    .navigate(R.id.action_navigation_cart_to_navigation_food)
            }

        }

    }


    private fun payUsingUpi(amount: String, upiId: String, name: String, note: String) {

        val uri = Uri.parse("upi://pay").buildUpon()
            .appendQueryParameter("pa", upiId)
            .appendQueryParameter("pn", name)
            .appendQueryParameter("tn", note)
            .appendQueryParameter("am", amount)
            .appendQueryParameter("cu", "INR")
            .build()

        val upiPayIntent = Intent(Intent.ACTION_VIEW)
        upiPayIntent.data = uri

        val chooser = Intent.createChooser(upiPayIntent, "Pay with")

        if (null != chooser.resolveActivity(context?.packageManager!!)) {
            startActivityForResult(chooser, 0)
        } else {
            Toast.makeText(
                this.context,
                "No UPI app found, please install one to continue",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        when (requestCode) {

            0 -> {

                if ((RESULT_OK == resultCode) || (resultCode == 11)) {
                    if (data != null) {
                        val trxt = data.getStringExtra("response")
                        val dataList = ArrayList<String>()
                        dataList.add(trxt!!)
//                        upiPaymentDataOperation(dataList)
                    } else {

                        val dataList = ArrayList<String>()
                        dataList.add("nothing")
//                        upiPaymentDataOperation(dataList)
                    }
                } else {

                    val dataList = ArrayList<String>()
                    dataList.add("nothing")
//                    upiPaymentDataOperation(dataList)
                }

            }
        }
    }

//    private fun upiPaymentDataOperation(data: ArrayList<String>) {
//
//        if(isConnectionAvailable(context!!)){
//
//            var str: String? = data[0]
//            var paymentCancel = ""
//            if(str == null)
//                str = "discard"
//        }
//    }

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
                intent.putExtra("lat", location?.latitude)
                intent.putExtra("lng", location?.longitude)
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
                        intent.putExtra("lat", location?.latitude)
                        intent.putExtra("lng", location?.longitude)
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

    private fun isConnectionAvailable(context: Context): Boolean {

        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val netInfo = connectivityManager.activeNetworkInfo
        if (netInfo != null && netInfo.isConnected
            && netInfo.isConnectedOrConnecting
            && netInfo.isAvailable
        ) {
            return true
        }
        return false
    }

    override fun onResume() {
        super.onResume()

        if (!empty) {

            databaseReference.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onCancelled(p0: DatabaseError) {

                }

                override fun onDataChange(p0: DataSnapshot) {

                    val user = p0.getValue(User::class.java)
                    if (user != null) {
                        delivery_address_cart.text =
                            geocoder.getFromLocation(user.lat, user.lng, 1)[0].subLocality

                        userName = user.name
                        phone_no = user.phone!!
                        usr_email_phone_cart.text = "$userName, $phone_no"


                    }

                }

            })
        }

    }


    override fun onStop() {

        compositeDisposable.clear()
        super.onStop()

    }
}