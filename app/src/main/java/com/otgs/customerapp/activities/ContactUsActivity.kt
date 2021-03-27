package com.otgs.customerapp.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.AdapterView
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.google.gson.JsonObject
import com.kuber.vpn.Utils.sessionManager
import com.otgs.customerapp.R
import com.otgs.customerapp.adapter.QueryAdapter
import com.otgs.customerapp.model.response.Allcat
import com.otgs.customerapp.model.response.ContactUsResponse
import com.otgs.customerapp.presenter.ContactPresenter
import com.otgs.customerapp.view.ContactView
import kotlinx.android.synthetic.main.activity_contactus.*
import retrofit2.Response

class ContactUsActivity : AppCompatActivity(), ContactView.MainView {

    var presenter: ContactPresenter? = null
    var catList: ArrayList<Allcat>? = arrayListOf()

    var category_id = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_contactus)
        setSupportActionBar(toolbar)
        toolbar.title = getString(R.string.chat)
        toolbar?.navigationIcon =
            ContextCompat.getDrawable(this, R.drawable.ic_baseline_arrow_back_24)
        toolbar.setNavigationOnClickListener {
            onBackPressed()
        }
        init ()
    }

    fun init() {
        presenter = ContactPresenter(this, this)
        presenter!!.loadData()

        spinQuery?.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {

            }

            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                category_id = catList!!.get(position).category_id
            }

        }
        rlSubmit.setOnClickListener {
            submit()
        }
    }

    fun submit() {
        if (TextUtils.isEmpty(txtDesc.text.toString().trim())) {
            txtDesc.setError("Enter description")
            txtDesc.requestFocus()
            return
        }

        presenter!!.submitData(
            sessionManager.getUser_ID(this)!!,
            sessionManager.getUserType(this)!!,
            txtDesc.text.toString().trim(),
            category_id
        )
    }

    override fun showProgressbar() {
        rlSubmit.isClickable = false
        txtSubmit.visibility = View.GONE
        progressBar.visibility = View.VISIBLE
    }

    override fun hideProgressbar() {
        rlSubmit.isClickable = true
        txtSubmit.visibility = View.VISIBLE
        progressBar.visibility = View.GONE

    }

    override fun onSuccess(responseModel: Response<ContactUsResponse>) {
        if (responseModel.body() != null) {
            txtEmail.text = responseModel.body()!!.email
            txtPhone.text = responseModel.body()!!.phone
            txtAddress.text = responseModel.body()!!.address
            txtSite.text = responseModel.body()!!.website

            catList!!.clear()
            catList!!.addAll(responseModel.body()!!.Allcat)
            val adapter = QueryAdapter(this, catList!!)
            spinQuery.setAdapter(adapter)
        }
    }

    override fun onSuccess1(responseModel: Response<JsonObject>) {
        if (responseModel.body() != null) {

            Toast.makeText(
                this!!,
                responseModel.body()!!.get("message").asString,
                Toast.LENGTH_LONG
            ).show()

            txtDesc.setText("")
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