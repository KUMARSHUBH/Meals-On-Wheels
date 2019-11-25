package com.krshubham.mealsonwheels.adapter

//import android.content.Context
//import android.view.LayoutInflater
//import android.view.View
//import android.view.ViewGroup
//import androidx.recyclerview.widget.LinearLayoutManager
//import androidx.recyclerview.widget.RecyclerView
//import com.krshubham.mealsonwheels.R
//import com.krshubham.mealsonwheels.models.Category
//import com.krshubham.mealsonwheels.models.Restaurant
//import com.krshubham.mealsonwheels.ui.fragments.Food.FoodFragment
//
//
//class MainAdapter(
//    val context: Context,
//    var items: List<Any>
//) : RecyclerView.Adapter<BaseViewHolder<*>>() {
//
//    companion object {
//
//        private const val VERTICAL = 1
//        private const val HORIZONTAL = 2
//    }
//
//
//    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder<*> {
//
//        val inflater = LayoutInflater.from(context)
//        val view: View
//        val holder: BaseViewHolder<*>
//        when (viewType) {
//            VERTICAL -> {
//                view = inflater.inflate(R.layout.restaurant, parent, false)
//                holder = RestaurantViewAdapter.RestaurantViewHolder(view)
//            }
////            HORIZONTAL -> {
////
////                view = inflater.inflate(R.layout.category_layout, parent, false)
////                holder = CategoryViewAdapter.CategoryViewHolder(view)
////            }
//            else -> {
//                view = inflater.inflate(R.layout.category_layout, parent, false)
//                holder = CategoryViewAdapter.CategoryViewHolder(view)
//            }
//        }
//
//        return holder
//    }
//
//    override fun getItemCount() = items.size
//
////    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
////
////        var element = items[position]
////        if(holder.itemViewType == VERTICAL)
////            restaurantView(holder as RestaurantViewHolder)
////
////        else if(holder.itemViewType == HORIZONTAL)
////                categoryView(holder as CategoryViewHolder)
////
////    }
//
//
////    private fun categoryView(holder: CategoryViewHolder) {
////        val adapter = CategoryViewAdapter(context, FoodFragment.categoryList)
////        holder.recyclerView1.layoutManager = LinearLayoutManager(
////            context,
////            LinearLayoutManager.HORIZONTAL,
////            false
////        )
////        holder.recyclerView1.adapter = adapter
////    }
////
////    private fun restaurantView(holder: RestaurantViewHolder) {
////        val adapter1 = RestaurantViewAdapter(context, FoodFragment.restaurantlist)
////        holder.recyclerView2.layoutManager =
////            LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
////        holder.recyclerView2.adapter = adapter1
////    }
//
//
//    override fun getItemViewType(position: Int): Int {
//        if (items[position] is Restaurant)
//            return VERTICAL
//        if (items[position] is Category)
//            return HORIZONTAL
//
//        return -1
//    }
//
////
////    class CategoryViewHolder(itemView: View) :
////        RecyclerView.ViewHolder(itemView) {
////        var recyclerView1: RecyclerView = itemView.horizontal_recyclerView
////
////    }
////
////    class RestaurantViewHolder(itemView: View) :
////        RecyclerView.ViewHolder(itemView) {
////
////        var recyclerView2: RecyclerView = itemView.vertical_recyclerView
////
////    }
//
//
//
//    override fun onBindViewHolder(holder: BaseViewHolder<*>, position: Int) {
//
//        when (holder){
//
//            is RestaurantViewAdapter.RestaurantViewHolder ->{
//                holder.bind(items[position] as Restaurant)
//            }
////            is CategoryViewAdapter.CategoryViewHolder -> holder.bind(items[position] as Category)
////            else -> throw IllegalArgumentException()
//
//        }
//
//    }
//
//}
//
//
