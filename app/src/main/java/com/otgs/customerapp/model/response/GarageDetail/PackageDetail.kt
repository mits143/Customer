package com.otgs.customerapp.model.response.GarageDetail

data class PackageDetail(
    var amount: String,
    var gst: String,
    var internet_handling_cost: String,
    var package_name: String,
    var package_status: String,
    var service_id: Int,
    var total: String
)