package com.krshubham.mealsonwheels.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.krshubham.mealsonwheels.R
import com.krshubham.mealsonwheels.models.Category
import com.krshubham.mealsonwheels.models.Restaurant
import com.krshubham.mealsonwheels.ui.RestaurantDetailActivity
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.restaurant_layout.view.*
import kotlinx.android.synthetic.main.text_layout.view.*
import kotlin.random.Random

class RestaurantViewAdapter(
    private val context: Context,
    private val restaurantList: List<Restaurant>?,
    private val categoryList: List<Category>?
) : RecyclerView.Adapter<RestaurantViewAdapter.ViewHolder>() {

    val pos = Random.nextInt(4, 8)
    var resAddress: String = ""

    private object ItemType {

        val restaurant = 1
        val input = 2
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        var resName: TextView? = view.restaurant_name
        var resImage: ImageView? = view.restaurant_image
        //        var resCategories: TextView
        var resPerPersonCost: TextView? = view.restaurant_per_person_cost
        var resRating: TextView? = view.rating
        var cat: RecyclerView? = view.category_recycler_view
        var root: ConstraintLayout? = view.root
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        val view = LayoutInflater.from(context).inflate(
            when (viewType) {

                ItemType.restaurant -> R.layout.restaurant_layout
                ItemType.input -> R.layout.text_layout
                else -> error("Unknown view type: $viewType")
            }
            , parent, false
        )
        return ViewHolder(view)
    }

    override fun getItemCount() = (restaurantList?.size) ?: 0

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        if (position != pos) {

            holder.resName?.text = restaurantList?.get(position)?.name.toString()
            holder.resRating?.text = restaurantList?.get(position)?.rating.toString()
            Picasso.get().load(restaurantList?.get(position)?.image.toString())
                .placeholder(R.drawable.food_background)
                .into(holder.resImage)
            holder.resImage?.clipToOutline = true
            holder.cat = null


            holder.root?.setOnClickListener {

                val intent = Intent(context, RestaurantDetailActivity::class.java)
                intent.putExtra("id", restaurantList?.get(position)?.id.toString())
                intent.putExtra("name", restaurantList?.get(position)?.name.toString())
                intent.putExtra("phone", restaurantList?.get(position)?.phone.toString())
                intent.putExtra("image", restaurantList?.get(position)?.image.toString())
                intent.putExtra("rating", restaurantList?.get(position)?.rating.toString())
                intent.putExtra("resAddress", restaurantList?.get(position)?.address.toString())
                context.startActivity(intent)
            }
        } else {

            holder.cat?.adapter = CategoryViewAdapter(context, categoryList)
            holder.cat?.layoutManager =
                LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        }

    }

    override fun getItemViewType(position: Int): Int {

        return if (position == pos) ItemType.input else ItemType.restaurant
    }


}

