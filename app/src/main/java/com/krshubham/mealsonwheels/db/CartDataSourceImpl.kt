package com.krshubham.mealsonwheels.db

import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Observable
import io.reactivex.Single

class CartDataSourceImpl(private val cartDao: CartDao) : CartDataSource {
    override fun getAllCartItems(): Flowable<List<CartItem>> {

        return cartDao.getAllCartItems()
    }

    override fun getCartItemCount(): Observable<Int> {

        return cartDao.getCartItemCount()
    }

    override fun sumPrice(): Single<Float> {

        return cartDao.sumPrice()
    }

    override fun getCartItem(foodId: String): Observable<CartItem> {

        return cartDao.getCartItem(foodId)
    }


    override fun cleanCart() {
        return cartDao.cleanCart()
    }

    override fun increaseQuantity(foodId: String): Completable {
        return cartDao.increaseQuantity(foodId)
    }

    override fun decreaseQuantity(foodId: String): Single<Int> {
        return cartDao.decreaseQuantity(foodId)
    }

    override fun insertCartItem(cartItem: CartItem): Completable {
        return cartDao.insertCartItem(cartItem)
    }

    override fun checkIfPresent(foodId: String): Single<Int> {
        return cartDao.checkIfPresent(foodId)
    }

    override fun deleteCartItem(cartItem: CartItem): Single<Int> {
        return cartDao.deleteCartItem(cartItem)
    }
}