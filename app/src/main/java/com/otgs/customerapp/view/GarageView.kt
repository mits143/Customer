package com.otgs.customerapp.view

import com.otgs.customerapp.model.response.GarageResponse
import retrofit2.Response

interface GarageView {

    interface MainView {

        fun showProgressbar()
        fun hideProgressbar()
        fun onSuccess(responseModel: Response<GarageResponse>)
        fun onError(errorCode: Int)
        fun onError(throwable: Throwable)
    }

    interface MainPresenter {
        fun loadData(
            token: String,
            latitude: Double,
            longitude: Double,
            pickup_flag: Int,
            radius: String,
            service_id: String,
            user_id: String,
            user_type: String,
            doorstep_service_id: Int
        )

        fun onStop()
    }
}