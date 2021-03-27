package com.otgs.customerapp.view

import com.otgs.customerapp.model.response.FAQResponse
import com.otgs.customerapp.model.response.LoginResponse
import okhttp3.internal.http2.ErrorCode
import retrofit2.Response

interface FAQView {

    interface MainView {

        fun showProgressbar()
        fun hideProgressbar()
        fun onSuccess(responseModel: Response<FAQResponse>)
        fun onError(errorCode: Int)
        fun onError(throwable: Throwable)
    }

    interface MainPresenter {
        fun loadData(user_id: String)
        fun onStop()
    }
}