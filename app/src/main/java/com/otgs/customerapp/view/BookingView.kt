package com.otgs.customerapp.view

import com.otgs.customerapp.model.response.LoginResponse
import com.google.gson.JsonObject
import com.otgs.customerapp.model.response.BookingResponse
import com.otgs.customerapp.model.response.BrandResponse
import okhttp3.internal.http2.ErrorCode
import retrofit2.Response

interface BookingView {

    interface MainView {

        fun showProgressbar()
        fun hideProgressbar()
        fun onSuccess(responseModel: Response<BookingResponse>)
        fun onSuccessBrand(responseModel: Response<BrandResponse>)
        fun onSuccessAddRating(responseModel: Response<JsonObject>)
        fun onError(errorCode: Int)
        fun onError(throwable: Throwable)
    }

    interface MainPresenter {
        fun loadData(
            token: String,
            end_date: String,
            start_date: String,
            type: Int,
            user_id: Int,
            user_type: Int,
            vehicle_type: Int,
            brand: Int,
            status: Int
        )

        fun loadBrandData(
            token: String,
            user_id: Int,
            user_type: Int
        )

        fun addRating(
            token: String,
            date: String,
            order_id: Int,
            rating: Int,
            review: String,
            sp_id: Int,
            user_id: Int,
            user_type: Int
        )

        fun onStop()
    }
}