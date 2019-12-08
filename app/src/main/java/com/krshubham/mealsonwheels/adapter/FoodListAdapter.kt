package com.krshubham.mealsonwheels.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import com.krshubham.mealsonwheels.R
import com.krshubham.mealsonwheels.db.CartDataSource
import com.krshubham.mealsonwheels.db.CartDataSourceImpl
import com.krshubham.mealsonwheels.db.CartDatabase
import com.krshubham.mealsonwheels.db.CartItem
import com.krshubham.mealsonwheels.models.Food
import com.squareup.picasso.Picasso
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.category_heading_layout.view.*
import kotlinx.android.synthetic.main.food_item_layout.view.*

class FoodListAdapter(val context: Context, val list: List<Any>?) :
    RecyclerView.Adapter<FoodListAdapter.ViewHolder>() {

    object ItemType {

        const val category = 1
        const val food = 2
    }

    private val compositeDisposable: CompositeDisposable = CompositeDisposable()
    private val cartDataSource: CartDataSource

    init {

        cartDataSource = CartDataSourceImpl(CartDatabase.getInstance(this.context).cartDao())
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        var categoryName: TextView? = view.category_heading
        var name: TextView? = view.food_title
        var price: TextView? = view.food_price
        var rating: RatingBar? = view.food_rating
        var image: ImageView? = view.food_image
        var addToCartText: TextView? = view.add_button_text
        var addTOCartImage: ImageView? = view.add_button_image
        var addItem: ImageView? = view.add_item
        var removeItem: ImageView? = view.remove_item
        var itemCount: TextView? = view.item_count
        var addOrRemove: LinearLayout? = view.add_or_remove

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        val view = LayoutInflater.from(context).inflate(
            when (viewType) {

                ItemType.category -> R.layout.category_heading_layout
                ItemType.food -> R.layout.food_item_layout
                else -> error("Unknown view type: $viewType")
            }
            , parent, false
        )
        return ViewHolder(view)

    }

    override fun getItemCount() = list?.size ?: 0

    @SuppressLint("CheckResult")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        if (getItemViewType(position) == ItemType.food) {

            Picasso.get().load((list?.get(position) as Food).image).into(holder.image)
            holder.name?.text = (list[position] as Food).name
            holder.rating?.rating = (list[position] as Food).rating.toFloat()
            holder.price?.text = (list[position] as Food).price
            holder.image?.clipToOutline = true

            compositeDisposable.add(
                cartDataSource.getCartItem((list[position] as Food).foodId)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe({

                        if (it != null) {

                            if (it.quantity != 0) {
                                holder.addOrRemove?.visibility = View.VISIBLE
                                holder.itemCount?.text = it.quantity.toString()
                            }
                        } else
                            holder.addOrRemove?.visibility = View.INVISIBLE


                    }, { t: Throwable? ->
                        Toast.makeText(this.context, "[COUNT] $t", Toast.LENGTH_LONG)
                            .show()

                    })
            )

            holder.addToCartText?.setOnClickListener {

                val cartItem = CartItem()
                cartItem.apply {

                    foodId = (list[position] as Food).foodId
                    name = (list[position] as Food).name
                    price = (list[position] as Food).price.toDouble()
                    quantity = 1
                }

                compositeDisposable.add(cartDataSource.insertCartItem(cartItem)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(
                        {


                        }, { t: Throwable? ->
                            Toast.makeText(this.context, "[INSERT CART] $t", Toast.LENGTH_LONG)
                                .show()
                        }
                    ))
            }
            holder.addTOCartImage?.setOnClickListener {

                val cartItem = CartItem()
                cartItem.apply {

                    foodId = (list[position] as Food).foodId
                    name = (list[position] as Food).name
                    price = (list[position] as Food).price.toDouble()
                    quantity = 1
                }

                compositeDisposable.add(cartDataSource.insertCartItem(cartItem)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(
                        {
                            holder.addOrRemove?.visibility = View.VISIBLE
                        }, { t: Throwable? ->
                            Toast.makeText(this.context, "[INSERT CART] $t", Toast.LENGTH_LONG)
                                .show()
                        }
                    ))
            }

            holder.addItem?.setOnClickListener {

                compositeDisposable.add(cartDataSource.increaseQuantity((list[position] as Food).foodId)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(
                        {
                            cartDataSource.getCartItem((list[position] as Food).foodId)
                                .subscribeOn(Schedulers.io())
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe({

                                    holder.itemCount?.text = it.quantity.toString()
                                }, { t: Throwable? ->
                                    Toast.makeText(
                                        this.context,
                                        "[COUNT] $t",
                                        Toast.LENGTH_LONG
                                    )
                                        .show()

                                })
                        },
                        { t: Throwable? ->
                            Toast.makeText(this.context, "[UPDATE] $t", Toast.LENGTH_LONG)
                                .show()

                        }
                    ))
            }

            holder.removeItem?.setOnClickListener {

                compositeDisposable.add(cartDataSource.decreaseQuantity((list[position] as Food).foodId)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(
                        {
                            cartDataSource.getCartItem((list[position] as Food).foodId)
                                .subscribeOn(Schedulers.io())
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe({

                                    if (it != null)
                                        if (it.quantity != 0)
                                            holder.itemCount?.text = it.quantity.toString()
                                        else {
                                            cartDataSource.deleteCartItem(it)
                                                .subscribeOn(Schedulers.io())
                                                .observeOn(AndroidSchedulers.mainThread())
                                                .subscribe()
                                            holder.addOrRemove?.visibility = View.INVISIBLE
                                        }

                                }, { t: Throwable? ->
                                    Toast.makeText(
                                        this.context,
                                        "[COUNT] $t",
                                        Toast.LENGTH_LONG
                                    )
                                        .show()

                                })
                        },
                        { t: Throwable? ->
                            Toast.makeText(this.context, "[UPDATE] $t", Toast.LENGTH_LONG)
                                .show()

                        }
                    ))
            }

        } else if (getItemViewType(position) == ItemType.category) {

            holder.categoryName?.text = list?.get(position).toString()
        }
    }


    fun onStop() {

        compositeDisposable.clear()
    }

    override fun getItemViewType(position: Int): Int {

        return if (list?.get(position) is Food) {

            ItemType.food

        } else ItemType.category
    }
}