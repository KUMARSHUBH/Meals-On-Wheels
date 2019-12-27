package com.krshubham.mealsonwheels.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.krshubham.mealsonwheels.OrderDetailActivity
import com.krshubham.mealsonwheels.R
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.order_list_item.view.*

class OrderListAdapter(val context: Context, val ordersList: List<String>) :
    RecyclerView.Adapter<OrderListAdapter.OrderListViewHolder>() {

    lateinit var databaseReference: DatabaseReference
    lateinit var resReference: DatabaseReference

    class OrderListViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        var image = view.order_item_image
        var name = view.order_item_name
        var date = view.order_item_date
        var amount = view.order_item_amount
        val orderCard = view.order_card
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrderListViewHolder {

        databaseReference = FirebaseDatabase.getInstance()
            .getReference("orders/${FirebaseAuth.getInstance().currentUser?.uid}")

        resReference = FirebaseDatabase.getInstance().getReference("restaurant")
        val view =
            LayoutInflater.from(context).inflate(R.layout.order_list_item, parent, false)
        return OrderListViewHolder(view)
    }

    override fun getItemCount() = ordersList.size

    override fun onBindViewHolder(holder: OrderListViewHolder, position: Int) {

        databaseReference.child(ordersList[position])
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onCancelled(p0: DatabaseError) {

                }

                override fun onDataChange(p0: DataSnapshot) {


                    holder.orderCard.setOnClickListener {

                        val intent = Intent(context, OrderDetailActivity::class.java)
                        intent.putExtra("orderID", ordersList[position])
                        context.startActivity(intent)
                    }

                    holder.date.text = p0.child("date").value.toString()
                    holder.amount.text =
                        (p0.child("tax").value.toString().toDouble() + p0.child("total_cost").value.toString().toDouble()).toString()
                    holder.name.text = p0.child("res_name").value.toString()

                    holder.image.clipToOutline = true
                    resReference.child(p0.child("res_id").value.toString())
                        .addListenerForSingleValueEvent(object : ValueEventListener {
                            override fun onCancelled(p0: DatabaseError) {

                            }

                            override fun onDataChange(p0: DataSnapshot) {

                                Picasso.get().load(p0.child("image").value.toString())
                                    .placeholder(R.drawable.food_background).into(holder.image)
                            }


                        })

                }


            })


    }
}