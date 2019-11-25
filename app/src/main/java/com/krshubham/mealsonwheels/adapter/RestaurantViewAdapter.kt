package com.krshubham.mealsonwheels.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.krshubham.mealsonwheels.R
import com.krshubham.mealsonwheels.models.Restaurant
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.restaurant_layout.view.*

class RestaurantViewAdapter(
    private val context: Context,
    private val restaurantList: List<Restaurant>?
) : RecyclerView.Adapter<RestaurantViewAdapter.ViewHolder>() {


    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {

            var resName: TextView = view.restaurant_name
            var resImage: ImageView = view.restaurant_image
            //        var resCategories: TextView
            var resPerPersonCost: TextView = view.restaurant_per_person_cost
            var resRating: TextView = view.rating
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        val view =  LayoutInflater.from(context).inflate(R.layout.restaurant_layout,parent,false)
        return ViewHolder(view)
    }

    override fun getItemCount() = restaurantList?.size ?: 0

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        holder.resName.text = restaurantList?.get(position)?.name.toString()
        holder.resRating.text = restaurantList?.get(position)?.rating.toString()
        Picasso.get().load(restaurantList?.get(position)?.image.toString()).into(holder.resImage)
    }

}

