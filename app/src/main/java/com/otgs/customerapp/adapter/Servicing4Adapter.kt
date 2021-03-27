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
import com.otgs.customerapp.model.response.FourWheeler
import com.otgs.customerapp.utils.Utils
import kotlinx.android.synthetic.main.cell_garage.view.*
import kotlinx.android.synthetic.main.cell_servicing_chart.view.*
import java.util.*



class Servicing4Adapter(val dataList: ArrayList<FourWheeler>) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private lateinit var itemClickListener: OnItemClickListener
    private lateinit var context: Context

    interface OnItemClickListener {
        fun onItemClick(view: View, obj: FourWheeler, position: Int)
    }

    fun setOnItemClickListener(mItemClickListener: OnItemClickListener) {
        this.itemClickListener = mItemClickListener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val itemView =
            LayoutInflater.from(parent.context)
                .inflate(R.layout.cell_servicing_chart, parent, false)

        return PlaceViewHolder(itemView)
    }

    override fun onBindViewHolder(reholder: RecyclerView.ViewHolder, position: Int) {

        if (reholder is PlaceViewHolder) {
            val cats = dataList[position]

            reholder.txtKM.text = cats.km_range
            reholder.txtNo.text = cats.servicing_number

            reholder.itemView.setOnClickListener { view ->
                itemClickListener.onItemClick(view, dataList[position], position)
            }

        }

    }

    override fun getItemCount(): Int {
        return dataList.size
    }

    inner class PlaceViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var txtKM: TextView = view.txtKM
        var txtNo: TextView = view.txtNo
    }

}
