package com.otgs.customerapp.adapter

import android.content.Context
import android.graphics.Color
import android.graphics.PorterDuff
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.otgs.customerapp.R
import com.otgs.customerapp.model.response.AllRule
import com.otgs.customerapp.utils.Utils
import kotlinx.android.synthetic.main.app_directory_home_10_item.view.*
import kotlinx.android.synthetic.main.cell_emergency_contact.view.*

import java.util.*

class RTOAdapter(val dataList: ArrayList<AllRule>) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private lateinit var itemClickListener: OnItemClickListener
    private lateinit var context: Context

    interface OnItemClickListener {
        fun onItemClick(view: View, obj: AllRule, position: Int)
    }

    fun setOnItemClickListener(mItemClickListener: OnItemClickListener) {
        this.itemClickListener = mItemClickListener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.cell_emergency_contact, parent, false)

        return PlaceViewHolder(itemView)
    }

    override fun onBindViewHolder(reholder: RecyclerView.ViewHolder, position: Int) {

        if (reholder is PlaceViewHolder) {
            val cats = dataList[position]

            reholder.txtName.text = cats.ans
            reholder.txtMobile.text = cats.question
            reholder.txtCall.visibility = View.GONE

            reholder.txtCall.setOnClickListener { view ->

                itemClickListener.onItemClick(view, dataList[position], position)

            }

        }

    }

    override fun getItemCount(): Int {
        return dataList.size
    }

    inner class PlaceViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var txtName: TextView = view.txtName
        var txtMobile: TextView = view.txtMobile
        var txtCall: TextView = view.txtCall

    }

}
