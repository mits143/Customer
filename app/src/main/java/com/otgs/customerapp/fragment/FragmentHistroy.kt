package com.otgs.customerapp.fragment

import android.app.DatePickerDialog
import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.channelpartner.adapter.SpinBrandAdapter
import com.google.gson.JsonObject
import com.kuber.vpn.Utils.sessionManager
import com.otgs.customerapp.R
import com.otgs.customerapp.adapter.BookingAdapter
import com.otgs.customerapp.model.response.BookingResponse
import com.otgs.customerapp.model.response.BrandData
import com.otgs.customerapp.model.response.BrandResponse
import com.otgs.customerapp.model.response.OrderMaster
import com.otgs.customerapp.presenter.BookingPresenter
import com.otgs.customerapp.view.BookingView
import kotlinx.android.synthetic.main.activity_servicing_chart.llNoData
import kotlinx.android.synthetic.main.activity_servicing_chart.recyclerView
import kotlinx.android.synthetic.main.activity_servicing_chart.swipeRefresh
import kotlinx.android.synthetic.main.fragment_current_booking.*
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.*

class FragmentHistroy : Fragment(), BookingView.MainView {

    var presenter: BookingPresenter? = null
    var adapter: BookingAdapter? = null
    var bookingList: ArrayList<OrderMaster>? = arrayListOf()
    var adapterBrand: SpinBrandAdapter? = null
    var brandList: ArrayList<BrandData>? = arrayListOf()

    var vehicle_type = 0
    var brand = 0
    var status = 0
    var fromdate = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_current_booking, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        presenter = BookingPresenter(context, this)
        if (!TextUtils.isEmpty(sessionManager.getUser_ID(context)!!)) {
            presenter!!.loadBrandData(
                sessionManager.getToken(context)!!,
                sessionManager.getUser_ID(context)!!.toInt(),
                sessionManager.getUserType(context)!!.toInt()
            )
        }
        if (!TextUtils.isEmpty(sessionManager.getUser_ID(context)!!)) {
            presenter!!.loadData(
                sessionManager.getToken(context)!!,
                "",
                "",
                3,
                sessionManager.getUser_ID(context)!!.toInt(),
                sessionManager.getUserType(context)!!.toInt(),
                vehicle_type,
                brand,
                status
            )
        }

        swipeRefresh.setOnRefreshListener {
            if (!TextUtils.isEmpty(sessionManager.getUser_ID(context)!!)) {
                presenter!!.loadData(
                    sessionManager.getToken(context)!!,
                    "",
                    "",
                    3,
                    sessionManager.getUser_ID(context)!!.toInt(),
                    sessionManager.getUserType(context)!!.toInt(),
                    vehicle_type,
                    brand,
                    status
                )
            }
        }


        val c = Calendar.getInstance()
        txtFromDate.setOnClickListener {
            val year = c.get(Calendar.YEAR)
            val month = c.get(Calendar.MONTH)
            val day = c.get(Calendar.DAY_OF_MONTH)
            val dpd = DatePickerDialog(
                context!!,
                DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
                    // Display Selected date in TextView
                    fromdate = "" + (month) + "-" + dayOfMonth + "-" + year
                    txtFromDate.setText(" " + (month + 1) + "-" + dayOfMonth + "-" + year)
                    txtToDate.setText("")
                },
                year,
                month,
                day
            )
            dpd.datePicker.maxDate = c.timeInMillis;
            dpd.show()
        }

        txtToDate.setOnClickListener {
//            val getfromdate: String = edtFromDate.getText().toString().trim()
            if (!TextUtils.isEmpty(fromdate)) {
                val getfrom = fromdate.split("-".toRegex()).toTypedArray()
                val year1: Int
                val month1: Int
                val day1: Int
                year1 = getfrom[2].toInt()
                month1 = getfrom[0].toInt()
                day1 = getfrom[1].toInt()
                val c1 = Calendar.getInstance()
                c1[year1, month1] = day1
                val dpd = DatePickerDialog(
                    context!!,
                    DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
                        // Display Selected date in TextView
                        txtToDate.setText(" " + (month1 + 1) + "-" + dayOfMonth + "-" + year)
                    },
                    year1,
                    month1,
                    day1
                )
                dpd.datePicker.maxDate = c.timeInMillis;
                dpd.datePicker.minDate = c1.timeInMillis;
                dpd.show()
            } else {
                Toast.makeText(context!!, "First select from date", Toast.LENGTH_SHORT).show()
            }
        }



        spinVehicle?.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {

            }

            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                if (TextUtils.equals(
                        parent!!.getItemAtPosition(position).toString(),
                        "Select Vehicle"
                    )
                ) {
                    vehicle_type = 0
                }
                if (TextUtils.equals(
                        parent!!.getItemAtPosition(position).toString(),
                        "2 Wheeler"
                    )
                ) {
                    vehicle_type = 1
                }
                if (TextUtils.equals(
                        parent!!.getItemAtPosition(position).toString(),
                        "4 Wheeler"
                    )
                ) {
                    vehicle_type = 2
                }
            }

        }

        adapterBrand = SpinBrandAdapter(context!!, brandList!!)
        spinBrand.setAdapter(adapterBrand)

        spinBrand?.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {

            }

            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                brand = brandList!!.get(position).brand_id
            }

        }

        spinStatus?.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {

            }

            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                if (TextUtils.equals(
                        parent!!.getItemAtPosition(position).toString(),
                        "Select Status"
                    )
                ) {
                    status = 0
                }
                if (TextUtils.equals(
                        parent!!.getItemAtPosition(position).toString(),
                        "Accepted By Garage"
                    )
                ) {
                    status = 2
                }
                if (TextUtils.equals(
                        parent!!.getItemAtPosition(position).toString(),
                        "Rejected By Garage"
                    )
                ) {
                    status = 3
                }
            }

        }

        val layoutManager = LinearLayoutManager(context)
        recyclerView!!.setLayoutManager(layoutManager)
        adapter = BookingAdapter(bookingList!!)
        recyclerView!!.setAdapter(adapter)
        adapter!!.setOnItemClickListener(object : BookingAdapter.OnItemClickListener {
            override fun onItemClick(view: View, obj: OrderMaster, position: Int) {
                showDialog(obj)
            }
        })
    }

    override fun showProgressbar() {
        swipeRefresh.isRefreshing = true
    }

    override fun hideProgressbar() {
        swipeRefresh.isRefreshing = false
    }

    override fun onSuccess(responseModel: Response<BookingResponse>) {
        if (responseModel.body() != null && responseModel.body()!!.OrderMaster.size > 0) {
            llNoData.visibility = View.GONE
            bookingList!!.clear()
            bookingList!!.addAll(responseModel.body()!!.OrderMaster)
            adapter!!.notifyDataSetChanged()

        } else {
            bookingList!!.clear()
            llNoData.visibility = View.VISIBLE
        }
    }

    override fun onSuccessBrand(responseModel: Response<BrandResponse>) {
        if (responseModel.body() != null) {
            brandList!!.clear()
            val brand: BrandData? = BrandData(0, "Select Brand")
            brandList!!.add(0, brand!!)
            brandList!!.addAll(responseModel.body()!!.OrderMaster)
            adapterBrand!!.notifyDataSetChanged()

        }
    }

    override fun onSuccessAddRating(responseModel: Response<JsonObject>) {
        if (responseModel.body() != null) {
            Toast.makeText(
                context!!,
                responseModel.body()!!.get("message").asString,
                Toast.LENGTH_LONG
            ).show()
        }
    }

    override fun onError(errorCode: Int) {
        bookingList!!.clear()
        llNoData.visibility = View.VISIBLE
        if (errorCode == 500) {
            Toast.makeText(context, getString(R.string.internal_server_error), Toast.LENGTH_SHORT)
                .show()
        } else {
            Toast.makeText(context, getString(R.string.error), Toast.LENGTH_SHORT).show()
        }
    }

    override fun onError(throwable: Throwable) {
        bookingList!!.clear()
        llNoData.visibility = View.VISIBLE
        Toast.makeText(context, getString(R.string.error), Toast.LENGTH_SHORT).show()
    }

    override fun onDestroy() {
        super.onDestroy()
        presenter!!.onStop()
    }

    private fun showDialog(data: OrderMaster) {

        // custom dialog
        val dialog = Dialog(context!!)
        dialog.setContentView(R.layout.dialog_booking_detail)
        dialog.setCancelable(true)
        dialog.window!!.setLayout(
            WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.MATCH_PARENT
        )
        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        val txtOrderNo = dialog.findViewById(R.id.txtOrderNo) as TextView
        txtOrderNo.setText("Order No " + data.OrderNumber)
        val txtJobCard = dialog.findViewById(R.id.txtJobCard) as TextView
        val txtDate = dialog.findViewById(R.id.txtDate) as TextView
        txtDate.setText(data.date)
        val txtServiceType = dialog.findViewById(R.id.txtServiceType) as TextView
        txtServiceType.setText(data.date)
        val txtType = dialog.findViewById(R.id.txtType) as TextView
        txtType.setText(data.vehicle_type)
        val txtNumber = dialog.findViewById(R.id.txtNumber) as TextView
        txtNumber.setText(data.vehicle_Number)
        val txtGarageName = dialog.findViewById(R.id.txtGarageName) as TextView
        txtGarageName.setText(data.date)
        val txtAddress = dialog.findViewById(R.id.txtAddress) as TextView
        txtAddress.setText(data.date)
        val txtDrop = dialog.findViewById(R.id.txtDrop) as TextView
        txtDrop.setText(data.drop_time)
        val txtStatus = dialog.findViewById(R.id.txtStatus) as TextView
        txtStatus.setText(data.status)
        val llrating = dialog.findViewById(R.id.llrating) as LinearLayout
        txtStatus.visibility = View.VISIBLE

        llrating.setOnClickListener {
            showRatingDialog(data.order_no, data.order_no)
        }

        txtJobCard.setOnClickListener {

        }

        dialog.show()
    }

    private fun showRatingDialog(order_id: Int, sp_id: Int) {

        // custom dialog
        val dialog = Dialog(context!!)
        dialog.setContentView(R.layout.dialog_rating)
        dialog.setCancelable(true)
        dialog.window!!.setLayout(
            WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.MATCH_PARENT
        )
        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        val c = Calendar.getInstance().time
        val df = SimpleDateFormat("MM-dd-yyyy", Locale.getDefault())
        val formattedDate = df.format(c)
        val ratingBar2 = dialog.findViewById(R.id.ratingBar2) as RatingBar
        val edtReview = dialog.findViewById(R.id.txtJobCard) as TextView
        val review = edtReview.text.toString().toString()
        val submitButton = dialog.findViewById(R.id.txtServiceType) as TextView

        submitButton.setOnClickListener {
            if (TextUtils.isEmpty(review)) {
                edtReview.setError("Review cannot be empty")
                edtReview.requestFocus()
                return@setOnClickListener
            }
            val rating = ratingBar2.rating.toString()
            presenter!!.addRating(
                sessionManager.getToken(context!!)!!,
                formattedDate,
                order_id,
                rating.toInt(),
                review,
                sp_id,
                sessionManager.getUser_ID(context!!)!!.toInt(),
                sessionManager.getUserType(context!!)!!.toInt()
            )
        }

        dialog.show()
    }
}