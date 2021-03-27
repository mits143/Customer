package com.otgs.customerapp.view

import com.google.gson.JsonObject
import com.otgs.customerapp.model.response.ContactUsResponse
import com.otgs.customerapp.model.response.GarageDetail.GarageDetailResponse
import okhttp3.internal.http2.ErrorCode
import retrofit2.Response

interface GarageDetailView {

    interface MainView {

        fun showProgressbar()
        fun hideProgressbar()
        fun onSuccess(responseModel: Response<GarageDetailResponse>)
        fun onError(errorCode: Int)
        fun onError(throwable: Throwable)
    }

    interface MainPresenter {
        fun loadData(
            token: String,
            service_provider_id: String,
            cust_id: String
        )

        fun onStop()
    }
}