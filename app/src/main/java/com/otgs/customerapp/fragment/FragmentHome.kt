package com.otgs.customerapp.fragment

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.GridLayoutManager
import com.otgs.customerapp.R
import com.otgs.customerapp.activities.GarageActivity
import com.otgs.customerapp.activities.RTOActivity
import com.otgs.customerapp.activities.SOSActivity
import com.otgs.customerapp.adapter.HomeAdapter
import com.otgs.customerapp.model.response.AutoHubResponse
import com.otgs.customerapp.model.response.ServiceMasterLists
import com.otgs.customerapp.presenter.HomePresenter
import com.otgs.customerapp.utils.Utils
import com.otgs.customerapp.view.HomeView
import kotlinx.android.synthetic.main.fragment_home.*
import retrofit2.Response

class FragmentHome : Fragment(), HomeView.MainView {

    // data and featuredAdapter

    var presenter: HomePresenter? = null
    var adapter: HomeAdapter? = null
    var serviceMainList: ArrayList<ServiceMasterLists>? = arrayListOf()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initUI(view)
    }

    private fun initUI(view: View) {
        presenter = HomePresenter(context, this)
        presenter!!.loadData()

        Utils.setImageToImageView(context!!, imageView, R.drawable.user_profile_man)

        Utils.setCircleImageToImageView(context!!, imageView, R.drawable.user_profile_man, 0, 0)

        val mLayoutManagerForItems = GridLayoutManager(activity?.applicationContext, 3)
        recyclerView.layoutManager = mLayoutManagerForItems
        recyclerView.itemAnimator = DefaultItemAnimator()
        adapter = HomeAdapter(serviceMainList!!)
        recyclerView.adapter = adapter
        adapter!!.setOnItemClickListener(object : HomeAdapter.OnItemClickListener {
            override fun onItemClick(view: View, obj: ServiceMasterLists, position: Int) {
                if (TextUtils.equals(obj.service_master_id, "21")) {
                    context!!.startActivity(Intent(context, SOSActivity::class.java))
                    return
                }
                if (TextUtils.equals(obj.service_master_id, "20")) {
                    context!!.startActivity(Intent(context, RTOActivity::class.java))
                    return
                }
                context!!.startActivity(
                    Intent(
                        context,
                        GarageActivity::class.java
                    ).putExtra("serviceID", obj.service_master_id)
                )
            }
        })

    }

    override fun showProgressbar() {
//        progressBar.visibility = View.VISIBLE
    }

    override fun hideProgressbar() {
//        progressBar.visibility = View.GONE
    }

    override fun onSuccess(responseModel: Response<AutoHubResponse>) {
        if (responseModel.body() != null) {
            serviceMainList!!.addAll(responseModel.body()!!.ServiceMasterLists)
            adapter!!.notifyDataSetChanged()
        }


    }

    override fun onError(errorCode: Int) {
        if (errorCode == 500) {
            Toast.makeText(context, getString(R.string.internal_server_error), Toast.LENGTH_SHORT)
                .show()
        } else {
            Toast.makeText(context, getString(R.string.error), Toast.LENGTH_SHORT).show()
        }
    }

    override fun onError(throwable: Throwable) {
        Toast.makeText(context, getString(R.string.error), Toast.LENGTH_SHORT).show()

    }

    override fun onDestroy() {
        super.onDestroy()
        presenter!!.onStop()
    }
}