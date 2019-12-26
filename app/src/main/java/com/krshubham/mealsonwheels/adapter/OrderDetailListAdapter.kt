package com.krshubham.mealsonwheels.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.krshubham.mealsonwheels.R
import kotlinx.android.synthetic.main.order_detail_item.view.*

class OrderDetailListAdapter(
    val context: Context,
    val foodNamelist: List<String>,
    val foodQuantityList: List<Any>,
    val foodPricelist: List<String>
) : RecyclerView.Adapter<OrderDetailListAdapter.OrderDetailViewHolder>() {

    class OrderDetailViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        val foodName: TextView? = view.order_detail_res_name
        val foodQuantity: TextView? = view.food_item_quantity
        val perItemCost: TextView? = view.per_item_cost_order_detail
        val totalItemCost: TextView? = view.order_detail_total_price
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrderDetailViewHolder {

        val view = LayoutInflater.from(context).inflate(R.layout.order_detail_item, parent, false)
        return OrderDetailViewHolder(view)
    }

    override fun getItemCount() = foodNamelist.size

    override fun onBindViewHolder(holder: OrderDetailViewHolder, position: Int) {

        holder.foodQuantity?.clipToOutline = true
        holder.foodName?.text = foodNamelist[position]
        holder.foodQuantity?.text = foodQuantityList[position].toString()
        holder.perItemCost?.text = foodPricelist[position]
        try {

            holder.totalItemCost?.text = (foodPricelist[position].toDouble() * foodQuantityList[position].toString().toDouble()).toString()
        }
        catch (e: Exception){

            Toast.makeText(context,e.message, Toast.LENGTH_SHORT).show()
        }


    }
}