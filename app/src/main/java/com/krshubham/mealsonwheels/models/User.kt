package com.krshubham.mealsonwheels.models

data class User(val name: String, val email: String, val lat: Double, val lng: Double, val fullAddress: String? = null, val howToReach: String? = null)