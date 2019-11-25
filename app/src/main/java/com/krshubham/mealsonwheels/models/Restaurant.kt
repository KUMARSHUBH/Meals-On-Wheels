package com.krshubham.mealsonwheels.models

data class Restaurant(var name: String = "", val contact: String="", val lat: Double=0.0, val lng: Double=0.0, var image:String="", var rating:String="", val payment_url: String="", var perPersonCost: String="")