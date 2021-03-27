package com.otgs.customerapp.model.request

data class ChangePwdRequest(
    val new_password: String,
    val old_password: String,
    val user_id: Int,
    val user_type: Int
)