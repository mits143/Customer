package com.otgs.customerapp.model.request

data class ForgotPwdRequest(
    var username: String,
    var user_type: String
)