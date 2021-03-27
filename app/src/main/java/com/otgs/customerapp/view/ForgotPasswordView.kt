package com.otgs.customerapp.view

import com.otgs.customerapp.model.request.ForgotPwdRequest
import com.otgs.customerapp.model.response.LoginResponse
import com.google.gson.JsonObject
import org.json.JSONObject
import retrofit2.Response

interface ForgotPasswordView {

    interface MainView {

        fun showProgressbar()
        fun hideProgressbar()
        fun onSuccess(responseModel: Response<JsonObject>)
        fun onError(errorCode: Int)
        fun onError(throwable: Throwable)
    }

    interface MainPresenter {
        fun loadData(username: String,
                     user_type: String)
        fun onStop()
    }
}