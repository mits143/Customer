package com.otgs.customerapp.model.response.GarageDetail

data class GarageDetailResponse(
    var documentDetails: List<DocumentDetail>,
    var gallaryImages: List<GallaryImage>,
    var garageInformation: GarageInformation,
    var message: String,
    var packageDetails: List<PackageDetail>,
    var paymentModes: List<PaymentMode>,
    var reviewDetails: List<ReviewDetail>,
    var serviceDetails: List<ServiceDetail>,
    var workingHours: List<WorkingHour>
)