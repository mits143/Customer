package com.otgs.customerapp.model.request

data class BookingRequest(
    var end_date: String,
    var start_date: String,
    var type: Int,
    var user_id: Int,
    var user_type: Int,
    var vehicle_type: Int,
    var brand: Int,
    var status: Int
)