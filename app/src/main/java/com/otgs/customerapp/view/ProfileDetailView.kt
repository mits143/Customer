package com.otgs.customerapp.view

import com.google.gson.JsonObject
import com.otgs.customerapp.model.response.CityReponse
import com.otgs.customerapp.model.response.PincodeResponse
import com.otgs.customerapp.model.response.ProfileDetailResponse
import com.otgs.customerapp.model.response.StateResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.internal.http2.ErrorCode
import retrofit2.Response
import retrofit2.http.Header
import retrofit2.http.Part

interface ProfileDetailView {

    interface MainView {

        fun showProgressbar()
        fun hideProgressbar()
        fun onSuccess(responseModel: Response<ProfileDetailResponse>)
        fun onSuccessEditProfile(int: Int, responseModel: Response<JsonObject>)
        fun onSuccessgetStates(responseModel: Response<StateResponse>)
        fun onSuccessgetCities(responseModel: Response<CityReponse>)
        fun onSuccessgetPincodes(responseModel: Response<PincodeResponse>)
        fun onError(errorCode: Int)
        fun onError(throwable: Throwable)
    }

    interface MainPresenter {
        fun loadDataProfileDetail(
            token: String,
            customer_id: String
        )

        fun editProfile(
            token: String,
            @Part profile: MultipartBody.Part,
            @Part("user_id") user_id: RequestBody,
            @Part("user_type") user_type: RequestBody,
            @Part("address") address: RequestBody,
            @Part("dob") dob: RequestBody,
            @Part("email_id") email_id: RequestBody,
            @Part("emergency_contact1") emergency_contact1: RequestBody,
            @Part("emergency_contact2") emergency_contact2: RequestBody,
            @Part("emergency_contact3") emergency_contact3: RequestBody,
            @Part("first_name") first_name: RequestBody,
            @Part("last_name") last_name: RequestBody,
            @Part("gender") gender: RequestBody,
            @Part("mobile_no") mobile_no: RequestBody,
            @Part("pincode") pincode: RequestBody,
            @Part("relation1") relation1: RequestBody,
            @Part("relation2") relation2: RequestBody,
            @Part("relation3") relation3: RequestBody,
            @Part("state_id") state_id: RequestBody,
            @Part("city_id") city_id: RequestBody
        )

        fun loadStates()

        fun loadCities(state_id: String)

        fun loadPincodes(city_id: String)

        fun onStop()
    }
}