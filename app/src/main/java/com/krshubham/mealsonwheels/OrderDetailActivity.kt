package com.krshubham.mealsonwheels

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.krshubham.mealsonwheels.adapter.OrderDetailListAdapter
import kotlinx.android.synthetic.main.activity_order_detail.*


private lateinit var orderDetailListAdapter: OrderDetailListAdapter
private lateinit var orderReference: DatabaseReference
private lateinit var foodReference: DatabaseReference


class OrderDetailActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTheme(R.style.AppTheme_OrderTrackingActivity)
        setContentView(R.layout.activity_order_detail)


        foodReference = FirebaseDatabase.getInstance().getReference("food")
        orderReference = FirebaseDatabase.getInstance().getReference("orders")

        val foodList = mutableListOf<String>()
        val foodQuantityList = mutableListOf<Any>()
        val foodNameList = mutableListOf<String>()
        val foodPriceList = mutableListOf<String>()

        orderReference.child(FirebaseAuth.getInstance().currentUser?.uid!!)
            .child(intent.getStringExtra("orderID")!!)
            .addValueEventListener(object : ValueEventListener {
                override fun onCancelled(p0: DatabaseError) {

                }

                override fun onDataChange(p0: DataSnapshot) {

                    var tax = 0.0
                    var item_total = 0.0


                    order_no.text = p0.key
                    for (p: DataSnapshot in p0.children) {

                        try{

                            when (p.key) {
                                "phone" -> phone.text = p.value.toString().substringAfter(" ")
                                "tax" -> {

                                    tax = p.value.toString().toDouble()
                                    tax_order_detail.text = tax.toString()
                                }
                                "total_cost" -> {

                                    item_total = p.value.toString().toDouble()
                                    item_total_order_detail.text = item_total.toString()
                                }
                                "delivery_address" -> delivery_address.text = p.value.toString()
                                "date" -> date_time.text = p.value.toString()
                                "res_name" -> res_name_order_detail.text = p.value.toString()
                                "res_address" -> res_address_order_detail.text = p.value.toString()
                                else -> {

                                    foodList.add(p.key!!)
                                    foodQuantityList.add(p.value!!)
                                }
                            }
                            total_order_detail.text = (tax + item_total).toString()
                        }
                        catch (e: Exception){

                            Toast.makeText(this@OrderDetailActivity,e.message,Toast.LENGTH_SHORT).show()
                        }


                    }

                    orderDetailListAdapter.notifyDataSetChanged()
                }


            })


        foodReference.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {

            }

            override fun onDataChange(p0: DataSnapshot) {

                for (p: DataSnapshot in p0.children) {

                    if (p.key in foodList) {

                        foodNameList.add(p.child("name").value.toString())
                        foodPriceList.add(p.child("price").value.toString())
                    }
                    orderDetailListAdapter.notifyDataSetChanged()
                }



            }


        })




        orderDetailListAdapter =
            OrderDetailListAdapter(this, foodNameList, foodQuantityList, foodPriceList)
        orders_list.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        orders_list.adapter = orderDetailListAdapter

    }

}
