package com.otgs.customerapp.view

import com.otgs.customerapp.model.response.LoginResponse
import com.google.gson.JsonObject
import com.otgs.customerapp.model.response.RatingListResponse
import okhttp3.internal.http2.ErrorCode
import retrofit2.Response

interface RatingListView {

    interface MainView {

        fun showProgressbar()
        fun hideProgressbar()
        fun onSuccess(responseModel: Response<RatingListResponse>)
        fun onError(errorCode: Int)
        fun onError(throwable: Throwable)
    }

    interface MainPresenter {
        fun loadData(
            token: String,
            end_date: String,
            page_number: Int,
            start_date: String,
            user_id: Int,
            user_type: Int
        )

        fun onStop()
    }
}