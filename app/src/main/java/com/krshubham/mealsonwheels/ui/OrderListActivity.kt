package com.krshubham.mealsonwheels.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.krshubham.mealsonwheels.R
import com.krshubham.mealsonwheels.adapter.OrderListAdapter
import kotlinx.android.synthetic.main.activity_order_list.*

class OrderListActivity : AppCompatActivity() {

    private lateinit var adapter: OrderListAdapter
    private lateinit var orderRef: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTheme(R.style.AppTheme_OrderTrackingActivity)
        setContentView(R.layout.activity_order_list)

        val orderList = mutableListOf<String>()
        orderRef = FirebaseDatabase.getInstance().getReference("orders/${FirebaseAuth.getInstance().currentUser?.uid}")

        orderRef.addListenerForSingleValueEvent(object : ValueEventListener{
            override fun onCancelled(p0: DatabaseError) {

            }

            override fun onDataChange(p0: DataSnapshot) {

                for(p: DataSnapshot in p0.children){

                    orderList.add(p.key.toString())
                }

                adapter.notifyDataSetChanged()

            }

        })

        order_list_recycler_view.layoutManager  = LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false)
        adapter = OrderListAdapter(this,orderList)
        order_list_recycler_view.adapter = adapter


    }
}
