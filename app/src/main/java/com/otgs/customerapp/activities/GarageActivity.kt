package com.otgs.customerapp.activities

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.location.Location
import android.location.LocationManager
import android.os.Bundle
import android.os.Looper
import android.provider.Settings
import android.text.TextUtils
import android.view.View
import android.widget.AdapterView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.gms.location.*
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.CircleOptions
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.kuber.vpn.Utils.sessionManager
import com.otgs.customerapp.R
import com.otgs.customerapp.adapter.GarageAdapter
import com.otgs.customerapp.adapter.SOSAdapter
import com.otgs.customerapp.model.response.GarageInformationData
import com.otgs.customerapp.model.response.GarageResponse
import com.otgs.customerapp.model.response.SOSEmergencyContact
import com.otgs.customerapp.presenter.GaragePresenter
import com.otgs.customerapp.presenter.SOSPresenter
import com.otgs.customerapp.view.GarageView
import kotlinx.android.synthetic.main.activity_map.*
import kotlinx.android.synthetic.main.activity_map.recyclerView
import kotlinx.android.synthetic.main.activity_sos.*
import retrofit2.Response


class GarageActivity : AppCompatActivity(), OnMapReadyCallback, GarageView.MainView {

    var supportMapFragment: SupportMapFragment? = null
    val PERMISSION_ID = 100
    private var mMap: GoogleMap? = null
    lateinit var mFusedLocationClient: FusedLocationProviderClient
    var lat = ""
    var lng = ""
    var radius = ""
    var service_id = ""
    var flag = 0
    var circleOptions: CircleOptions? = null
    var presenter: GaragePresenter? = null

    var adapter: GarageAdapter? = null
    var garageList: ArrayList<GarageInformationData>? = arrayListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_map)
        init()
    }

    fun init() {
        presenter = GaragePresenter(this, this)
        if (intent.extras != null) {
            service_id = intent.extras!!.getString("serviceID")!!
        }

        val mLayoutManagerForItems = LinearLayoutManager(this)
        recyclerView.layoutManager = mLayoutManagerForItems
        recyclerView.itemAnimator = DefaultItemAnimator()
        adapter = GarageAdapter(garageList!!)
        recyclerView.adapter = adapter
        adapter!!.setOnItemClickListener(object : GarageAdapter.OnItemClickListener {
            override fun onItemClick(view: View, obj: GarageInformationData, position: Int) {
                startActivity(
                    Intent(
                        this@GarageActivity,
                        GarageDetailActivity::class.java
                    ).putExtra("service_id", obj.sp_id)
                )
            }
        })
        supportMapFragment =
            supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        supportMapFragment!!.getMapAsync(this)


        chkFlag.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                flag = 1
            } else {
                flag = 0
            }
        }
    }

    override fun onMapReady(googleMap: GoogleMap?) {

        mMap = googleMap
//        if (ActivityCompat.checkSelfPermission(
//                this,
//                Manifest.permission.ACCESS_FINE_LOCATION
//            ) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
//                this,
//                Manifest.permission.ACCESS_COARSE_LOCATION
//            ) == PackageManager.PERMISSION_GRANTED
//        ) {
//            mMap!!.isMyLocationEnabled = true
//            return
//        }
        getLastLocation()

        spinRadius?.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {

            }

            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                radius = parent!!.getItemAtPosition(position).toString()
                mMap!!.clear()
                if (TextUtils.equals(radius, "Select Radius")) {
                    radius = "0"
                }
                if (TextUtils.equals(radius, "5")) {
                    drawCircle(LatLng(lat.toDouble(), lng.toDouble()), 5000)

                }
                if (TextUtils.equals(radius, "10")) {
                    drawCircle(LatLng(lat.toDouble(), lng.toDouble()), 5000)
                    drawCircle(LatLng(lat.toDouble(), lng.toDouble()), 10000)
                }
                if (TextUtils.equals(radius, "15")) {
                    drawCircle(LatLng(lat.toDouble(), lng.toDouble()), 5000)
                    drawCircle(LatLng(lat.toDouble(), lng.toDouble()), 10000)
                    drawCircle(LatLng(lat.toDouble(), lng.toDouble()), 15000)
                }
                if (TextUtils.equals(radius, "20")) {
                    drawCircle(LatLng(lat.toDouble(), lng.toDouble()), 5000)
                    drawCircle(LatLng(lat.toDouble(), lng.toDouble()), 10000)
                    drawCircle(LatLng(lat.toDouble(), lng.toDouble()), 15000)
                    drawCircle(LatLng(lat.toDouble(), lng.toDouble()), 20000)
                }
                presenter!!.loadData(
                    sessionManager.getToken(this@GarageActivity)!!,
                    lat.toDouble(),
                    lng.toDouble(),
                    flag,
                    radius,
                    service_id,
                    sessionManager.getUser_ID(this@GarageActivity)!!,
                    sessionManager.getUserType(this@GarageActivity)!!,
                    0
                )
            }

        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<out String>
        , grantedResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantedResults)
        when (requestCode) {
            PERMISSION_ID -> {
                if ((grantedResults.isNotEmpty() && grantedResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    getLastLocation()
                }
            }
        }
    }

    @SuppressLint("MissingPermission")
    private fun getLastLocation() {
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        if (checkPermissions()) {
            if (isLocationEnabled()) {
                mFusedLocationClient.lastLocation.addOnCompleteListener(this) { task ->
                    var location: Location? = task.result
                    if (location == null) {
                        requestNewLocationData()
                    } else {
                        lat = location.latitude.toString()
                        lng = location.longitude.toString()

                        val latlng = LatLng(lat.toDouble(), lng.toDouble())
                        drawCircle(latlng, 0)
                    }
                }
            } else {
                Toast.makeText(this, "Turn on location", Toast.LENGTH_LONG).show()
                val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
                startActivityForResult(intent, PERMISSION_ID)
            }
        } else {
            requestPermissions()
        }
    }

    @SuppressLint("MissingPermission")
    private fun requestNewLocationData() {
        var mLocationRequest = LocationRequest()
        mLocationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        mLocationRequest.interval = 0
        mLocationRequest.fastestInterval = 0
        mLocationRequest.numUpdates = 1

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        mFusedLocationClient!!.requestLocationUpdates(
            mLocationRequest, mLocationCallback,
            Looper.myLooper()
        )
    }

    private val mLocationCallback = object : LocationCallback() {
        override fun onLocationResult(locationResult: LocationResult) {
            var mLastLocation: Location = locationResult.lastLocation
            lat = mLastLocation.latitude.toString()
            lng = mLastLocation.longitude.toString()

            val latlng = LatLng(lat.toDouble(), lng.toDouble())
            drawCircle(latlng, 0)
        }
    }

    private fun isLocationEnabled(): Boolean {
        var locationManager: LocationManager =
            getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(
            LocationManager.NETWORK_PROVIDER
        )
    }

    private fun checkPermissions(): Boolean {
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED &&
            ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED &&
            ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.READ_EXTERNAL_STORAGE
            ) == PackageManager.PERMISSION_GRANTED &&
            ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            return true
        }
        return false
    }

    private fun requestPermissions() {
        ActivityCompat.requestPermissions(
            this,
            arrayOf(
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            ),
            PERMISSION_ID
        )
    }


    private fun drawCircle(point: LatLng, radius: Int) {

        // mMap.clear();
        circleOptions = CircleOptions()
        circleOptions!!.center(point)
        circleOptions!!.radius(radius.toDouble())
        circleOptions!!.strokeColor(Color.BLACK)
        circleOptions!!.fillColor(0x30F34F07)
        circleOptions!!.strokeWidth(2f)
        mMap!!.addMarker(MarkerOptions().position(point).title("You Are Here"))
        mMap!!.moveCamera(CameraUpdateFactory.newLatLng(point))
        mMap!!.animateCamera(CameraUpdateFactory.zoomTo(11f))
        mMap!!.addCircle(circleOptions)
    }

    override fun showProgressbar() {

    }

    override fun hideProgressbar() {

    }

    override fun onSuccess(responseModel: Response<GarageResponse>) {
        if (responseModel.body() != null && responseModel.body()!!.garageInformation.size > 0) {
            garageList!!.clear()
            garageList!!.addAll(responseModel.body()!!.garageInformation)
            adapter!!.notifyDataSetChanged()
            for (i in garageList!!.indices) {
                val contact_latitude: Double = garageList!![i].lat.toDouble()
                val contact_longitude: Double = garageList!![i].lng.toDouble()
                val garage_name: String = garageList!![i].garage_name

                val sydney = LatLng(contact_latitude, contact_longitude)
                val marker = MarkerOptions().position(LatLng(contact_latitude, contact_longitude))
                    .title("$garage_name Garage")
                marker.icon(BitmapDescriptorFactory.fromResource(R.drawable.baseline_address))
                mMap!!.addMarker(marker)
                mMap!!.moveCamera(CameraUpdateFactory.newLatLng(sydney))
                val cameraUpdate = CameraUpdateFactory.newLatLngZoom(sydney, 10f)
                mMap!!.animateCamera(cameraUpdate)
                mMap!!.isTrafficEnabled = true
                mMap!!.isIndoorEnabled = true
                mMap!!.isBuildingsEnabled = true
                mMap!!.uiSettings.isZoomControlsEnabled = true
                mMap!!.addCircle(circleOptions)
            }
        } else {
            garageList!!.clear()
        }
    }

    override fun onError(errorCode: Int) {
        garageList!!.clear()
    }

    override fun onError(throwable: Throwable) {
        garageList!!.clear()
    }
}