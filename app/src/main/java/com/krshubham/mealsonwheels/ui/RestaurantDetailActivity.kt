package com.krshubham.mealsonwheels.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.krshubham.mealsonwheels.R
import com.krshubham.mealsonwheels.adapter.FoodAdapter
import com.krshubham.mealsonwheels.models.Food
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_restaurant_detail.*

class RestaurantDetailActivity : AppCompatActivity() {

    private lateinit var foodAdapter: FoodAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_restaurant_detail)

        val id = intent.getStringExtra("id")
        val resName = intent.getStringExtra("name")
        val resRating = intent.getStringExtra("rating")
        val resPhone = intent.getStringExtra("phone")
        val resImage = intent.getStringExtra("image")

        val foods = ArrayList<Food>()
        name_detail.text = resName
        rating_detail.text = resRating

        Picasso.get().load(resImage).into(toolbar_background)

        foodAdapter = FoodAdapter(this, foods)
        restaurant_detail_recycler_view.adapter = foodAdapter
        restaurant_detail_recycler_view.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)


        foodAdapter.notifyDataSetChanged()

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

                                    if (p1.key!! != "name" || p1.key!! != "image") {

                                        foodList.add(p1.key!!)
                                    }
                                }
                            }
                        }

                        foodReference.addValueEventListener(object : ValueEventListener {
                            override fun onCancelled(p0: DatabaseError) {

                            }

                            override fun onDataChange(p0: DataSnapshot) {

                                for (f: DataSnapshot in p0.children) {

                                    if (f.key!! in foodList) {
                                        val food = Food()
                                        food.apply {

                                            name = f.child("name").getValue(true).toString()
                                            image = f.child("image").getValue(true).toString()
                                            price = f.child("price").getValue(true).toString()
                                            rating = f.child("rating").getValue(true).toString()

                                        }

                                        foods.add(food)
                                    }
                                }

                                foodAdapter.notifyDataSetChanged()
                            }

                        })

                    }

                })



            }


        })





//        foodReference.addValueEventListener(object : ValueEventListener {
//            override fun onCancelled(p0: DatabaseError) {
//
//            }
//
//            override fun onDataChange(p0: DataSnapshot) {
//
//                for (f: DataSnapshot in p0.children) {
//
//                    val food = Food()
//                    food.apply {
//
//                        name = f.child("name").getValue(true).toString()
//                        image = f.child("image").getValue(true).toString()
//                        price = f.child("price").getValue(true).toString()
//                        rating = f.child("rating").getValue(true).toString()
//
//                    }
//
//                    foods.add(food)
//
//
//                }
//
//                foodAdapter.notifyDataSetChanged()
//            }
//
//        })


    }


}
