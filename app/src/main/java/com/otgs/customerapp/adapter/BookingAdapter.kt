package com.otgs.customerapp.adapter

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import com.otgs.customerapp.R
import com.otgs.customerapp.model.response.OrderMaster
import com.otgs.customerapp.utils.Utils
import kotlinx.android.synthetic.main.cell_booking.view.*
import kotlinx.android.synthetic.main.cell_garage.view.*
import java.util.*


class BookingAdapter(val dataList: ArrayList<OrderMaster>) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private lateinit var itemClickListener: OnItemClickListener
    private lateinit var context: Context

    interface OnItemClickListener {
        fun onItemClick(view: View, obj: OrderMaster, position: Int)
    }

    fun setOnItemClickListener(mItemClickListener: OnItemClickListener) {
        this.itemClickListener = mItemClickListener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val itemView =
            LayoutInflater.from(parent.context).inflate(R.layout.cell_booking, parent, false)

        return PlaceViewHolder(itemView)
    }

    override fun onBindViewHolder(reholder: RecyclerView.ViewHolder, position: Int) {

        if (reholder is PlaceViewHolder) {
            val cats = dataList[position]

            reholder.txtOrderNo.text = "Order No." + cats.OrderNumber
            reholder.txtDate.text = cats.date
            reholder.txtType.text = cats.vehicle_type
            reholder.txtNumber.text = cats.vehicle_Number
            reholder.txtBrand.text = cats.brands
            reholder.txtStatus.text = cats.status

            reholder.itemView.setOnClickListener { view ->
                itemClickListener.onItemClick(view, dataList[position], position)
            }

        }

    }

    override fun getItemCount(): Int {
        return dataList.size
    }

    inner class PlaceViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var txtOrderNo: TextView = view.txtOrderNo
        var txtDate: TextView = view.txtDate
        var txtType: TextView = view.txtType
        var txtNumber: TextView = view.txtNumber
        var txtBrand: TextView = view.txtBrand
        var txtStatus: TextView = view.txtStatus


    }

}
