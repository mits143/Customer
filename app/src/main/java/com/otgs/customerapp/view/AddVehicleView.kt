package com.otgs.customerapp.view

import com.otgs.customerapp.model.response.LoginResponse
import com.google.gson.JsonObject
import com.otgs.customerapp.model.response.VehicleBrandResponse
import com.otgs.customerapp.model.response.VehicleSubTypeResponse
import com.otgs.customerapp.model.response.VehicleTypeResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.internal.http2.ErrorCode
import retrofit2.Response
import retrofit2.http.Header
import retrofit2.http.Part

interface AddVehicleView {

    interface MainView {

        fun showProgressbar()
        fun hideProgressbar()
        fun onSuccessVehicleType(responseModel: Response<VehicleTypeResponse>)
        fun onSuccessVehicleSubType(responseModel: Response<VehicleSubTypeResponse>)
        fun onSuccessVehicleBrand(responseModel: Response<VehicleBrandResponse>)
        fun onSuccess(responseModel: Response<JsonObject>)
        fun onError(errorCode: Int)
        fun onError(throwable: Throwable)
    }

    interface MainPresenter {
        fun loadVehicleTypeData(
            token: String
        )

        fun loadVehicleSubTypeData(
            token: String,
            vehicle_type_id: String
        )

        fun loadVehicleBrandData(
            token: String,
            vehicle_subtype_id: String
        )

        fun loadData(
            @Header("token") token: String,
            @Part("user_id") user_id: RequestBody,
            @Part("user_type") user_type: RequestBody,
            @Part("vehicle_type") vehicle_type: RequestBody,
            @Part("vehicle_number") vehicle_number: RequestBody,
            @Part("vehicle_sub_type") vehicle_sub_type: RequestBody,
            @Part("vehicle_brand") vehicle_brand: RequestBody,
            @Part("vehicle_details_id") vehicle_details_id: RequestBody,
            @Part driving_licence: MultipartBody.Part,
            @Part puc: MultipartBody.Part,
            @Part registration_paper: ArrayList<MultipartBody.Part>,
            @Part insurance: ArrayList<MultipartBody.Part>,
            @Part other: ArrayList<MultipartBody.Part>
        )

        fun onStop()
    }
}