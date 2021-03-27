package com.otgs.customerapp.model.response

data class Details(
    var List: List<VehicleDoc>,
    var vehicle_brand: String,
    var vehicle_brand_id: Int,
    var vehicle_details_id: Int,
    var vehicle_number: String,
    var vehicle_sub_type: String,
    var vehicle_sub_type_id: Int,
    var vehicle_type: String,
    var vehicle_type_id: Int
)