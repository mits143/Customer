package com.otgs.customerapp.model.response

data class Customer(
    var address: String,
    var city_id: String,
    var city_name: String,
    var customer_id: Int,
    var dob: String,
    var email_id: String,
    var emergency_contact1: String,
    var emergency_contact2: String,
    var emergency_contact3: String,
    var first_name: String,
    var gender: String,
    var is_active: String,
    var last_name: String,
    var mobile_no: String,
    var pincode: String,
    var pincode_value: String,
    var profile_image: String,
    var relation1: String,
    var relation2: String,
    var relation3: String,
    var state_id: String,
    var state_name: String,
    var user_id: Int,
    var user_name: String
)