package com.krshubham.mealsonwheels.db

import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Single

interface CartDataSource {

    fun getAllCartItems(): Flowable<List<CartItem>>

    fun getCartItemCount(): Single<Int>

    fun sumPrice(): Single<Float>

    fun getCartItem(foodId: String): Single<CartItem>

    fun insertOrReplaceAll(vararg  cartItem: CartItem) : Completable

    fun updateCart(cartItem: CartItem) : Single<Int>

    fun deleteCart(cartItem: CartItem) : Single<Int>

    fun cleanCart()

}