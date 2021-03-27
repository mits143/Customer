package com.otgs.customerapp.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.Toast
import com.kuber.vpn.Utils.sessionManager
import com.otgs.customerapp.R
import com.otgs.customerapp.model.response.LoginResponse
import com.otgs.customerapp.presenter.LoginPresenter
import com.otgs.customerapp.utils.Utils.getDeviceID
import com.otgs.customerapp.utils.Utils.isValidEmail
import com.otgs.customerapp.view.LoginView
import kotlinx.android.synthetic.main.activity_login.*
import retrofit2.Response

class LoginActivity : AppCompatActivity(), LoginView.MainView {

    var presenter: LoginPresenter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        init()
    }

    fun init() {
        presenter = LoginPresenter(this, this)
        forgotTextView.setOnClickListener {
            startActivity(Intent(this, ForgotPwdActivity::class.java))
        }
        signuptTextView.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
        }
        txtSkip.setOnClickListener(View.OnClickListener {
            finish()
        })
        rlLogin.setOnClickListener(View.OnClickListener {
            submit()
        })
    }

    fun submit() {
        if (TextUtils.isEmpty(edtEmail.text.toString().trim())) {
            edtEmail.setError(getString(R.string.email_empty))
            edtEmail.requestFocus()
            return
        }
        if (!isValidEmail(edtEmail.text.toString().trim())) {
            edtEmail.setError(getString(R.string.invalid_email))
            edtEmail.requestFocus()
            return
        }
        if (TextUtils.isEmpty(edtPwd.text.toString().trim())) {
            edtPwd.setError(getString(R.string.pwd_empty))
            edtPwd.requestFocus()
            return
        }
        presenter!!.loadData(
            edtEmail.text.toString().trim(),
            edtPwd.text.toString().trim(),
            "",
            "",
            "4",
            getDeviceID(this)
        )
    }

    override fun showProgressbar() {
        rlLogin.isClickable = false
        txtLogin.visibility = View.GONE
        progressBar.visibility = View.VISIBLE
    }

    override fun hideProgressbar() {
        rlLogin.isClickable = true
        txtLogin.visibility = View.VISIBLE
        progressBar.visibility = View.GONE
    }

    override fun onSuccess(responseModel: Response<LoginResponse>) {
        if (responseModel.body() != null) {
            sessionManager.setUser_ID(this, responseModel.body()!!.userid)
            sessionManager.setUserType(this, responseModel.body()!!.usertype)
            sessionManager.setEmail(this, responseModel.body()!!.email)
            sessionManager.setName(
                this,
                responseModel.body()!!.firstname + " " + responseModel.body()!!.lastname
            )
            sessionManager.setUnique_No(this, responseModel.body()!!.uniqueNo)
            sessionManager.setis_first_atempt(this, responseModel.body()!!.is_first_atempt)
            sessionManager.setMobile_No(this, responseModel.body()!!.mobile_no)
            sessionManager.setPofile_photo(this, responseModel.body()!!.profile_photo)
            sessionManager.setToken(this, responseModel.body()!!.token)
            finish()
        }
    }

    override fun onError(errorCode: Int) {
        if (errorCode == 401) {
            edtEmail.setError(getString(R.string.invalid_username_password))
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