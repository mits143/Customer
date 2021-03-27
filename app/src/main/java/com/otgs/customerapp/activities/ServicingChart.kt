package com.otgs.customerapp.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.kuber.vpn.Utils.sessionManager
import com.otgs.customerapp.R
import com.otgs.customerapp.adapter.Servicing2Adapter
import com.otgs.customerapp.adapter.Servicing4Adapter
import com.otgs.customerapp.model.response.FourWheeler
import com.otgs.customerapp.model.response.ServicingResponse
import com.otgs.customerapp.model.response.TwoWheeler
import com.otgs.customerapp.presenter.ServicingPresenter
import com.otgs.customerapp.view.ServicingView
import kotlinx.android.synthetic.main.activity_servicing_chart.*
import retrofit2.Response

class ServicingChart : AppCompatActivity(), ServicingView.MainView {

    var presenter: ServicingPresenter? = null
    var adapter2wheel: Servicing2Adapter? = null
    var adapter4wheel: Servicing4Adapter? = null
    var twoWheelList: ArrayList<TwoWheeler>? = arrayListOf()
    var fourWheelList: ArrayList<FourWheeler>? = arrayListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_servicing_chart)
        setSupportActionBar(toolbar)
        toolbar.title = getString(R.string.servicing_charts)
        toolbar?.navigationIcon =
            ContextCompat.getDrawable(this, R.drawable.ic_baseline_arrow_back_24)
        toolbar.setNavigationOnClickListener {
            onBackPressed()
        }
        init()
    }

    fun init() {
        presenter = ServicingPresenter(this, this)

        presenter!!.loadData(
            sessionManager.getToken(this)!!,
            sessionManager.getUser_ID(this)!!,
            sessionManager.getUserType(this)!!
        )

        swipeRefresh.setOnRefreshListener {
            presenter!!.loadData(
                sessionManager.getToken(this)!!,
                sessionManager.getUser_ID(this)!!,
                sessionManager.getUserType(this)!!
            )
        }
        cvTwoWheel.setOnClickListener(View.OnClickListener {
            cvTwoWheel.setCardBackgroundColor(resources.getColor(R.color.colorPrimary))
            txtTwoWheel.setTextColor(resources.getColor(R.color.md_white_1000))
            cvFourWheel.setCardBackgroundColor(resources.getColor(R.color.md_white_1000))
            txtFourWheel.setTextColor(resources.getColor(R.color.md_black_1000))
            rlTwoWheeler.visibility = View.VISIBLE
            rlFourWheeler.visibility = View.GONE
        })
        cvFourWheel.setOnClickListener(View.OnClickListener {
            cvTwoWheel.setCardBackgroundColor(resources.getColor(R.color.md_white_1000))
            txtTwoWheel.setTextColor(resources.getColor(R.color.md_black_1000))
            cvFourWheel.setCardBackgroundColor(resources.getColor(R.color.colorPrimary))
            txtFourWheel.setTextColor(resources.getColor(R.color.md_white_1000))
            rlTwoWheeler.visibility = View.GONE
            rlFourWheeler.visibility = View.VISIBLE
        })
        val layoutManager = LinearLayoutManager(this)
        recyclerView!!.setLayoutManager(layoutManager)
        adapter2wheel = Servicing2Adapter(twoWheelList!!)
        recyclerView!!.setAdapter(adapter2wheel)

        val layoutManager1 = LinearLayoutManager(this)
        recyclerView1!!.setLayoutManager(layoutManager1)
        adapter4wheel = Servicing4Adapter(fourWheelList!!)
        recyclerView1!!.setAdapter(adapter4wheel)


    }

    override fun showProgressbar() {
        swipeRefresh.isRefreshing = true
    }

    override fun hideProgressbar() {
        swipeRefresh.isRefreshing = false
    }

    override fun onSuccess(responseModel: Response<ServicingResponse>) {
        if (responseModel.body() != null && responseModel.body()!!.two_wheeler.size > 0) {
            llNoData.visibility = View.GONE
            twoWheelList!!.clear()
            twoWheelList!!.addAll(responseModel.body()!!.two_wheeler)
            adapter2wheel!!.notifyDataSetChanged()

            llNoData1.visibility = View.GONE
            fourWheelList!!.clear()
            fourWheelList!!.addAll(responseModel.body()!!.four_wheeler)
            adapter4wheel!!.notifyDataSetChanged()

        } else {
            twoWheelList!!.clear()
            llNoData.visibility = View.VISIBLE
            fourWheelList!!.clear()
            llNoData1.visibility = View.VISIBLE
        }
    }

    override fun onError(errorCode: Int) {
        twoWheelList!!.clear()
        llNoData.visibility = View.VISIBLE
        fourWheelList!!.clear()
        llNoData1.visibility = View.VISIBLE
        if (errorCode == 500) {
            Toast.makeText(this, getString(R.string.internal_server_error), Toast.LENGTH_SHORT)
                .show()
        } else {
            Toast.makeText(this, getString(R.string.error), Toast.LENGTH_SHORT).show()
        }
    }

    override fun onError(throwable: Throwable) {
        twoWheelList!!.clear()
        llNoData.visibility = View.VISIBLE
        fourWheelList!!.clear()
        llNoData1.visibility = View.VISIBLE
        Toast.makeText(this, getString(R.string.error), Toast.LENGTH_SHORT).show()
    }

    override fun onDestroy() {
        super.onDestroy()
        presenter!!.onStop()
    }
}