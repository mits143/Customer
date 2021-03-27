package com.otgs.customerapp.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.Toast
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import com.channelpartner.adapter.SpinStateAdapter
import com.kuber.vpn.Utils.sessionManager
import com.otgs.customerapp.R
import com.otgs.customerapp.adapter.RTOAdapter
import com.otgs.customerapp.model.response.AllRule
import com.otgs.customerapp.model.response.AllState
import com.otgs.customerapp.model.response.RTOResponse
import com.otgs.customerapp.model.response.StateResponse
import com.otgs.customerapp.presenter.RTOPresenter
import com.otgs.customerapp.view.RTOView
import kotlinx.android.synthetic.main.activity_rto.spinState
import kotlinx.android.synthetic.main.activity_sos.recyclerView
import retrofit2.Response

class RTOActivity : AppCompatActivity(), RTOView.MainView {

    var presenter: RTOPresenter? = null
    var adapter: RTOAdapter? = null
    var rtoList: ArrayList<AllRule>? = arrayListOf()

    var stateAdapter: SpinStateAdapter? = null
    var stateList: ArrayList<AllState>? = arrayListOf()
    var state = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_rto)
        init()
    }

    fun init() {
        presenter = RTOPresenter(this, this)
        presenter!!.loadStates()

        stateAdapter = SpinStateAdapter(this, stateList!!)
        spinState.setAdapter(stateAdapter)

        spinState?.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {

            }

            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                state = stateList!!.get(position).state_id
                presenter!!.loadData(
                    sessionManager.getToken(this@RTOActivity)!!,
                    state
                )
            }

        }
        val mLayoutManagerForItems = LinearLayoutManager(this)
        recyclerView.layoutManager = mLayoutManagerForItems
        recyclerView.itemAnimator = DefaultItemAnimator()
        adapter = RTOAdapter(rtoList!!)
        recyclerView.adapter = adapter
        adapter!!.setOnItemClickListener(object : RTOAdapter.OnItemClickListener {
            override fun onItemClick(view: View, obj: AllRule, position: Int) {

            }
        })
    }

    override fun showProgressbar() {
    }

    override fun hideProgressbar() {
    }

    override fun onSuccessgetStates(responseModel: Response<StateResponse>) {
        if (responseModel.body() != null) {
            stateList!!.clear()
            val allState: AllState? = AllState("0", "Select State")
            stateList!!.add(0, allState!!)
            stateList!!.addAll(responseModel.body()!!.AllStates)
            stateAdapter!!.notifyDataSetChanged()

        }
    }

    override fun onSuccess(responseModel: Response<RTOResponse>) {
        if (responseModel.body() != null) {
            rtoList!!.clear()
            rtoList!!.addAll(responseModel.body()!!.AllRules)
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