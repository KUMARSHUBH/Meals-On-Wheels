package com.krshubham.mealsonwheels

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.krshubham.mealsonwheels.db.CartDataSource
import com.krshubham.mealsonwheels.db.CartDataSourceImpl
import com.krshubham.mealsonwheels.db.CartDatabase
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.cart_bottom_sheet.*

class BottomSheetFragment : BottomSheetDialogFragment() {

    private lateinit var cartDataSource: CartDataSource
    private lateinit var compositeDisposable: CompositeDisposable
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View =
        inflater.inflate(R.layout.cart_bottom_sheet, container, false)


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        var itemTotal: Double
        var tax: Double

        cartDataSource = CartDataSourceImpl(CartDatabase.getInstance(context!!).cartDao())
        compositeDisposable = CompositeDisposable()


        orders_recycler_view.layoutManager = LinearLayoutManager(
            context,
            LinearLayoutManager.VERTICAL, false
        )


        compositeDisposable.add(cartDataSource.getAllCartItems()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                val adapter = CartListAdapter(context!!,it)
                orders_recycler_view.adapter = adapter

                itemTotal = 0.0
                it.forEach {cartItem->

                    val total = (cartItem.quantity)*(cartItem.price)
                    itemTotal += total
                }


                tax = (itemTotal*18)/100


                total_price.text = itemTotal.toString()
                taxes.text = tax.toString()

                total_payable_price.text = (itemTotal+tax).toString()
                total_cost.text = total_payable_price.text

            },{
                Toast.makeText(context,it.message,Toast.LENGTH_LONG).show()
            }))


    }


    override fun onStop() {

        compositeDisposable.clear()
        super.onStop()

    }
}
