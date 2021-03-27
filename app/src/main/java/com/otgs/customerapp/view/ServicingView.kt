package com.otgs.customerapp.view

import com.otgs.customerapp.model.response.LoginResponse
import com.google.gson.JsonObject
import com.otgs.customerapp.model.response.ServicingResponse
import okhttp3.internal.http2.ErrorCode
import retrofit2.Response

interface ServicingView {

    interface MainView {

        fun showProgressbar()
        fun hideProgressbar()
        fun onSuccess(responseModel: Response<ServicingResponse>)
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