package com.otgs.customerapp.view

import com.otgs.customerapp.model.response.LoginResponse
import okhttp3.internal.http2.ErrorCode
import retrofit2.Response

interface LoginView {

    interface MainView {

        fun showProgressbar()
        fun hideProgressbar()
        fun onSuccess(responseModel: Response<LoginResponse>)
        fun onError(errorCode: Int)
        fun onError(throwable: Throwable)
    }

    interface MainPresenter {
        fun loadData(username: String,
                     password: String,
                     device_token: String,
                     device_type: String,
                     user_type: String,
                     device_id: String
        )
        fun onStop()
    }
}