package com.krshubham.mealsonwheels.db

import androidx.room.Entity
import androidx.room.PrimaryKey
import org.jetbrains.annotations.NotNull


@Entity(tableName = "cart")
data class CartItem(

    @PrimaryKey
    @NotNull
    var foodId: String = "",

    var name: String = "",
    var price: Double = 0.0,
    var quantity: Int = 0
)