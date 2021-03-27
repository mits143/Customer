package com.otgs.customerapp.model.response

data class GarageResponse(
    var garageInformation: List<GarageInformationData>,
    var message: String
)