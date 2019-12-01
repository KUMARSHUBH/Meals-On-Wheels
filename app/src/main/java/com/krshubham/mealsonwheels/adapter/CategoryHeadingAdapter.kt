package com.krshubham.mealsonwheels.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.krshubham.mealsonwheels.R
import com.krshubham.mealsonwheels.models.Category
import kotlinx.android.synthetic.main.category_heading_layout.view.*

class CategoryHeadingAdapter(val context: Context, val categoryList: List<Category>?) : RecyclerView.Adapter<CategoryHeadingAdapter.ViewHolder>(){

    class ViewHolder(view: View): RecyclerView.ViewHolder(view){

        var name: TextView? = view.category_heading
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {


        val view =
            LayoutInflater.from(context).inflate(R.layout.category_heading_layout, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount()  = categoryList?.size ?: 0

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        holder.name?.text = categoryList?.get(position)?.name.toString()
    }
}