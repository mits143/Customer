package com.otgs.customerapp.model.request

data class GoogleMapDataRequest(
    var lat: Double,
    var lng: Double,
    var pickup_flag: Int,
    var radius: String,
    var service_id: String,
    var user_id: String,
    var user_type: String,
    var doorstep_service_id: Int
)