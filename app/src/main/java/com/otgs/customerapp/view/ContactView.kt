package com.otgs.customerapp.view

import com.google.gson.JsonObject
import com.otgs.customerapp.model.response.ContactUsResponse
import okhttp3.internal.http2.ErrorCode
import retrofit2.Response

interface ContactView {

    interface MainView {

        fun showProgressbar()
        fun hideProgressbar()
        fun onSuccess(responseModel: Response<ContactUsResponse>)
        fun onSuccess1(responseModel: Response<JsonObject>)
        fun onError(errorCode: Int)
        fun onError(throwable: Throwable)
    }

    interface MainPresenter {
        fun loadData()
        fun submitData(
            user_id: String,
            user_type: String,
            query: String,
            category_id: String
        )
        fun onStop()
    }
}