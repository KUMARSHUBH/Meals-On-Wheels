package com.krshubham.mealsonwheels

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.krshubham.mealsonwheels.db.CartItem
import kotlinx.android.synthetic.main.cart_item_layout.view.*


class CartListAdapter(val context: Context, val items : List<CartItem>): RecyclerView.Adapter<CartListAdapter.ViewHolder>(){


    class ViewHolder(view: View) : RecyclerView.ViewHolder(view){

        var name:TextView? = view.food_name
        var price: TextView? = view.food_price
        var totalPrice: TextView? = view.total_price_per_item
        var addItemCart: ImageView? = view.add_item_cart
        var removeItemCart: ImageView? = view.remove_item_cart
        var itemCountCart: TextView? = view.item_count_cart
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {


        val view =
            LayoutInflater.from(context).inflate(R.layout.cart_item_layout, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount() = items.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        holder.name?.text = items[position].name
        holder.price?.text = items[position].price.toString()
        holder.totalPrice?.text = ((items[position].price) * (items[position].quantity)).toString()
        holder.itemCountCart?.text = items[position].quantity.toString()

        holder.addItemCart?.setOnClickListener {

        }

        holder.removeItemCart?.setOnClickListener {

        }

    }
}