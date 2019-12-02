package com.krshubham.mealsonwheels.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.krshubham.mealsonwheels.R
import com.krshubham.mealsonwheels.adapter.FoodListAdapter
import com.krshubham.mealsonwheels.models.Food
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_restaurant_detail.*

class RestaurantDetailActivity : AppCompatActivity() {

    //    private lateinit var foodAdapter: FoodAdapter
    private lateinit var foodListAdapter: FoodListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_restaurant_detail)

        val id = intent.getStringExtra("id")
        val resName = intent.getStringExtra("name")
        val resRating = intent.getStringExtra("rating")
        val resPhone = intent.getStringExtra("phone")
        val resImage = intent.getStringExtra("image")

        val foods = ArrayList<Any>()
        name_detail.text = resName
        rating_detail.text = resRating

        Picasso.get().load(resImage).into(toolbar_background)

//        foodAdapter = FoodAdapter(this, foods)
        foodListAdapter = FoodListAdapter(this, foods)
        restaurant_detail_recycler_view.adapter = foodListAdapter
        restaurant_detail_recycler_view.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)


//        foodAdapter.notifyDataSetChanged()

        val resCategoryReference =
            FirebaseDatabase.getInstance().getReference("restaurantCategories")
        val categoryReference = FirebaseDatabase.getInstance().getReference("category")
        val foodReference = FirebaseDatabase.getInstance().getReference("food")

        val categoryList = ArrayList<String>()
        val foodList = ArrayList<String>()


        resCategoryReference.child(id!!).addValueEventListener(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {

            }

            override fun onDataChange(p0: DataSnapshot) {

                for (p: DataSnapshot in p0.children) {

                    categoryList.add(p.key!!)
                }

                categoryReference.addValueEventListener(object : ValueEventListener {
                    override fun onCancelled(p0: DatabaseError) {

                    }

                    override fun onDataChange(p0: DataSnapshot) {

                        for (p: DataSnapshot in p0.children) {

                            if (p.key!! in categoryList) {

                                for (p1: DataSnapshot in p.children) {

//                                    if(p1.key!! == "name")
//                                        foods.add(p.child("name").value.toString())

//                                    foodListAdapter.notifyDataSetChanged()

                                    if (p1.key!! != "aaaaa" || p1.key!! != "image") {

                                        foodReference.addListenerForSingleValueEvent(object :
                                            ValueEventListener {
                                            override fun onCancelled(p0: DatabaseError) {

                                            }

                                            override fun onDataChange(p0: DataSnapshot) {

                                                if(p1.key!! == "aaaaa")
                                                    foods.add(p.child("aaaaa").value.toString())
                                                for (p2: DataSnapshot in p0.children) {

                                                    if (p2.key == p1.key) {
                                                        val food = Food()
                                                        food.apply {

                                                            name = p2.child("name").getValue(true)
                                                                .toString()
                                                            image = p2.child("image").getValue(true)
                                                                .toString()
                                                            price = p2.child("price").getValue(true)
                                                                .toString()
                                                            rating =
                                                                p2.child("rating").getValue(true)
                                                                    .toString()
                                                        }


                                                        foods.add(food)
                                                        foodListAdapter.notifyDataSetChanged()

                                                    }
                                                }



                                            }
                                        })
                                    }
                                }
                            }

                        }

//                        foodListAdapter.notifyDataSetChanged()

                    }


                })


            }


        })


    }

}
