package com.otgs.customerapp.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import com.kuber.vpn.Utils.sessionManager
import com.otgs.customerapp.R
import com.otgs.customerapp.adapter.SOSAdapter
import com.otgs.customerapp.model.response.SOSEmergencyContact
import com.otgs.customerapp.model.response.SOSResponse
import com.otgs.customerapp.presenter.SOSPresenter
import com.otgs.customerapp.view.SOSView
import kotlinx.android.synthetic.main.activity_sos.*
import retrofit2.Response

class SOSActivity : AppCompatActivity(), SOSView.MainView {

    var presenter: SOSPresenter? = null
    var adapter: SOSAdapter? = null
    var sosList: ArrayList<SOSEmergencyContact>? = arrayListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sos)
        init()
    }

    fun init() {
        presenter = SOSPresenter(this, this)
        presenter!!.loadData(
            sessionManager.getToken(this)!!,
            sessionManager.getUser_ID(this)!!,
            sessionManager.getUserType(this)!!
        )
        val mLayoutManagerForItems = LinearLayoutManager(this)
        recyclerView.layoutManager = mLayoutManagerForItems
        recyclerView.itemAnimator = DefaultItemAnimator()
        adapter = SOSAdapter(sosList!!)
        recyclerView.adapter = adapter
        adapter!!.setOnItemClickListener(object : SOSAdapter.OnItemClickListener {
            override fun onItemClick(view: View, obj: SOSEmergencyContact, position: Int) {

            }
        })
    }

    override fun showProgressbar() {

    }

    override fun hideProgressbar() {

    }

    override fun onSuccess(responseModel: Response<SOSResponse>) {
        if (responseModel.body() != null) {
            txtRelation1.text = responseModel.body()!!.Customer.relation1
            txtRelation3.text = responseModel.body()!!.Customer.relation2
            txtRelation1.text = responseModel.body()!!.Customer.relation3
            txtMobile1.text = responseModel.body()!!.Customer.emergency_contact1
            txtMobile2.text = responseModel.body()!!.Customer.emergency_contact2
            txtMobile3.text = responseModel.body()!!.Customer.emergency_contact3

            sosList!!.clear()
            sosList!!.addAll(responseModel.body()!!.emergency_contact)
            adapter!!.notifyDataSetChanged()
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