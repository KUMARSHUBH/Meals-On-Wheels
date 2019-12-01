package com.krshubham.mealsonwheels.adapter

//import android.content.Context
//import android.view.View
//import android.view.ViewGroup
//import android.widget.ImageView
//import android.widget.TextView
//import androidx.recyclerview.widget.RecyclerView
//import com.krshubham.mealsonwheels.models.Category
//import com.krshubham.mealsonwheels.models.Food
//import kotlinx.android.synthetic.main.category_heading_layout.view.*
//import kotlinx.android.synthetic.main.food_item_layout.view.*
//
//class FoodListAdapter(val context: Context, val categoryList: List<Category>, val foodList: List<Food>): RecyclerView.Adapter<FoodListAdapter.ViewHolder>() {
//
//
//    private object ItemType {
//
//        val category = 1
//        val food = 2
//    }
//
//    class ViewHolder(view: View) : RecyclerView.ViewHolder(view){
//
//        var categoryName: TextView? = view.category_heading
//        var name: TextView? = view.food_title
//        var price: TextView? = view.food_price
//        var rating: TextView? = view.food_rating
//        var image: ImageView? = view.food_image
//
//    }
//
//    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
//
//    }
//
//    override fun getItemCount(): Int {
//
//    }
//
//    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
//
//    }
//
//    override fun getItemViewType(position: Int): Int {
//
//        return
//    }
//}