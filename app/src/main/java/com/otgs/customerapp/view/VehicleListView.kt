package com.otgs.customerapp.view

import com.google.gson.JsonObject
import com.otgs.customerapp.model.response.*
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.internal.http2.ErrorCode
import retrofit2.Response
import retrofit2.http.Header
import retrofit2.http.Part

interface VehicleListView {

    interface MainView {

        fun showProgressbar()
        fun hideProgressbar()
        fun onSuccess(responseModel: Response<VehicleListResponse>)
        fun onError(errorCode: Int)
        fun onError(throwable: Throwable)
    }

    interface MainPresenter {
        fun loadData(
            token: String,
            user_id: String,
            user_type: String
        )

        fun onStop()
    }
}