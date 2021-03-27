package com.otgs.customerapp.view

import com.otgs.customerapp.model.response.LoginResponse
import com.google.gson.JsonObject
import okhttp3.internal.http2.ErrorCode
import retrofit2.Response

interface ChangePasswordView {

    interface MainView {

        fun showProgressbar()
        fun hideProgressbar()
        fun onSuccess(responseModel: Response<JsonObject>)
        fun onError(errorCode: Int)
        fun onError(throwable: Throwable)
    }

    interface MainPresenter {
        fun loadData(
            new_password: String,
            old_password: String,
            user_id: Int,
            user_type: Int
        )

        fun onStop()
    }
}