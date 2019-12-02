package com.krshubham.mealsonwheels.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RatingBar
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.krshubham.mealsonwheels.R
import com.krshubham.mealsonwheels.models.Food
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.category_heading_layout.view.*
import kotlinx.android.synthetic.main.food_item_layout.view.*

class FoodListAdapter(val context: Context, val list: List<Any>?) :
    RecyclerView.Adapter<FoodListAdapter.ViewHolder>() {

    private object ItemType {

        val category = 1
        val food = 2
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        var categoryName: TextView? = view.category_heading
        var name: TextView? = view.food_title
        var price: TextView? = view.food_price
        var rating: RatingBar? = view.food_rating
        var image: ImageView? = view.food_image

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        val view = LayoutInflater.from(context).inflate(
            when (viewType) {

                ItemType.category -> R.layout.category_heading_layout
                ItemType.food -> R.layout.food_item_layout
                else -> error("Unknown view type: $viewType")
            }
            , parent, false
        )
        return ViewHolder(view)

    }

    override fun getItemCount() = list?.size ?: 0

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        if (getItemViewType(position) == ItemType.food) {

            Picasso.get().load((list?.get(position) as Food).image).into(holder.image)
            holder.name?.text = (list[position] as Food).name
            holder.rating?.rating = (list[position] as Food).rating.toFloat()
            holder.price?.text = (list[position] as Food).price
        }

        else if(getItemViewType(position) == ItemType.category){

            holder.categoryName?.text = list?.get(position).toString()
        }
    }

    override fun getItemViewType(position: Int): Int {

        return if (list?.get(position) is Food) {

            ItemType.food

        } else ItemType.category
    }
}