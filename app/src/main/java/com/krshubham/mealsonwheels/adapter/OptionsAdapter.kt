package com.krshubham.mealsonwheels.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.krshubham.mealsonwheels.R
import kotlinx.android.synthetic.main.option_item.view.*

class OptionsAdapter(val context: Context,
                     val options: List<String>) : RecyclerView.Adapter<OptionsAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view){

        var title = view.option

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {


        val view =
            LayoutInflater.from(context).inflate(R.layout.option_item, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount() = options.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        holder.title.text = options[position]
    }
}