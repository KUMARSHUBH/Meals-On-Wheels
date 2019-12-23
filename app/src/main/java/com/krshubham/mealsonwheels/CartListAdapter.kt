package com.krshubham.mealsonwheels

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.krshubham.mealsonwheels.db.CartDataSource
import com.krshubham.mealsonwheels.db.CartDataSourceImpl
import com.krshubham.mealsonwheels.db.CartDatabase
import com.krshubham.mealsonwheels.db.CartItem
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.cart_item_layout.view.*


class CartListAdapter(val context: Context, val items : List<CartItem>): RecyclerView.Adapter<CartListAdapter.ViewHolder>(){


    private val compositeDisposable: CompositeDisposable = CompositeDisposable()
    private val cartDataSource: CartDataSource

    init {

        cartDataSource = CartDataSourceImpl(CartDatabase.getInstance(this.context).cartDao())
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view){

        var name:TextView? = view.food_name
        var price: TextView? = view.food_price
        var totalPrice: TextView? = view.total_price_per_item
        var addItemCart: ImageView? = view.add_item_cart
        var removeItemCart: ImageView? = view.remove_item_cart
        var itemCountCart: TextView? = view.item_count_cart
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {


        val view =
            LayoutInflater.from(context).inflate(R.layout.cart_item_layout, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount() = items.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        holder.name?.text = items[position].name
        holder.price?.text = items[position].price.toString()
        holder.totalPrice?.text = ((items[position].price) * (items[position].quantity)).toString()
        holder.itemCountCart?.text = items[position].quantity.toString()

        holder.addItemCart?.setOnClickListener {

            compositeDisposable.add(cartDataSource.increaseQuantity((items[position]).foodId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    {
                        cartDataSource.getCartItem((items[position]).foodId)
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe({

                                holder.itemCountCart?.text = it.quantity.toString()
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

        holder.removeItemCart?.setOnClickListener {

            compositeDisposable.add(cartDataSource.decreaseQuantity((items[position]).foodId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    {
                        cartDataSource.getCartItem((items[position]).foodId)
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe({

                                if (it != null)
                                    if (it.quantity != 0)
                                        holder.itemCountCart?.text = it.quantity.toString()
                                    else {
                                        cartDataSource.deleteCartItem(it)
                                            .subscribeOn(Schedulers.io())
                                            .observeOn(AndroidSchedulers.mainThread())
                                            .subscribe()
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

    }
}