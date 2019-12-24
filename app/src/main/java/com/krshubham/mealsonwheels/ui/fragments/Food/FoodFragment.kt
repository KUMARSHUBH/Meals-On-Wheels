package com.krshubham.mealsonwheels.ui.fragments.Food

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.database.*
import com.krshubham.mealsonwheels.R
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
    private lateinit var orderSharedPreferences: SharedPreferences
    private lateinit var restaurantReference: DatabaseReference
    private lateinit var snackbar: Snackbar

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
        orderSharedPreferences = context?.getSharedPreferences("OrderRestId", Context.MODE_PRIVATE)!!

        categoryList = ArrayList()
        restaurantlist = ArrayList()
        optionsList = listOf(
            "Filters",
            "Sort By",
            "Pure Veg Places",
            "Rating: 4.0+",
            "Great Offers",
            "Express Delivery"
        )


        restaurantReference.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {

            }

            override fun onDataChange(p0: DataSnapshot) {


                for (ds: DataSnapshot in p0.children) {

                    val restaurant = Restaurant()
                    restaurant.apply {

                        id = ds.child("id").getValue(true).toString()
                        name = ds.child("name").getValue(true).toString()
                        rating = ds.child("rating").getValue(true).toString()
                        image = ds.child("image").getValue(true).toString()
                        phone = ds.child("phone").getValue(true).toString()

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

                        name = ds.child("aaaaa").getValue(true).toString()
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



    override fun onResume() {
        super.onResume()

        val a = orderSharedPreferences.getString("resId", "")
        if (a != null) {

            if(a != ""){
                var res: String
                restaurantReference = FirebaseDatabase.getInstance().getReference("restaurant/${a}")
                restaurantReference.addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onCancelled(p0: DatabaseError) {

                    }

                    override fun onDataChange(p0: DataSnapshot) {

                        res = p0.child("name").value.toString()
                        snackbar = Snackbar.make(test, res, Snackbar.LENGTH_INDEFINITE)
                        snackbar.apply {
                            view.layoutParams =
                                (view.layoutParams as CoordinatorLayout.LayoutParams).apply {
                                    setMargins(
                                        leftMargin,
                                        topMargin,
                                        rightMargin,
                                        bottomMargin
                                    )

                                }
                        }
                            .setActionTextColor(resources.getColor(android.R.color.white))

                            .setAction("View") {
                                Navigation.findNavController(it).navigate(R.id.action_navigation_food_to_navigation_cart)
                            }
                            .show()
                    }


                })

            }

        }

        try {
            snackbar.dismiss()
        }
        catch (e: Exception){

        }

    }
}
