package com.otgs.customerapp.model.response.GarageDetail

data class ServiceDetail(
    var area_limit: String,
    var brands: List<Brand>,
    var dropDetails: List<DropDetail>,
    var labour_cost: String,
    var pick_drop_flag: String,
    var pickupDetails: List<PickupDetail>,
    var serviceContactDetails: List<ServiceContactDetail>,
    var service_id: Int,
    var spare_part_type: String,
    var vehicle_types: List<VehicleType>
)