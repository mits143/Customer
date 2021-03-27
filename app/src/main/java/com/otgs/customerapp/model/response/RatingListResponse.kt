package com.otgs.customerapp.model.response

data class RatingListResponse(
    var RatingReview: List<RatingReview>,
    var message: String,
    var total_pages: Int
)