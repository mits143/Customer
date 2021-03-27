package com.otgs.customerapp.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.otgs.customerapp.R
import com.otgs.customerapp.model.response.RatingReview
import kotlinx.android.synthetic.main.cell_reveiw.view.*
import java.util.*


class RatingAdapter(val dataList: ArrayList<RatingReview>) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private lateinit var itemClickListener: OnItemClickListener
    private lateinit var context: Context

    interface OnItemClickListener {
        fun onItemClick(view: View, obj: RatingReview, position: Int)
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

            reholder.txtType.text = "Order No." + cats.garage_name
            reholder.txtDate.text = "Order No." + cats.date
            reholder.txtNumber.text = cats.rating.toString()
            reholder.txtStatus.text = cats.review

            reholder.itemView.setOnClickListener { view ->
                itemClickListener.onItemClick(view, dataList[position], position)
            }

        }

    }

    override fun getItemCount(): Int {
        return dataList.size
    }

    inner class PlaceViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var txtDate: TextView = view.txtDate
        var txtType: TextView = view.txtType
        var txtNumber: TextView = view.txtNumber
        var txtStatus: TextView = view.txtStatus


    }

}
