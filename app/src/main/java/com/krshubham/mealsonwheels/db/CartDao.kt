package com.krshubham.mealsonwheels.db

import androidx.room.*
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Observable
import io.reactivex.Single

@Dao
interface CartDao {

    @Query("SELECT * FROM cart")
    fun getAllCartItems(): Flowable<List<CartItem>>

    @Query("SELECT COUNT(*) FROM cart")
    fun getCartItemCount(): Single<Int>

    @Query("SELECT COUNT(*) FROM cart where foodId = :foodId")
    fun checkIfPresent(foodId: String): Single<Int>

    @Query("SELECT SUM(price*quantity) FROM cart")
    fun sumPrice(): Single<Float>

    @Query("SELECT * FROM cart where foodId= :foodId")
    fun getCartItem(foodId: String): Observable<CartItem>

    @Query("UPDATE cart SET quantity = quantity+1 where foodId = :foodId")
    fun increaseQuantity(foodId: String): Completable

    @Query("UPDATE cart SET quantity = quantity-1 where foodId = :foodId and quantity > 0")
    fun decreaseQuantity(foodId: String): Single<Int>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertCartItem(cartItem: CartItem): Completable

    @Delete
    fun deleteCartItem(cartItem: CartItem): Single<Int>

    @Query("DELETE FROM cart")
    fun cleanCart()


}