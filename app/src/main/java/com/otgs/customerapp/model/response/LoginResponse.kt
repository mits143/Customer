package com.otgs.customerapp.model.response

data class LoginResponse(
    var email: String,
    var firstname: String,
    var is_first_atempt: Int,
    var lastname: String,
    var message: String,
    var mobile_no: String,
    var status: String,
    var uniqueNo: String,
    var userid: String,
    var usertype: String,
    var profile_photo: String,
    var token: String
)