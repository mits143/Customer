package com.otgs.customerapp.model.response

data class OrderMaster(
    var OrderNumber: String,
    var brands: String,
    var date: String,
    var details: List<Detail>,
    var drop_date: String,
    var drop_person: String,
    var drop_person_id: String,
    var drop_slot: String,
    var drop_time: String,
    var order_no: Int,
    var pickup_date: String,
    var pickup_person: String,
    var pickup_person_id: String,
    var pickup_slot: String,
    var pickup_time: String,
    var rating: String,
    var review: String,
    var status: String,
    var vehicle_Number: String,
    var vehicle_type: String
)