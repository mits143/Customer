package com.otgs.customerapp.model.response

data class RatingReview(
    var date: String,
    var garage_name: String,
    var order_id: String,
    var rating: Int,
    var review: String
)