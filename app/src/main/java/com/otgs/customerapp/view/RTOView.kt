package com.otgs.customerapp.view

import com.otgs.customerapp.model.response.LoginResponse
import com.otgs.customerapp.model.response.RTOResponse
import com.otgs.customerapp.model.response.SOSResponse
import com.otgs.customerapp.model.response.StateResponse
import okhttp3.internal.http2.ErrorCode
import retrofit2.Response

interface RTOView {

    interface MainView {

        fun showProgressbar()
        fun hideProgressbar()
        fun onSuccessgetStates(responseModel: Response<StateResponse>)
        fun onSuccess(responseModel: Response<RTOResponse>)
        fun onError(errorCode: Int)
        fun onError(throwable: Throwable)
    }

    interface MainPresenter {
        fun loadStates()
        fun loadData(
            token: String,
            state_id: String
        )

        fun onStop()
    }
}