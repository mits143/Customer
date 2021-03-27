package com.otgs.customerapp.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.kuber.vpn.Utils.sessionManager
import com.otgs.customerapp.R
import com.otgs.customerapp.adapter.VehicleAdapter
import com.otgs.customerapp.model.response.VehicleDetail
import com.otgs.customerapp.model.response.VehicleListResponse
import com.otgs.customerapp.presenter.VehicleListPresenter
import com.otgs.customerapp.view.VehicleListView
import kotlinx.android.synthetic.main.activity_vehicle_list.*
import kotlinx.android.synthetic.main.activity_vehicle_list.recyclerView
import kotlinx.android.synthetic.main.activity_vehicle_list.swipeRefresh
import retrofit2.Response
import java.util.ArrayList

class VehicleList : AppCompatActivity(), VehicleListView.MainView {

    var presenter: VehicleListPresenter? = null
    var adapter: VehicleAdapter? = null
    var vehicleList: ArrayList<VehicleDetail>? = arrayListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_vehicle_list)
        init()
    }

    fun init() {
        presenter = VehicleListPresenter(this, this)
        if (!TextUtils.isEmpty(sessionManager.getUser_ID(this)!!)) {
            presenter!!.loadData(
                sessionManager.getToken(this)!!,
                sessionManager.getUser_ID(this)!!,
                sessionManager.getUserType(this)!!
            )
        }

        swipeRefresh.setOnRefreshListener {
            if (!TextUtils.isEmpty(sessionManager.getUser_ID(this)!!)) {
                presenter!!.loadData(
                    sessionManager.getToken(this)!!,
                    sessionManager.getUser_ID(this)!!,
                    sessionManager.getUserType(this)!!
                )
            }
        }

        val layoutManager = LinearLayoutManager(this)
        recyclerView!!.setLayoutManager(layoutManager)
        adapter = VehicleAdapter(vehicleList!!)
        recyclerView!!.setAdapter(adapter)
        adapter!!.setOnItemClickListener(object : VehicleAdapter.OnItemClickListener {
            override fun onItemClick(view: View, obj: VehicleDetail, position: Int) {
                startActivity(
                    Intent(
                        this@VehicleList,
                        EditVehicleActivity::class.java
                    ).putExtra("Vehicle_ID", obj.vehicle_details_id)
                )
            }
        })
    }

    override fun showProgressbar() {
        swipeRefresh.isRefreshing = true
    }

    override fun hideProgressbar() {
        swipeRefresh.isRefreshing = false
    }

    override fun onSuccess(responseModel: Response<VehicleListResponse>) {
        if (responseModel.body() != null && responseModel.body()!!.vehicle_details.size > 0) {
            txtAddVehicle.visibility = View.GONE
            vehicleList!!.clear()
            vehicleList!!.addAll(responseModel.body()!!.vehicle_details)
            adapter!!.notifyDataSetChanged()

        } else {
            vehicleList!!.clear()
            txtAddVehicle.visibility = View.VISIBLE
        }

    }

    override fun onError(errorCode: Int) {
        vehicleList!!.clear()
        txtAddVehicle.visibility = View.VISIBLE
        if (errorCode == 500) {
            Toast.makeText(this, getString(R.string.internal_server_error), Toast.LENGTH_SHORT)
                .show()
        } else {
            Toast.makeText(this, getString(R.string.error), Toast.LENGTH_SHORT).show()
        }
    }

    override fun onError(throwable: Throwable) {
        vehicleList!!.clear()
        txtAddVehicle.visibility = View.VISIBLE
        Toast.makeText(this, getString(R.string.error), Toast.LENGTH_SHORT).show()
    }

    override fun onDestroy() {
        super.onDestroy()
        presenter!!.onStop()
    }
}