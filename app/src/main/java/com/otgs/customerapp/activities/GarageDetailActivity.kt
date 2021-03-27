package com.otgs.customerapp.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.google.gson.JsonObject
import com.kuber.vpn.Utils.sessionManager
import com.otgs.customerapp.R
import com.otgs.customerapp.model.response.GarageDetail.GarageDetailResponse
import com.otgs.customerapp.presenter.GarageDetailPresenter
import com.otgs.customerapp.view.GarageDetailView
import kotlinx.android.synthetic.main.activity_change_password.*
import kotlinx.android.synthetic.main.activity_garage_detail.*
import retrofit2.Response

class GarageDetailActivity : AppCompatActivity(), GarageDetailView.MainView {

    var presenter: GarageDetailPresenter? = null
    var service_id = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_garage_detail)
        init()
    }

    fun init() {
        if (intent.extras != null) {
            service_id = intent.extras!!.getInt("service_id").toString()
        }
        presenter = GarageDetailPresenter(this, this)
        presenter!!.loadData(
            sessionManager.getToken(this)!!,
            service_id,
            sessionManager.getUser_ID(this)!!
        )
        txtBookOrder
    }

    override fun showProgressbar() {
    }

    override fun hideProgressbar() {
    }

    override fun onSuccess(responseModel: Response<GarageDetailResponse>) {
        if (responseModel.body() != null) {
            txtName.text = "Garage name: " + responseModel.body()!!.garageInformation.garage_name
            txtAddress.text = "Address: " + responseModel.body()!!.garageInformation.contact_address +
                    " " + responseModel.body()!!.garageInformation.contact_city +
                    " " + responseModel.body()!!.garageInformation.contact_state +
                    " " + responseModel.body()!!.garageInformation.contact_pincode
            if (responseModel.body()!!.workingHours.size == 1) {
                txtMon.text = "Monday"
                txtMonTime.text =
                    responseModel.body()!!.workingHours[0].from_time + " " + responseModel.body()!!.workingHours[0].from_time
            }
            if (responseModel.body()!!.workingHours.size == 2) {
                txtTue.text = "Tuesday"
                txtTueTime.text =
                    responseModel.body()!!.workingHours[1].from_time + " " + responseModel.body()!!.workingHours[1].from_time
            }
            if (responseModel.body()!!.workingHours.size == 3) {
                txtWed.text = "Wednesday"
                txtWedTime.text =
                    responseModel.body()!!.workingHours[2].from_time + " " + responseModel.body()!!.workingHours[2].from_time
            }
            if (responseModel.body()!!.workingHours.size == 4) {
                txtThu.text = "Thursday"
                txtThuTime.text =
                    responseModel.body()!!.workingHours[3].from_time + " " + responseModel.body()!!.workingHours[3].from_time
            }
            if (responseModel.body()!!.workingHours.size == 5) {
                txtFri.text = "Friday"
                txtFriTime.text =
                    responseModel.body()!!.workingHours[4].from_time + " " + responseModel.body()!!.workingHours[4].from_time
            }
            if (responseModel.body()!!.workingHours.size == 6) {
                txtSat.text = "Saturday"
                txtSatTime.text =
                    responseModel.body()!!.workingHours[5].from_time + " " + responseModel.body()!!.workingHours[5].from_time
            }
            if (responseModel.body()!!.workingHours.size == 7) {
                txtSun.text = "Sunday"
                txtSunTime.text =
                    responseModel.body()!!.workingHours[6].from_time + " " + responseModel.body()!!.workingHours[6].from_time
            }
            txtCompleted.text = responseModel.body()!!.garageInformation.total_count.toString()
            if (responseModel.body()!!.serviceDetails.size == 1) {
                txtLabourCharges.text = responseModel.body()!!.serviceDetails[0].labour_cost
            }
            if (responseModel.body()!!.paymentModes.size == 1) {
                txtPaymentType.text = responseModel.body()!!.paymentModes[0].payment_mode_name
            }

        }
    }

    override fun onError(errorCode: Int) {
        if (errorCode == 500) {
            Toast.makeText(this, getString(R.string.internal_server_error), Toast.LENGTH_SHORT)
                .show()
        } else {
            Toast.makeText(this, getString(R.string.error), Toast.LENGTH_SHORT).show()
        }
    }

    override fun onError(throwable: Throwable) {
        Toast.makeText(this, getString(R.string.error), Toast.LENGTH_SHORT).show()
    }

    override fun onDestroy() {
        super.onDestroy()
        presenter!!.onStop()
    }
}