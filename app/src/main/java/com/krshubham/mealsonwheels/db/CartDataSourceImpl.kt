package com.krshubham.mealsonwheels.db

import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Single

class CartDataSourceImpl(private val cartDao: CartDao) : CartDataSource {
    override fun getAllCartItems(): Flowable<List<CartItem>> {

        return cartDao.getAllCartItems()
    }

    override fun getCartItemCount(): Single<Int> {

        return cartDao.getCartItemCount()
    }

    override fun sumPrice(): Single<Float> {

        return cartDao.sumPrice()
    }

    override fun getCartItem(foodId: String): Single<CartItem> {

        return cartDao.getCartItem(foodId)
    }

    override fun insertOrReplaceAll(vararg cartItem: CartItem): Completable {

        return cartDao.insertOrReplaceAll(*cartItem)
    }

    override fun updateCart(cartItem: CartItem): Single<Int> {

        return cartDao.updateCart(cartItem)
    }

    override fun deleteCart(cartItem: CartItem): Single<Int> {

        return cartDao.deleteCart(cartItem)
    }

    override fun cleanCart() {
        return cartDao.cleanCart()
    }
}