package com.otgs.customerapp.model.response

data class ServicingResponse(
    var four_wheeler: List<FourWheeler>,
    var message: String,
    var two_wheeler: ArrayList<TwoWheeler>
)