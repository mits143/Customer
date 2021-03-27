package com.otgs.customerapp.model.request

data class RatingListRequest(
    var end_date: String,
    var page_number: Int,
    var start_date: String,
    var user_id: Int,
    var user_type: Int
)