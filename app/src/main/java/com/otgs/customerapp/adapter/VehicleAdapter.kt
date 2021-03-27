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
import com.otgs.customerapp.model.response.VehicleDetail
import kotlinx.android.synthetic.main.cell_vehicle.view.*
import java.util.*


class VehicleAdapter(val dataList: ArrayList<VehicleDetail>) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private lateinit var itemClickListener: OnItemClickListener
    private lateinit var context: Context

    interface OnItemClickListener {
        fun onItemClick(view: View, obj: VehicleDetail, position: Int)
    }

    fun setOnItemClickListener(mItemClickListener: OnItemClickListener) {
        this.itemClickListener = mItemClickListener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val itemView =
            LayoutInflater.from(parent.context).inflate(R.layout.cell_vehicle, parent, false)

        return PlaceViewHolder(itemView)
    }

    override fun onBindViewHolder(reholder: RecyclerView.ViewHolder, position: Int) {

        if (reholder is PlaceViewHolder) {
            val cats = dataList[position]

            reholder.txtBrand.text = cats.vehicle_type_name
            reholder.txtStatus.text = cats.vehicle_number

            reholder.itemView.setOnClickListener { view ->
                itemClickListener.onItemClick(view, dataList[position], position)
            }

        }

    }

    override fun getItemCount(): Int {
        return dataList.size
    }

    inner class PlaceViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var txtBrand: TextView = view.txtBrand
        var txtStatus: TextView = view.txtStatus


    }

}
