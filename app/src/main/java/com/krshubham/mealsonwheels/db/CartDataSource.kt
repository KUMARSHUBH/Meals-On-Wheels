package com.krshubham.mealsonwheels.db

import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Observable
import io.reactivex.Single

interface CartDataSource {


    fun getAllCartItems(): Flowable<List<CartItem>>

    fun getCartItemCount(): Single<Int>

    fun sumPrice(): Single<Float>

    fun getCartItem(foodId: String): Observable<CartItem>

    fun increaseQuantity(foodId: String): Completable

    fun decreaseQuantity(foodId: String): Single<Int>

    fun insertCartItem(cartItem: CartItem): Completable

    fun checkIfPresent(foodId: String): Single<Int>

    fun deleteCartItem(cartItem: CartItem): Single<Int>

    fun cleanCart()

}