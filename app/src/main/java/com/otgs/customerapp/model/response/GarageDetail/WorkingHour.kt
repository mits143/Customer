package com.otgs.customerapp.model.response.GarageDetail

data class WorkingHour(
    var day: String,
    var from_time: String,
    var sp_id: Int,
    var sp_unique_number: String,
    var to_time: String
)