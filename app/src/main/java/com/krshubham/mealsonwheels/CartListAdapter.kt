package com.krshubham.mealsonwheels

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.krshubham.mealsonwheels.db.CartItem
import kotlinx.android.synthetic.main.cart_item_layout.view.*

class CartListAdapter(val context: Context, val items : List<CartItem>): RecyclerView.Adapter<CartListAdapter.ViewHolder>(){


    class ViewHolder(view: View) : RecyclerView.ViewHolder(view){

        var name: TextView? = view.name
        var quantity: TextView? = view.quantity
        var price: TextView? = view.price
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {


        val view =
            LayoutInflater.from(context).inflate(R.layout.cart_item_layout, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount() = items.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        holder.name?.text = items[position].name
        holder.price?.text= items[position].price.toString()
        holder.quantity?.text = items[position].quantity.toString()
    }
}