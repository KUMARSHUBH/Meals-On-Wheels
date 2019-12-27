package com.krshubham.mealsonwheels.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.krshubham.mealsonwheels.R
import com.krshubham.mealsonwheels.models.Category
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.square_category_layout.view.*

class CategoryViewAdapter(
    val context: Context,
    private val categoryList: List<Category>?
) : RecyclerView.Adapter<CategoryViewAdapter.CategoryViewHolder>() {


    class CategoryViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        var catName = view.category_big_name
        var catImage = view.category_big_image
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryViewHolder {

        val view =
            LayoutInflater.from(context).inflate(R.layout.square_category_layout, parent, false)
        return CategoryViewHolder(view)
    }

    override fun getItemCount() = categoryList?.size ?: 0


    override fun onBindViewHolder(holder: CategoryViewHolder, position: Int) {

        holder.catName.text = categoryList?.get(position)?.name
        Picasso.get().load(categoryList?.get(position)?.image).placeholder(R.drawable.food_background).into(holder.catImage)
        holder.catImage.clipToOutline= true
    }
}