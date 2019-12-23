package com.krshubham.mealsonwheels.ui

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.krshubham.mealsonwheels.BottomSheetFragment
import com.krshubham.mealsonwheels.R
import com.krshubham.mealsonwheels.adapter.FoodListAdapter
import com.krshubham.mealsonwheels.db.CartDataSource
import com.krshubham.mealsonwheels.db.CartDataSourceImpl
import com.krshubham.mealsonwheels.db.CartDatabase
import com.krshubham.mealsonwheels.models.Food
import com.squareup.picasso.Picasso
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_restaurant_detail.*

class RestaurantDetailActivity : AppCompatActivity() {

    private lateinit var foodListAdapter: FoodListAdapter

    lateinit var compositeDisposable: CompositeDisposable
    lateinit var cartDataSource: CartDataSource


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_restaurant_detail)


        val id = intent.getStringExtra("id")
        val resName = intent.getStringExtra("name")
        val resRating = intent.getStringExtra("rating")
        val resPhone = intent.getStringExtra("phone")
        val resImage = intent.getStringExtra("image")

        compositeDisposable = CompositeDisposable()
        cartDataSource = CartDataSourceImpl(CartDatabase.getInstance(this).cartDao())

        compositeDisposable.add(cartDataSource.getAllCartItems()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe {

                if (it == null || it.isEmpty()) {

                    bottom_app_bar.visibility = View.GONE
                } else {

                    var items = 0
                    var price = 0.0
                    bottom_app_bar.visibility = View.VISIBLE
                    it.forEach { cartItem ->

                        items += cartItem.quantity
                        price += (cartItem.price * cartItem.quantity)
                    }

                    cart_items.text = "$items ITEMS"
                    cart_price.text = "Rs. $price plus taxes"
                }
            })


        val foods = ArrayList<Any>()
        name_detail.text = resName
        rating_detail.text = resRating

        view_cart.setOnClickListener {

            val bottomSheetFragment = BottomSheetFragment()
            bottomSheetFragment.show(supportFragmentManager, bottomSheetFragment.tag)

        }

        Picasso.get().load(resImage).into(toolbar_background)

        foodListAdapter = FoodListAdapter(this, foods)

        restaurant_detail_recycler_view.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)


        val resCategoryReference =
            FirebaseDatabase.getInstance().getReference("restaurantCategories")
        val categoryReference = FirebaseDatabase.getInstance().getReference("category")
        val foodReference = FirebaseDatabase.getInstance().getReference("food")

        val categoryList = ArrayList<String>()

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

                                    if (p1.key!! != "aaaaa" || p1.key!! != "image") {

                                        foodReference.addListenerForSingleValueEvent(object :
                                            ValueEventListener {
                                            override fun onCancelled(p0: DatabaseError) {

                                            }

                                            override fun onDataChange(p0: DataSnapshot) {

                                                if (p1.key!! == "aaaaa")
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
                                                            foodId =
                                                                p2.child("foodId").getValue(true)
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


                    }


                })

            }
        })

    }

    override fun onResume() {
        super.onResume()

        restaurant_detail_recycler_view.adapter = foodListAdapter


    }

    override fun onStop() {

        foodListAdapter.onStop()
        compositeDisposable.clear()
        super.onStop()
    }


}
