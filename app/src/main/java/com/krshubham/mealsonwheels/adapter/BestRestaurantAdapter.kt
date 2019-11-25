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
import kotlinx.android.synthetic.main.square_restaurant_layout.view.*

class BestRestaurantAdapter(val context: Context,
                            private val bestRestaurantList: List<Restaurant>?) : RecyclerView.Adapter<BestRestaurantAdapter.ViewHolder>() {


    class ViewHolder(view: View) : RecyclerView.ViewHolder(view){


        var image: ImageView = view.restaurant_big_image
        var name: TextView = view.restaurant_big_name
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        val view =  LayoutInflater.from(context).inflate(R.layout.square_restaurant_layout,parent,false)
        return ViewHolder(view)
    }

    override fun getItemCount() = bestRestaurantList?.size ?: 0

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        Picasso.get().load(bestRestaurantList?.get(position)?.image).into(holder.image)
        holder.name.text = bestRestaurantList?.get(position)?.name
    }
}