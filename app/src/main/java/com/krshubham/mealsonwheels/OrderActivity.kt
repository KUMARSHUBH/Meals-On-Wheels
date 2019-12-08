package com.krshubham.mealsonwheels

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.krshubham.mealsonwheels.db.CartDataSource
import com.krshubham.mealsonwheels.db.CartDataSourceImpl
import com.krshubham.mealsonwheels.db.CartDatabase
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_order.*

class OrderActivity : AppCompatActivity() {

    private lateinit var cartDataSource : CartDataSource
    private lateinit var compositeDisposable: CompositeDisposable
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_order)

        cartDataSource = CartDataSourceImpl(CartDatabase.getInstance(this).cartDao())
        compositeDisposable = CompositeDisposable()

        cart_recycler_view.layoutManager = LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false)

        compositeDisposable.add(cartDataSource.getAllCartItems()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe {

                val adapter = CartListAdapter(this,it)
                cart_recycler_view.adapter = adapter

            })


    }
}
