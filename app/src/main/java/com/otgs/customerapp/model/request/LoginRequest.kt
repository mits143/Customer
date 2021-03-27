package com.otgs.customerapp.model.request

data class LoginRequest(
    var username: String,
    var password: String,
    var device_token: String,
    var device_type: String,
    var user_type: String,
    var device_id: String
)