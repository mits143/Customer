package com.otgs.customerapp.model.request

data class AddVehicleRequest(
    var user_id: Int,
    var user_type: Int,
    var vehicle_brand: String,
    var vehicle_number: String,
    var vehicle_sub_type: Int,
    var vehicle_type: Int
)