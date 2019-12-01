package com.krshubham.mealsonwheels.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.krshubham.mealsonwheels.R
import com.krshubham.mealsonwheels.models.Food
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.food_item_layout.view.*

class FoodAdapter(val context: Context, val foodList: List<Food>?) : RecyclerView.Adapter<FoodAdapter.ViewHolder>() {


    class ViewHolder(view: View) : RecyclerView.ViewHolder(view){

        var name: TextView? = view.food_title
        var price: TextView? = view.food_price
        var rating: TextView? = view.food_rating
        var image: ImageView? = view.food_image
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {


        val view =
            LayoutInflater.from(context).inflate(R.layout.food_item_layout, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount() = foodList?.size ?: 0

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        Picasso.get().load(foodList?.get(position)?.image).into(holder.image)
        holder.image?.clipToOutline = true
        holder.name?.text =foodList?.get(position)?.name
        val a = "$ ${foodList?.get(position)?.price}"
        holder.price?.text = a
        holder.rating?.text = foodList?.get(position)?.price
    }
}