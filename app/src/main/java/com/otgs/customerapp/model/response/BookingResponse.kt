package com.otgs.customerapp.model.response

data class BookingResponse(
    var OrderMaster: List<OrderMaster>,
    var message: String
)