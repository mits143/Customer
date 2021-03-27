package com.otgs.customerapp.activities

import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.TextView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DecodeFormat
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.channelpartner.adapter.SpinVehicleBrandAdapter
import com.channelpartner.adapter.SpinVehicleSubTypeAdapter
import com.channelpartner.adapter.SpinVehicleTypeAdapter
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.gson.JsonObject
import com.kuber.vpn.Utils.sessionManager
import com.otgs.customerapp.R
import com.otgs.customerapp.adapter.ImagesAdapter
import com.otgs.customerapp.model.response.*
import com.otgs.customerapp.presenter.AddVehiclePresenter
import com.otgs.customerapp.utils.Utils.OPERATION_CAPTURE_PHOTO
import com.otgs.customerapp.utils.Utils.OPERATION_CHOOSE_MULTIPLE_PHOTO
import com.otgs.customerapp.utils.Utils.OPERATION_CHOOSE_PHOTO
import com.otgs.customerapp.utils.Utils.capturePhoto
import com.otgs.customerapp.utils.Utils.getPath
import com.otgs.customerapp.utils.Utils.openGallery
import com.otgs.customerapp.utils.Utils.openGallery1
import com.otgs.customerapp.utils.Utils.prepareFilePart
import com.otgs.customerapp.utils.Utils.source
import com.otgs.customerapp.view.AddVehicleView
import kotlinx.android.synthetic.main.activity_add_vehicle.*
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import java.io.File
import java.net.URISyntaxException

class AddVehicle : AppCompatActivity(), AddVehicleView.MainView {

    var presenter: AddVehiclePresenter? = null
    var spinVehicleTypeAdapter: SpinVehicleTypeAdapter? = null
    var spinVehicleSubTypeAdapter: SpinVehicleSubTypeAdapter? = null
    var spinVehicleBrandAdapter: SpinVehicleBrandAdapter? = null

    var regAdapter: ImagesAdapter? = null
    var insAdapter: ImagesAdapter? = null
    var otherAdapter: ImagesAdapter? = null

    var vehicleTypeList: ArrayList<VehicleTypeData>? = arrayListOf()
    var vehicleSubTypeList: ArrayList<VehicleSubTypeData>? = arrayListOf()
    var vehicleBrandList: ArrayList<VehicleBrandData>? = arrayListOf()

    var vehicle_brand = 0
    var vehicle_sub_type = 0
    var vehicle_type = 0

    var drivingLicense: File? = null
    var puc: File? = null

    var registration_paper: ArrayList<Uri> = arrayListOf()
    var insurance: ArrayList<Uri> = arrayListOf()
    var other: ArrayList<Uri> = arrayListOf()

    var parts: ArrayList<MultipartBody.Part> = arrayListOf()
    var parts1: ArrayList<MultipartBody.Part> = arrayListOf()
    var parts2: ArrayList<MultipartBody.Part> = arrayListOf()

    var type = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_vehicle)
        setSupportActionBar(toolbar)
        toolbar.title = getString(R.string.add_vehicle)
        toolbar?.navigationIcon =
            ContextCompat.getDrawable(this, R.drawable.ic_baseline_arrow_back_24)
        toolbar.setNavigationOnClickListener {
            onBackPressed()
        }
        init()
    }

    fun init() {
        presenter = AddVehiclePresenter(this, this)

        presenter!!.loadVehicleTypeData(
            sessionManager.getToken(this@AddVehicle)!!
        )

        spinVehicleType?.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {

            }

            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                vehicle_type = vehicleTypeList!![position].vehicle_type_id
                presenter!!.loadVehicleSubTypeData(
                    sessionManager.getToken(this@AddVehicle)!!,
                    vehicle_type.toString()
                )
            }

        }

        spinVehicleSubType?.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {

            }

            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                vehicle_sub_type = vehicleSubTypeList!![position].vehicle_subtype_id
                presenter!!.loadVehicleBrandData(
                    sessionManager.getToken(this@AddVehicle)!!,
                    vehicle_sub_type.toString()
                )
            }

        }

        spinVehicleBrand?.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {

            }

            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                vehicle_brand = vehicleBrandList!![position].vehicle_brand_id
            }

        }

        spinVehicleTypeAdapter = SpinVehicleTypeAdapter(this, vehicleTypeList!!)
        spinVehicleType.setAdapter(spinVehicleTypeAdapter)

        spinVehicleSubTypeAdapter = SpinVehicleSubTypeAdapter(this, vehicleSubTypeList!!)
        spinVehicleSubType.setAdapter(spinVehicleSubTypeAdapter)

        spinVehicleBrandAdapter = SpinVehicleBrandAdapter(this, vehicleBrandList!!)
        spinVehicleBrand.setAdapter(spinVehicleBrandAdapter)

        val layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        rvRegistrationPaper!!.setLayoutManager(layoutManager)
        regAdapter = ImagesAdapter(this, registration_paper!!)
        rvRegistrationPaper!!.setAdapter(regAdapter)

        val layoutManager1 = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        rvInsurance!!.setLayoutManager(layoutManager1)
        insAdapter = ImagesAdapter(this, insurance!!)
        rvInsurance!!.setAdapter(insAdapter)

        val layoutManager2 = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        rvOthers!!.setLayoutManager(layoutManager2)
        otherAdapter = ImagesAdapter(this, other!!)
        rvOthers!!.setAdapter(otherAdapter)

        imgDrivingLicense.setOnClickListener {
            type = 0
            showDialog()
        }

        imgPUC.setOnClickListener {
            type = 1
            showDialog()
        }

        imgRegAdd.setOnClickListener {
            type = 2
            openGallery1(this)
        }

        imgInsAdd.setOnClickListener {
            type = 3
            openGallery1(this)
        }

        imgOtherAdd.setOnClickListener {
            type = 4
            openGallery1(this)
        }

        txtSubmit.setOnClickListener {
            submit()
        }
    }


    fun submit() {
        if (vehicle_type == 0) {
            Toast.makeText(this, "Select Vehicle Type", Toast.LENGTH_LONG).show()
            return
        }
        if (vehicle_sub_type == 0) {
            Toast.makeText(this, "Select Vehicle SubType", Toast.LENGTH_LONG).show()
            return
        }
        if (vehicle_brand == 0) {
            Toast.makeText(this, "Select Vehicle Brand", Toast.LENGTH_LONG).show()
            return
        }

        if (TextUtils.isEmpty(edtVehicleNumber.text.toString().trim())) {
            edtVehicleNumber.setError("Enter vehicle number")
            edtVehicleNumber.requestFocus()
            return
        }

        val user_Id: RequestBody =
            RequestBody.create(
                MultipartBody.FORM,
                sessionManager.getUser_ID(this).toString()
            )

        val user_type: RequestBody =
            RequestBody.create(
                MultipartBody.FORM,
                sessionManager.getUserType(this).toString()
            )

        val vehicle_type: RequestBody =
            RequestBody.create(
                MultipartBody.FORM,
                vehicle_type.toString()
            )

        val vehicle_sub_type: RequestBody =
            RequestBody.create(
                MultipartBody.FORM,
                vehicle_sub_type.toString()
            )

        val vehicle_brand: RequestBody =
            RequestBody.create(
                MultipartBody.FORM,
                vehicle_brand.toString()
            )

        val vehicle_number: RequestBody =
            RequestBody.create(
                MultipartBody.FORM,
                edtVehicleNumber.text.toString().trim()
            )

        val vehicle_details_id: RequestBody =
            RequestBody.create(
                MultipartBody.FORM,
                "0"
            )

        var body0: MultipartBody.Part? = null
        if (drivingLicense != null) {
            val reqFile0: RequestBody =
                RequestBody.create(MediaType.parse("multipart/form-data"), drivingLicense!!)
            body0 = MultipartBody.Part.createFormData(
                "driving_licence",
                drivingLicense!!.name,
                reqFile0
            )
        } else {
            val attachmentEmpty =
                RequestBody.create(MediaType.parse("text/plain"), "")
            body0 = MultipartBody.Part.createFormData("driving_licence", "", attachmentEmpty);
        }

        var body1: MultipartBody.Part? = null
        if (puc != null) {
            val reqFile1: RequestBody =
                RequestBody.create(MediaType.parse("multipart/form-data"), puc!!)
            body1 = MultipartBody.Part.createFormData("puc", puc!!.name, reqFile1)
        } else {
            val attachmentEmpty =
                RequestBody.create(MediaType.parse("text/plain"), "")
            body1 = MultipartBody.Part.createFormData("puc", "", attachmentEmpty);
        }

        if (registration_paper != null) {
            parts.clear()
            for (i in registration_paper.indices) {
                parts.add(prepareFilePart(this, "registration_paper", registration_paper.get(i))!!)
            }
        }

        if (insurance != null) {
            parts1.clear()
            for (i in insurance.indices) {
                parts1.add(prepareFilePart(this, "insurance", insurance.get(i))!!)
            }
        }

        if (other != null) {
            parts2.clear()
            for (i in other.indices) {
                parts2.add(prepareFilePart(this, "other", other.get(i))!!)
            }
        }

        presenter!!.loadData(
            sessionManager.getToken(this)!!,
            user_Id,
            user_type,
            vehicle_type,
            vehicle_number,
            vehicle_sub_type,
            vehicle_brand,
            vehicle_details_id,
            body0,
            body1,
            parts,
            parts1,
            parts2
        )

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
                            if (type == 0) {
                                drivingLicense = source
                                Glide.with(this).load(source!!.getAbsolutePath())
                                    .apply(requestOptions).into(imgDrivingLicense)
                            }
                            if (type == 1) {
                                puc = source
                                Glide.with(this).load(source!!.getAbsolutePath())
                                    .apply(requestOptions).into(imgPUC)
                            }
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
                                    if (type == 0) {
                                        drivingLicense = file
                                        Glide.with(this).load(contentURI)
                                            .apply(requestOptions).into(imgDrivingLicense)
                                    }
                                    if (type == 1) {
                                        puc = file
                                        Glide.with(this).load(contentURI)
                                            .apply(requestOptions).into(imgPUC)
                                    }

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
            OPERATION_CHOOSE_MULTIPLE_PHOTO ->
                if (resultCode == Activity.RESULT_OK) {
                    if (type == 2) {
                        registration_paper.clear()
                        if (data!!.clipData != null) {
                            val count: Int = data!!.clipData!!.itemCount
                            var currentItem = 0
                            while (currentItem < count) {
                                val imageUri: Uri =
                                    data!!.clipData!!.getItemAt(currentItem).uri
                                currentItem = currentItem + 1
                                Log.d("Uri Selected", imageUri.toString())
                                try {
                                    registration_paper.add(imageUri)
                                    regAdapter!!.notifyDataSetChanged()
                                } catch (e: Exception) {
                                    Log.e("TAG", "File select error", e)
                                }
                            }
                        } else if (data.getData() != null) {
                            val uri: Uri = data.data!!
                            try {
                                registration_paper.add(uri)
                                regAdapter!!.notifyDataSetChanged()
                            } catch (e: java.lang.Exception) {
                                Log.e("TAG", "File select error", e)
                            }
                        }
                    }
                    if (type == 3) {
                        insurance.clear()
                        if (data!!.clipData != null) {
                            val count: Int = data!!.clipData!!.itemCount
                            var currentItem = 0
                            while (currentItem < count) {
                                val imageUri: Uri =
                                    data!!.clipData!!.getItemAt(currentItem).uri
                                currentItem = currentItem + 1
                                Log.d("Uri Selected", imageUri.toString())
                                try {
                                    insurance.add(imageUri)
                                    insAdapter!!.notifyDataSetChanged()
                                } catch (e: Exception) {
                                    Log.e("TAG", "File select error", e)
                                }
                            }
                        } else if (data.getData() != null) {
                            val uri: Uri = data.data!!
                            try {
                                insurance.add(uri)
                                insAdapter!!.notifyDataSetChanged()
                            } catch (e: java.lang.Exception) {
                                Log.e("TAG", "File select error", e)
                            }
                        }
                    }
                    if (type == 4) {
                        other.clear()
                        if (data!!.clipData != null) {
                            val count: Int = data!!.clipData!!.itemCount
                            var currentItem = 0
                            while (currentItem < count) {
                                val imageUri: Uri =
                                    data!!.clipData!!.getItemAt(currentItem).uri
                                currentItem = currentItem + 1
                                Log.d("Uri Selected", imageUri.toString())
                                try {
                                    other.add(imageUri)
                                    otherAdapter!!.notifyDataSetChanged()
                                } catch (e: Exception) {
                                    Log.e("TAG", "File select error", e)
                                }
                            }
                        } else if (data.getData() != null) {
                            val uri: Uri = data.data!!
                            try {
                                other.add(uri)
                                otherAdapter!!.notifyDataSetChanged()
                            } catch (e: java.lang.Exception) {
                                Log.e("TAG", "File select error", e)
                            }
                        }
                    }
                }
        }
    }

    override fun showProgressbar() {

    }

    override fun hideProgressbar() {

    }

    override fun onSuccessVehicleType(responseModel: Response<VehicleTypeResponse>) {
        if (responseModel.body() != null) {
            vehicleTypeList!!.clear()
            val data = VehicleTypeData(0, "Select Vehicle Type")
            vehicleTypeList!!.add(0, data!!)
            vehicleTypeList!!.addAll(responseModel.body()!!.All)
            spinVehicleTypeAdapter!!.notifyDataSetChanged()
        }
    }

    override fun onSuccessVehicleSubType(responseModel: Response<VehicleSubTypeResponse>) {
        if (responseModel.body() != null) {
            vehicleSubTypeList!!.clear()
            val data = VehicleSubTypeData(0, "Select Vehicle SubType")
            vehicleSubTypeList!!.add(0, data!!)
            vehicleSubTypeList!!.addAll(responseModel.body()!!.All)
            spinVehicleSubTypeAdapter!!.notifyDataSetChanged()
        }

    }

    override fun onSuccessVehicleBrand(responseModel: Response<VehicleBrandResponse>) {
        if (responseModel.body() != null) {
            vehicleBrandList!!.clear()
            val data = VehicleBrandData(0, "Select Vehicle Brand")
            vehicleBrandList!!.add(0, data!!)
            vehicleBrandList!!.addAll(responseModel.body()!!.All)
            spinVehicleBrandAdapter!!.notifyDataSetChanged()
        }

    }

    override fun onSuccess(responseModel: Response<JsonObject>) {
        if (responseModel.body() != null) {
            finish()
        }
    }

    override fun onError(errorCode: Int) {

    }

    override fun onError(throwable: Throwable) {

    }

    override fun onDestroy() {
        super.onDestroy()
        presenter!!.onStop()
    }
}