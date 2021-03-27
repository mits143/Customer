package com.otgs.customerapp.view

import com.otgs.customerapp.model.response.AutoHubResponse
import okhttp3.internal.http2.ErrorCode
import retrofit2.Response

interface HomeView {

    interface MainView {

        fun showProgressbar()
        fun hideProgressbar()
        fun onSuccess(responseModel: Response<AutoHubResponse>)
        fun onError(errorCode: Int)
        fun onError(throwable: Throwable)
    }

    interface MainPresenter {
        fun loadData()
        fun onStop()
    }
}