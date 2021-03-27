package com.otgs.customerapp.view

import com.otgs.customerapp.model.response.LoginResponse
import com.otgs.customerapp.model.response.SOSResponse
import okhttp3.internal.http2.ErrorCode
import retrofit2.Response

interface SOSView {

    interface MainView {

        fun showProgressbar()
        fun hideProgressbar()
        fun onSuccess(responseModel: Response<SOSResponse>)
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