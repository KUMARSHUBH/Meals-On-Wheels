package com.krshubham.mealsonwheels.ui.fragments.Food

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.krshubham.mealsonwheels.R.layout.fragment_food
import com.krshubham.mealsonwheels.adapter.BestRestaurantAdapter
import com.krshubham.mealsonwheels.adapter.CategoryViewAdapter
import com.krshubham.mealsonwheels.adapter.OptionsAdapter
import com.krshubham.mealsonwheels.adapter.RestaurantViewAdapter
import com.krshubham.mealsonwheels.models.Category
import com.krshubham.mealsonwheels.models.Restaurant
import kotlinx.android.synthetic.main.fragment_food.*

class FoodFragment : Fragment() {

    private lateinit var foodViewModel: FoodViewModel

    private lateinit var restaurantViewAdapter: RestaurantViewAdapter
    private lateinit var categoryViewAdapter: CategoryViewAdapter
    private lateinit var bestRestaurantAdapter: BestRestaurantAdapter
    private lateinit var optionsAdapter: OptionsAdapter

    companion object {

        lateinit var restaurantlist: ArrayList<Restaurant>
        lateinit var categoryList: ArrayList<Category>
        lateinit var optionsList: List<String>

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(fragment_food, container, false)


        val restaurantReference = FirebaseDatabase.getInstance().getReference("restaurant")
        val categoryReference = FirebaseDatabase.getInstance().getReference("category")

        categoryList = ArrayList()
        restaurantlist = ArrayList()
        optionsList = listOf("Filters","Sort By","Pure Veg Places","Rating: 4.0+", "Great Offers", "Express Delivery")


        restaurantReference.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {

            }

            override fun onDataChange(p0: DataSnapshot) {


                for (ds: DataSnapshot in p0.children) {

                    val restaurant = Restaurant()
                    restaurant.apply {

                        name = ds.child("name").getValue(true).toString()
                        rating = ds.child("rating").getValue(true).toString()
                        image = ds.child("image").getValue(true).toString()

                    }
                    restaurantlist.add(restaurant)

                }

                restaurantViewAdapter.notifyDataSetChanged()
                bestRestaurantAdapter.notifyDataSetChanged()

            }


        })

        categoryReference.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {

            }

            override fun onDataChange(p0: DataSnapshot) {


                for (ds: DataSnapshot in p0.children) {

                    val category = Category()
                    category.apply {

                        name = ds.child("name").getValue(true).toString()
                        image = ds.child("image").getValue(true).toString()

                    }
                    categoryList.add(category)
                }

            }


        })

        foodViewModel =
            ViewModelProviders.of(this).get(FoodViewModel::class.java)
        return view


    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        best_restaurant_recycler_view.layoutManager =
            LinearLayoutManager(this.context, LinearLayoutManager.HORIZONTAL, false)
        best_restaurant_recycler_view.setHasFixedSize(true)

        restaurant_recycler_view.layoutManager =
            LinearLayoutManager(this.context, LinearLayoutManager.VERTICAL, false)
        restaurant_recycler_view.setHasFixedSize(true)

        option_recycler_view.layoutManager =
            LinearLayoutManager(this.context, LinearLayoutManager.HORIZONTAL, false)
        option_recycler_view.setHasFixedSize(true)

        restaurantViewAdapter = RestaurantViewAdapter(this.context!!, restaurantlist, categoryList)
        categoryViewAdapter = CategoryViewAdapter(this.context!!, categoryList)
        bestRestaurantAdapter = BestRestaurantAdapter(this.context!!, restaurantlist)
        optionsAdapter = OptionsAdapter(this.context!!, optionsList)

        restaurant_recycler_view.adapter = restaurantViewAdapter
        best_restaurant_recycler_view.adapter = bestRestaurantAdapter
        option_recycler_view.adapter = optionsAdapter

        optionsAdapter.notifyDataSetChanged()

        restaurant_recycler_view.isNestedScrollingEnabled = true


    }
}
