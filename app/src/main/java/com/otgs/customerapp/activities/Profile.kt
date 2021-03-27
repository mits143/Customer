package com.otgs.customerapp.activities

import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.text.TextUtils
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.bumptech.glide.load.DecodeFormat
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.channelpartner.adapter.SpinCityAdapter
import com.channelpartner.adapter.SpinPincodeAdapter
import com.channelpartner.adapter.SpinStateAdapter
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.gson.JsonObject
import com.kuber.vpn.Utils.sessionManager
import com.otgs.customerapp.R
import com.otgs.customerapp.model.response.*
import com.otgs.customerapp.presenter.ProfileDetailPresenter
import com.otgs.customerapp.utils.Utils
import com.otgs.customerapp.utils.Utils.DateDialog
import com.otgs.customerapp.utils.Utils.OPERATION_CAPTURE_PHOTO
import com.otgs.customerapp.utils.Utils.OPERATION_CHOOSE_PHOTO
import com.otgs.customerapp.utils.Utils.capturePhoto
import com.otgs.customerapp.utils.Utils.getDeviceID
import com.otgs.customerapp.utils.Utils.getPath
import com.otgs.customerapp.utils.Utils.loadImage
import com.otgs.customerapp.utils.Utils.openGallery
import com.otgs.customerapp.utils.Utils.source
import com.otgs.customerapp.view.ProfileDetailView
import kotlinx.android.synthetic.main.activity_profile.*
import kotlinx.android.synthetic.main.activity_profile.edtAddress
import kotlinx.android.synthetic.main.activity_profile.edtDOB
import kotlinx.android.synthetic.main.activity_profile.edtLandmark
import kotlinx.android.synthetic.main.activity_profile.edtMobile1
import kotlinx.android.synthetic.main.activity_profile.edtMobile2
import kotlinx.android.synthetic.main.activity_profile.edtMobile3
import kotlinx.android.synthetic.main.activity_profile.edtRelation2
import kotlinx.android.synthetic.main.activity_profile.edtRelation3
import kotlinx.android.synthetic.main.activity_profile.spinCity
import kotlinx.android.synthetic.main.activity_profile.spinPincode
import kotlinx.android.synthetic.main.activity_profile.spinState
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import java.io.File
import java.net.URISyntaxException

class Profile : AppCompatActivity(), ProfileDetailView.MainView {

    var presenter: ProfileDetailPresenter? = null
    var stateAdapter: SpinStateAdapter? = null
    var cityAdapter: SpinCityAdapter? = null
    var pincodeAdapter: SpinPincodeAdapter? = null
    var isBackAllowed: Boolean? = false

    var stateList: ArrayList<AllState>? = arrayListOf()
    var cityList: ArrayList<AllCity>? = arrayListOf()
    var pincodeList: ArrayList<AllPincode>? = arrayListOf()

    private var edit: MenuItem? = null
    private var done: MenuItem? = null

    var file0: File? = null
    var state = ""
    var city = ""
    var pincode = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)
        setSupportActionBar(toolbar)
        toolbar.title = getString(R.string.profile)
        toolbar?.navigationIcon =
            ContextCompat.getDrawable(this, R.drawable.ic_baseline_arrow_back_24)
        toolbar.setNavigationOnClickListener {
            onBackPressed()
        }
        init()
    }

    fun init() {
        presenter = ProfileDetailPresenter(this, this)
        presenter!!.loadDataProfileDetail(

            sessionManager.getToken(this)!!,
            sessionManager.getUser_ID(this)!!
        )

        stateAdapter = SpinStateAdapter(this, stateList!!)
        spinState.setAdapter(stateAdapter)


        cityAdapter = SpinCityAdapter(this, cityList!!)
        spinCity.setAdapter(cityAdapter)


        pincodeAdapter = SpinPincodeAdapter(this, pincodeList!!)
        spinPincode.setAdapter(pincodeAdapter)

        edtDOB.setOnClickListener {
            DateDialog(this, edtDOB)
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

        profileImageView.setOnClickListener {
            showDialog()
        }

        editProfile(false)
    }

    fun editProfile(isEditable: Boolean) {
        if (!isEditable) {
            edtAddress.isFocusableInTouchMode = false
            edtAddress.clearFocus()
            edtLandmark.isFocusableInTouchMode = false
            edtLandmark.clearFocus()
            spinState.visibility = View.VISIBLE
            spinState.isEnabled = false
            spinCity.visibility = View.VISIBLE
            spinCity.isEnabled = false
            spinPincode.visibility = View.VISIBLE
            spinPincode.isEnabled = false
            edtMobile1.isFocusableInTouchMode = false
            edtMobile1.clearFocus()
            edtRelation1.isFocusableInTouchMode = false
            edtRelation1.clearFocus()
            edtMobile2.isFocusableInTouchMode = false
            edtMobile2.clearFocus()
            edtRelation2.isFocusableInTouchMode = false
            edtRelation2.clearFocus()
            edtMobile3.isFocusableInTouchMode = false
            edtMobile3.clearFocus()
            edtRelation3.isFocusableInTouchMode = false
            edtRelation3.clearFocus()
        } else {
            edtAddress.isFocusableInTouchMode = true
            edtLandmark.isFocusableInTouchMode = true
            spinState.visibility = View.VISIBLE
            spinState.isEnabled = true
            spinCity.visibility = View.VISIBLE
            spinCity.isEnabled = true
            spinPincode.visibility = View.VISIBLE
            spinPincode.isEnabled = true
            edtMobile1.isFocusableInTouchMode = true
            edtRelation1.isFocusableInTouchMode = true
            edtMobile2.isFocusableInTouchMode = true
            edtRelation2.isFocusableInTouchMode = true
            edtMobile3.isFocusableInTouchMode = true
            edtRelation3.isFocusableInTouchMode = true
        }
    }

    private fun showDialog() {
        val dialogView: View = layoutInflater.inflate(R.layout.dialog_image, null)
        val dialog = BottomSheetDialog(this)
        dialog.setCanceledOnTouchOutside(true)
        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.setContentView(dialogView)

        val txtTakePhoto = dialogView.findViewById(R.id.txtTakePhoto) as TextView
        val txtChoosePhoto = dialogView.findViewById(R.id.txtChoosePhoto) as TextView

        txtTakePhoto.setOnClickListener {
            dialog.dismiss()
            capturePhoto(this)
        }

        txtChoosePhoto.setOnClickListener {
            dialog.dismiss()
            val checkSelfPermission = ContextCompat.checkSelfPermission(
                this,
                android.Manifest.permission.WRITE_EXTERNAL_STORAGE
            ) + ContextCompat.checkSelfPermission(
                this,
                android.Manifest.permission.READ_EXTERNAL_STORAGE
            )
            if (checkSelfPermission != PackageManager.PERMISSION_GRANTED) {
                //Requests permissions to be granted to this application at runtime
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(
                        android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        android.Manifest.permission.READ_EXTERNAL_STORAGE
                    ), 1
                )
            } else {
                openGallery(this)
            }
        }
        dialog.show()

    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.edit_menu, menu)
        edit = menu.findItem(R.id.action_edit);
        done = menu.findItem(R.id.action_done);
        done!!.isVisible = false
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_edit -> {
                editProfile(true)
                edit!!.isVisible = false
                done!!.isVisible = true
                true
            }
            R.id.action_done -> {
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

                val user_id: RequestBody =
                    RequestBody.create(
                        MultipartBody.FORM,
                        sessionManager.getUser_ID(this)!!
                    )

                val user_type: RequestBody =
                    RequestBody.create(
                        MultipartBody.FORM,
                        sessionManager.getUserType(this)!!
                    )

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
                        txtEmail.text.toString().trim()
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
                        nameTextView.text.toString().trim()
                    )
                val last_name: RequestBody =
                    RequestBody.create(
                        MultipartBody.FORM,
                        ""
                    )
                val gender: RequestBody =
                    RequestBody.create(
                        MultipartBody.FORM,
                        txtGender.text.toString().trim()
                    )
                val mobile_no: RequestBody =
                    RequestBody.create(
                        MultipartBody.FORM,
                        txtMobile.text.toString().trim()
                    )
                val pincode: RequestBody =
                    RequestBody.create(
                        MultipartBody.FORM,
                        pincode
                    )
                val relation1: RequestBody =
                    RequestBody.create(
                        MultipartBody.FORM,
                        edtRelation1.text.toString().trim()
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

                presenter!!.editProfile(
                    sessionManager.getToken(this)!!,
                    body0,
                    user_id,
                    user_type,
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
                    city_id
                )
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<out String>
        , grantedResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantedResults)
        when (requestCode) {
            1 ->
                if (grantedResults.isNotEmpty() && grantedResults.get(0) ==
                    PackageManager.PERMISSION_GRANTED
                ) {
                    openGallery(this)
                } else {
                    //show("Unfortunately You are Denied Permission to Perform this Operataion.")
                }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            OPERATION_CAPTURE_PHOTO ->
                if (resultCode == Activity.RESULT_OK) {
                    var requestOptions = RequestOptions()
                    requestOptions = requestOptions.format(DecodeFormat.PREFER_ARGB_8888)
                    requestOptions = requestOptions.diskCacheStrategy(DiskCacheStrategy.ALL)

                    if (source != null) {
                        if (source!!.exists()) {
                            file0 = source

                            loadImage(this, false, 1, source!!.getAbsolutePath(), profileImageView)
                        } else {
                            Toast.makeText(
                                this,
                                "file not found",
                                Toast.LENGTH_LONG
                            ).show()

                        }
                    }
                }
            OPERATION_CHOOSE_PHOTO ->
                if (resultCode == Activity.RESULT_OK) {
                    var requestOptions = RequestOptions()
                    requestOptions = requestOptions.format(DecodeFormat.PREFER_ARGB_8888)
                    requestOptions = requestOptions.diskCacheStrategy(DiskCacheStrategy.ALL)
                    if (Build.VERSION.SDK_INT >= 19) {
                        try {
                            val contentURI: Uri? = data!!.data
                            var path = getPath(this, contentURI!!)!!
                            val file = File(path)
                            if (file != null) {
                                if (file!!.exists()) {
                                    file0 = file
//                                    Glide.with(this).load(contentURI)
//                                        .apply(requestOptions).into(imgProfile)

                                    loadImage(this, 1, contentURI, profileImageView)
                                } else {
                                    Toast.makeText(
                                        this,
                                        "file not found",
                                        Toast.LENGTH_LONG
                                    ).show()

                                }
                            }
                        } catch (e: URISyntaxException) {
                            e.printStackTrace()
                        }
                    }
                }
        }
    }

    override fun showProgressbar() {
//        progressBar.visibility = View.VISIBLE
    }

    override fun hideProgressbar() {
//        progressBar.visibility = View.GONE
    }

    override fun onSuccess(responseModel: Response<ProfileDetailResponse>) {
        if (responseModel.body() != null) {
            loadImage(
                this,
                true,
                1,
                responseModel.body()!!.Customer.profile_image,
                profileImageView
            )
            loadImage(
                this,
                true,
                1,
                responseModel.body()!!.Customer.profile_image,
                coverUserImageView
            )
            nameTextView.setText(responseModel.body()!!.Customer.first_name.trim() + responseModel.body()!!.Customer.last_name.trim())
            txtEmail.setText(responseModel.body()!!.Customer.email_id.trim())
            edtDOB.setText(responseModel.body()!!.Customer.dob.trim())
            edtAddress.setText(responseModel.body()!!.Customer.address.trim())
            if (TextUtils.equals(responseModel.body()!!.Customer.gender, "1"))
                txtGender.text = getString(R.string.male)
            else
                txtGender.text = getString(R.string.female)
            edtLandmark.setText(responseModel.body()!!.Customer.address.trim())
            state = responseModel.body()!!.Customer.state_id.trim()
            city = responseModel.body()!!.Customer.city_id.trim()
            pincode = responseModel.body()!!.Customer.pincode.trim()
            edtMobile1.setText(responseModel.body()!!.Customer.emergency_contact1.trim())
            edtMobile2.setText(responseModel.body()!!.Customer.emergency_contact2.trim())
            edtMobile3.setText(responseModel.body()!!.Customer.emergency_contact3.trim())
            edtRelation1.setText(responseModel.body()!!.Customer.relation1.trim())
            edtRelation2.setText(responseModel.body()!!.Customer.relation2.trim())
            edtRelation3.setText(responseModel.body()!!.Customer.relation3.trim())

            presenter!!.loadStates()

            isBackAllowed = true
        }
    }

    override fun onSuccessEditProfile(int: Int, responseModel: Response<JsonObject>) {
        if (responseModel.body() != null) {
            Toast.makeText(
                this,
                "Profile Updated succesfully",
                Toast.LENGTH_SHORT
            ).show()
            isBackAllowed = true
            editProfile(false)
            edit!!.isVisible = true
            done!!.isVisible = false
        }
    }

    override fun onSuccessgetStates(responseModel: Response<StateResponse>) {
        if (responseModel.body() != null) {
            stateList!!.clear()
            val allState: AllState? = AllState("0", "Select State")
            stateList!!.add(0, allState!!)
            stateList!!.addAll(responseModel.body()!!.AllStates)

            for (i in stateList!!.indices) {
                if (TextUtils.equals(
                        stateList!![i].state_id,
                        state
                    )
                ) {
                    spinState.setSelection(stateList!![i].state_id.toInt())
                }
            }

            stateAdapter!!.notifyDataSetChanged()

        }
    }

    override fun onSuccessgetCities(responseModel: Response<CityReponse>) {
        if (responseModel.body() != null) {
            cityList!!.clear()
            val allCity: AllCity? = AllCity("0", "Select City")
            cityList!!.add(0, allCity!!)
            cityList!!.addAll(responseModel.body()!!.AllCities)

            for (i in cityList!!.indices) {
                if (TextUtils.equals(
                        cityList!![i].city_id,
                        city
                    )
                ) {
                    spinCity.setSelection(cityList!![i].city_id.toInt())
                }
            }

            cityAdapter!!.notifyDataSetChanged()

        }
    }

    override fun onSuccessgetPincodes(responseModel: Response<PincodeResponse>) {
        if (responseModel.body() != null) {
            pincodeList!!.clear()
            val allPincode: AllPincode? = AllPincode("Select Pincode", 0)
            pincodeList!!.add(0, allPincode!!)
            pincodeList!!.addAll(responseModel.body()!!.AllPincodes)

            for (i in pincodeList!!.indices) {
                if (TextUtils.equals(
                        pincodeList!![i].pincode_id.toString(),
                        pincode
                    )
                ) {
                    spinPincode.setSelection(pincodeList!![i].pincode_id)
                }
            }

            pincodeAdapter!!.notifyDataSetChanged()
        }
    }

    override fun onError(errorCode: Int) {
        if (errorCode == 500) {
            Toast.makeText(this, getString(R.string.internal_server_error), Toast.LENGTH_SHORT)
                .show()
        } else {
            Toast.makeText(this, getString(R.string.error), Toast.LENGTH_SHORT).show()
        }
        isBackAllowed = true

    }

    override fun onError(throwable: Throwable) {
        Toast.makeText(this, getString(R.string.error), Toast.LENGTH_SHORT).show()
        isBackAllowed = true
    }

    override fun onDestroy() {
        super.onDestroy()
        presenter!!.onStop()
    }
}