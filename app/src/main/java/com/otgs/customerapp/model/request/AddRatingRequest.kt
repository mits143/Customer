package com.otgs.customerapp.model.request

data class AddRatingRequest(
    var date: String,
    var order_id: Int,
    var rating: Int,
    var review: String,
    var sp_id: Int,
    var user_id: Int,
    var user_type: Int
)