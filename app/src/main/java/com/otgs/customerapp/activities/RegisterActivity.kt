package com.otgs.customerapp.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.view.View
import android.widget.AdapterView
import android.widget.Toast
import com.channelpartner.adapter.SpinCityAdapter
import com.channelpartner.adapter.SpinPincodeAdapter
import com.channelpartner.adapter.SpinStateAdapter
import com.google.gson.JsonObject
import com.kuber.vpn.Utils.sessionManager
import com.otgs.customerapp.R
import com.otgs.customerapp.model.response.*
import com.otgs.customerapp.presenter.RegisterPresenter
import com.otgs.customerapp.utils.Utils.DateDialog
import com.otgs.customerapp.utils.Utils.getDeviceID
import com.otgs.customerapp.utils.Utils.isValidEmail
import com.otgs.customerapp.view.RegisterView
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.activity_register.*
import kotlinx.android.synthetic.main.activity_register.edtEmail
import kotlinx.android.synthetic.main.activity_register.progressBar
import kotlinx.android.synthetic.main.activity_register.txtSkip
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import java.io.File

class RegisterActivity : AppCompatActivity(), RegisterView.MainView {

    var presenter: RegisterPresenter? = null
    var stateAdapter: SpinStateAdapter? = null
    var cityAdapter: SpinCityAdapter? = null
    var pincodeAdapter: SpinPincodeAdapter? = null

    var stateList: ArrayList<AllState>? = arrayListOf()
    var cityList: ArrayList<AllCity>? = arrayListOf()
    var pincodeList: ArrayList<AllPincode>? = arrayListOf()

    var gender = ""
    var state = ""
    var city = ""
    var pincode = ""

    var file0: File? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
        init()
    }

    fun init() {
        presenter = RegisterPresenter(this, this)
        presenter!!.loadStates()

        spinGender?.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {

            }

            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                gender = parent!!.getItemAtPosition(position).toString()
            }

        }

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
                presenter!!.loadCities(state)
            }

        }

        spinCity?.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {

            }

            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                city = cityList!!.get(position).city_id
                presenter!!.loadPincodes(city)
            }

        }

        spinPincode?.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {

            }

            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                pincode = pincodeList!!.get(position).pincode_id.toString()
            }

        }

        stateAdapter = SpinStateAdapter(this, stateList!!)
        spinState.setAdapter(stateAdapter)


        cityAdapter = SpinCityAdapter(this, cityList!!)
        spinCity.setAdapter(cityAdapter)


        pincodeAdapter = SpinPincodeAdapter(this, pincodeList!!)
        spinPincode.setAdapter(pincodeAdapter)

        edtDOB.setOnClickListener {
            DateDialog(this, edtDOB)
        }

        edtMobile1.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {
                if (p0!!.length == 10) {
                    txtMobile2.visibility = View.VISIBLE
                    edtMobile2.visibility = View.VISIBLE
                    txtRelation2.visibility = View.VISIBLE
                    edtRelation2.visibility = View.VISIBLE
                } else {
                    txtMobile2.visibility = View.GONE
                    edtMobile2.visibility = View.GONE
                    txtRelation2.visibility = View.GONE
                    edtRelation2.visibility = View.GONE
                }
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }
        })

        edtMobile2.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {
                if (p0!!.length == 10) {
                    txtMobile3.visibility = View.VISIBLE
                    edtMobile3.visibility = View.VISIBLE
                    txtRelation3.visibility = View.VISIBLE
                    edtRelation3.visibility = View.VISIBLE
                } else {
                    txtMobile3.visibility = View.GONE
                    edtMobile3.visibility = View.GONE
                    txtRelation3.visibility = View.GONE
                    edtRelation3.visibility = View.GONE
                }
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }
        })

        loginButton.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
        }
        helpButton.setOnClickListener {

        }
        txtSkip.setOnClickListener {
            finish()
        }
        rlOTP.setOnClickListener {
            if (edtMobile.text.toString().trim().length != 10) {
                edtMobile.error = getString(R.string.invalid_mobile)
                edtMobile.requestFocus()
                return@setOnClickListener
            }
            presenter!!.loadDataOTP(edtMobile.text.toString().trim())
        }
        rlRegister.setOnClickListener {
            submit()
        }
    }

    fun submit() {
        if (TextUtils.isEmpty(edtFname.text.toString().trim())) {
            edtFname.error = getString(R.string.fname_empty)
            edtFname.requestFocus()
            return
        }
        if (TextUtils.isEmpty(edtLname.text.toString().trim())) {
            edtLname.error = getString(R.string.lname_empty)
            edtLname.requestFocus()
            return
        }
        if (!isValidEmail(edtEmail.text.toString().trim())) {
            edtEmail.error = getString(R.string.invalid_email)
            edtEmail.requestFocus()
            return
        }
        if (edtMobile.text.toString().trim().length != 10) {
            edtMobile.error = getString(R.string.invalid_mobile)
            edtMobile.requestFocus()
            return
        }
        if (TextUtils.isEmpty(edtOTP.text.toString().trim())) {
            edtOTP.error = getString(R.string.otp_empty)
            edtOTP.requestFocus()
            return
        }
        if (TextUtils.isEmpty(edtDOB.text.toString().trim())) {
            edtDOB.error = getString(R.string.dob_empty)
            edtDOB.requestFocus()
            return
        }
        if (TextUtils.isEmpty(edtAddress.text.toString().trim())) {
            edtAddress.error = getString(R.string.address_empty)
            edtAddress.requestFocus()
            return
        }
        if (TextUtils.isEmpty(edtLandmark.text.toString().trim())) {
            edtLandmark.error = getString(R.string.landmark_empty)
            edtLandmark.requestFocus()
            return
        }
        if (TextUtils.isEmpty(edtMobile1.text.toString().trim())) {
            edtMobile1.error = getString(R.string.mobile1_empty)
            edtMobile1.requestFocus()
            return
        }
        if (TextUtils.isEmpty(edtRelation.text.toString().trim())) {
            edtRelation.error = getString(R.string.relation1_empty)
            edtRelation.requestFocus()
            return
        }
        if (TextUtils.isEmpty(edtMobile2.text.toString().trim())) {
            edtMobile2.error = getString(R.string.mobile2_empty)
            edtMobile2.requestFocus()
            return
        }
        if (TextUtils.isEmpty(edtRelation2.text.toString().trim())) {
            edtRelation2.error = getString(R.string.relation2_empty)
            edtRelation2.requestFocus()
            return
        }
        if (TextUtils.isEmpty(edtMobile3.text.toString().trim())) {
            edtMobile3.error = getString(R.string.mobile3_empty)
            edtMobile3.requestFocus()
            return
        }
        if (TextUtils.isEmpty(edtRelation3.text.toString().trim())) {
            edtRelation3.error = getString(R.string.relation3_empty)
            edtRelation3.requestFocus()
            return
        }
        if (TextUtils.equals(gender, "0")) {
            Toast.makeText(this, "Select gender", Toast.LENGTH_LONG)
                .show()
            return
        }
        if (TextUtils.equals(state, "0")) {
            Toast.makeText(this, "Select state", Toast.LENGTH_LONG)
                .show()
            return
        }
        if (TextUtils.equals(city, "0")) {
            Toast.makeText(this, "Select city", Toast.LENGTH_LONG)
                .show()
            return
        }
        if (TextUtils.equals(pincode, "0")) {
            Toast.makeText(this, "Select pincode", Toast.LENGTH_LONG)
                .show()
            return
        }
        if (!chkTerm.isChecked) {
            Toast.makeText(this, "Accept our terms & condition to continue", Toast.LENGTH_LONG)
                .show()
            return
        }
        var body0: MultipartBody.Part? = null
        if (file0 != null) {
            val reqFile0: RequestBody =
                RequestBody.create(
                    MediaType.parse("multipart/form-data"),
                    file0!!
                )
            body0 = MultipartBody.Part.createFormData(
                "profile",
                file0!!.name,
                reqFile0
            )
        } else {
            val attachmentEmpty =
                RequestBody.create(MediaType.parse("text/plain"), "")
            body0 = MultipartBody.Part.createFormData(
                "profile",
                "",
                attachmentEmpty
            );
        }

        val address: RequestBody =
            RequestBody.create(
                MultipartBody.FORM,
                edtAddress.text.toString().trim()
            )
        val dob: RequestBody =
            RequestBody.create(
                MultipartBody.FORM,
                edtDOB.text.toString().trim()
            )
        val email_id: RequestBody =
            RequestBody.create(
                MultipartBody.FORM,
                edtEmail.text.toString().trim()
            )
        val emergency_contact1: RequestBody =
            RequestBody.create(
                MultipartBody.FORM,
                edtMobile1.text.toString().trim()
            )
        val emergency_contact2: RequestBody =
            RequestBody.create(
                MultipartBody.FORM,
                edtMobile2.text.toString().trim()
            )
        val emergency_contact3: RequestBody =
            RequestBody.create(
                MultipartBody.FORM,
                edtMobile3.text.toString().trim()
            )
        val first_name: RequestBody =
            RequestBody.create(
                MultipartBody.FORM,
                edtFname.text.toString().trim()
            )
        val last_name: RequestBody =
            RequestBody.create(
                MultipartBody.FORM,
                edtLname.text.toString().trim()
            )
        val gender: RequestBody =
            RequestBody.create(
                MultipartBody.FORM,
                gender
            )
        val mobile_no: RequestBody =
            RequestBody.create(
                MultipartBody.FORM,
                edtMobile.text.toString().trim()
            )
        val pincode: RequestBody =
            RequestBody.create(
                MultipartBody.FORM,
                pincode
            )
        val relation1: RequestBody =
            RequestBody.create(
                MultipartBody.FORM,
                edtRelation.text.toString().trim()
            )
        val relation2: RequestBody =
            RequestBody.create(
                MultipartBody.FORM,
                edtRelation2.text.toString().trim()
            )
        val relation3: RequestBody =
            RequestBody.create(
                MultipartBody.FORM,
                edtRelation3.text.toString().trim()
            )
        val state_id: RequestBody =
            RequestBody.create(
                MultipartBody.FORM,
                state
            )
        val city_id: RequestBody =
            RequestBody.create(
                MultipartBody.FORM,
                city
            )
        val device_id: RequestBody =
            RequestBody.create(
                MultipartBody.FORM,
                getDeviceID(this)
            )

        presenter!!.loadDataRegister(
            body0,
            address,
            dob,
            email_id,
            emergency_contact1,
            emergency_contact2,
            emergency_contact3,
            first_name,
            last_name,
            gender,
            mobile_no,
            pincode,
            relation1,
            relation2,
            relation3,
            state_id,
            city_id,
            device_id
        )
    }

    override fun showProgressbar(int: Int) {
        if (int == 4) {
            rlOTP.isClickable = false
            txtOTP.visibility = View.GONE
            progressBar1.visibility = View.VISIBLE
        }
        if (int == 5) {
            rlRegister.isClickable = false
            txtRegister.visibility = View.GONE
            progressBar.visibility = View.VISIBLE
        }
    }

    override fun hideProgressbar(int: Int) {
        if (int == 4) {
            rlOTP.isClickable = true
            txtOTP.visibility = View.VISIBLE
            progressBar1.visibility = View.GONE
        }
        if (int == 5) {
            rlRegister.isClickable = true
            txtRegister.visibility = View.VISIBLE
            progressBar.visibility = View.GONE
        }
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

    override fun onSuccessgetCities(responseModel: Response<CityReponse>) {
        if (responseModel.body() != null) {
            cityList!!.clear()
            val allCity: AllCity? = AllCity("0", "Select City")
            cityList!!.add(0, allCity!!)
            cityList!!.addAll(responseModel.body()!!.AllCities)
            cityAdapter!!.notifyDataSetChanged()

        }
    }

    override fun onSuccessgetPincodes(responseModel: Response<PincodeResponse>) {
        if (responseModel.body() != null) {
            pincodeList!!.clear()
            val allPincode: AllPincode? = AllPincode("Select Pincode", 0)
            pincodeList!!.add(0, allPincode!!)
            pincodeList!!.addAll(responseModel.body()!!.AllPincodes)
            pincodeAdapter!!.notifyDataSetChanged()
        }
    }

    override fun onSuccessOTP(responseModel: Response<JsonObject>) {
        if (responseModel.body() != null) {
            Toast.makeText(
                this,
                responseModel.body()!!.get("message").asString,
                Toast.LENGTH_LONG
            )
                .show()
        }
    }

    override fun onSuccessRegister(responseModel: Response<LoginResponse>) {
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