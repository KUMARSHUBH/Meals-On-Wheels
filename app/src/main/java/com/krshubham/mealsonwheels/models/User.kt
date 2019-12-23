package com.krshubham.mealsonwheels.models

data class User(var name: String="", val email: String="", val lat: Double=0.0, val lng: Double=0.0, val fullAddress: String? = null, val howToReach: String? = null,val phone: String? = null)