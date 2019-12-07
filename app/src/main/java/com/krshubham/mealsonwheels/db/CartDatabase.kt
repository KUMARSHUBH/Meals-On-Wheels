package com.krshubham.mealsonwheels.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [CartItem::class],version = 1,exportSchema = false)
abstract class CartDatabase : RoomDatabase() {

    abstract fun cartDao() : CartDao

    companion object{

        private var instance:CartDatabase? = null

        fun getInstance(context: Context): CartDatabase{

            if(instance == null)
                instance = Room.databaseBuilder(context,CartDatabase::class.java, "cart.db").build()
            return instance!!
        }
    } }