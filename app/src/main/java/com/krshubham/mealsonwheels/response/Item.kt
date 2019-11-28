package com.krshubham.mealsonwheels.response

data class Item(
    val kind: String,
    val etag: String,
    val id: Id,
    val snippet: Snippet
)