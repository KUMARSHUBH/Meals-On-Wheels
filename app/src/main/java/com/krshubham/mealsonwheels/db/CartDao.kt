package com.krshubham.mealsonwheels.db

import androidx.room.*
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Single

@Dao
interface CartDao {

    @Query("SELECT * FROM cart")
    fun getAllCartItems(): Flowable<List<CartItem>>


    @Query("SELECT COUNT(*) FROM cart")
    fun getCartItemCount(): Single<Int>

    @Query("SELECT SUM(price*quantity) FROM cart")
    fun sumPrice(): Single<Float>


    @Query("SELECT * FROM cart where foodId= :foodId")
    fun getCartItem(foodId: String): Single<CartItem>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertOrReplaceAll(vararg  cartItem: CartItem) : Completable

    @Update(onConflict = OnConflictStrategy.REPLACE)
    fun updateCart(cartItem: CartItem) : Single<Int>

    @Delete
    fun deleteCart(cartItem: CartItem) : Single<Int>

    @Query("DELETE FROM cart")
    fun cleanCart()



}