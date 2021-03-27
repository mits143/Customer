package com.otgs.customerapp.model.response

import com.otgs.customerapp.model.response.VehicleDetail

data class VehicleListResponse(
    var message: String,
    var vehicle_details: List<VehicleDetail>
)