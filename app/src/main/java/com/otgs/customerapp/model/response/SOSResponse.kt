package com.otgs.customerapp.model.response

data class SOSResponse(
    var Customer: SOSData,
    var emergency_contact: List<SOSEmergencyContact>,
    var message: String
)