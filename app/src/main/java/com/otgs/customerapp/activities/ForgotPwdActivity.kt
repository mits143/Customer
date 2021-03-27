package com.otgs.customerapp.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.Toast
import com.google.gson.JsonObject
import com.otgs.customerapp.R
import com.otgs.customerapp.presenter.ForgotPasswordPresenter
import com.otgs.customerapp.view.ForgotPasswordView
import kotlinx.android.synthetic.main.activity_forgot_pwd.*
import kotlinx.android.synthetic.main.activity_forgot_pwd.edtEmail
import kotlinx.android.synthetic.main.activity_forgot_pwd.progressBar
import kotlinx.android.synthetic.main.activity_login.*
import retrofit2.Response

class ForgotPwdActivity : AppCompatActivity(), ForgotPasswordView.MainView {

    var presenter: ForgotPasswordPresenter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forgot_pwd)
        init()
    }

    fun init() {
        presenter = ForgotPasswordPresenter(this, this)
        rlReset.setOnClickListener(View.OnClickListener {
            submit()
        })
    }

    fun submit() {
        if (TextUtils.isEmpty(edtEmail.text.toString().trim())) {
            edtEmail.setError(getString(R.string.email_empty))
            edtEmail.requestFocus()
            return
        }
        presenter!!.loadData(edtEmail.text.toString().trim(), "4")
    }

    override fun showProgressbar() {
        rlReset.isClickable = false
        resetButton.visibility = View.GONE
        progressBar.visibility = View.VISIBLE
    }

    override fun hideProgressbar() {
        rlReset.isClickable = true
        resetButton.visibility = View.VISIBLE
        progressBar.visibility = View.GONE
    }

    override fun onSuccess(responseModel: Response<JsonObject>) {
        if (responseModel.body() != null) {
            Toast.makeText(this, responseModel.body()!!.get("message").asString, Toast.LENGTH_SHORT)
                .show()
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }
    }

    override fun onError(errorCode: Int) {
        if (errorCode == 401) {
            edtEmail.setError(getString(R.string.invalid_email))
            edtEmail.requestFocus()
        } else if (errorCode == 500) {
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