package com.otgs.customerapp.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import com.kuber.vpn.Utils.sessionManager
import com.otgs.customerapp.R
import com.otgs.customerapp.adapter.RatingAdapter
import com.otgs.customerapp.adapter.SOSAdapter
import com.otgs.customerapp.model.response.RatingListResponse
import com.otgs.customerapp.model.response.RatingReview
import com.otgs.customerapp.model.response.SOSEmergencyContact
import com.otgs.customerapp.model.response.SOSResponse
import com.otgs.customerapp.presenter.RatingListPresenter
import com.otgs.customerapp.presenter.SOSPresenter
import com.otgs.customerapp.view.RatingListView
import kotlinx.android.synthetic.main.activity_rating_list.*
import kotlinx.android.synthetic.main.activity_rating_list.llNoData
import kotlinx.android.synthetic.main.activity_servicing_chart.*
import kotlinx.android.synthetic.main.activity_sos.*
import kotlinx.android.synthetic.main.activity_sos.recyclerView
import retrofit2.Response

class RatingListActivity : AppCompatActivity(), RatingListView.MainView {

    var presenter: RatingListPresenter? = null
    var adapter: RatingAdapter? = null
    var ratingList: ArrayList<RatingReview>? = arrayListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_rating_list)
        init()
    }

    fun init() {
        presenter = RatingListPresenter(this, this)
        presenter!!.loadData(
            sessionManager.getToken(this)!!,
            "",
            0,
            "",
            sessionManager.getUser_ID(this)!!.toInt(),
            sessionManager.getUserType(this)!!.toInt()
        )
        val mLayoutManagerForItems = LinearLayoutManager(this)
        recyclerView.layoutManager = mLayoutManagerForItems
        recyclerView.itemAnimator = DefaultItemAnimator()
        adapter = RatingAdapter(ratingList!!)
        recyclerView.adapter = adapter
        adapter!!.setOnItemClickListener(object : RatingAdapter.OnItemClickListener {
            override fun onItemClick(view: View, obj: RatingReview, position: Int) {

            }
        })
    }

    override fun showProgressbar() {
    }

    override fun hideProgressbar() {

    }

    override fun onSuccess(responseModel: Response<RatingListResponse>) {
        if (responseModel.body() != null && responseModel.body()!!.RatingReview.size > 0) {
            ratingList!!.clear()
            ratingList!!.addAll(responseModel.body()!!.RatingReview)
            adapter!!.notifyDataSetChanged()
        } else {
            ratingList!!.clear()
            llNoData.visibility = View.VISIBLE
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
        ratingList!!.clear()
        llNoData.visibility = View.VISIBLE
        Toast.makeText(this, getString(R.string.error), Toast.LENGTH_SHORT).show()

    }

    override fun onDestroy() {
        ratingList!!.clear()
        llNoData.visibility = View.VISIBLE
        super.onDestroy()
        presenter!!.onStop()
    }
}