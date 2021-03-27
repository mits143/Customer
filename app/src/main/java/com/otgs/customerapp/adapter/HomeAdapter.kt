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
import com.otgs.customerapp.model.response.ServiceMasterLists
import com.otgs.customerapp.utils.Utils
import kotlinx.android.synthetic.main.app_directory_home_10_item.view.*

import java.util.*


class HomeAdapter(val dataList: ArrayList<ServiceMasterLists>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private lateinit var itemClickListener: OnItemClickListener
    private lateinit var context: Context

    interface OnItemClickListener {
        fun onItemClick(view: View, obj: ServiceMasterLists, position: Int)
    }

    fun setOnItemClickListener(mItemClickListener: OnItemClickListener) {
        this.itemClickListener = mItemClickListener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.app_directory_home_10_item, parent, false)

        return PlaceViewHolder(itemView)
    }

    override fun onBindViewHolder(reholder: RecyclerView.ViewHolder, position: Int) {

        if (reholder is PlaceViewHolder) {
            val cats = dataList[position]

            reholder.catNameTextView.text = cats.service_name

            context = reholder.catImageView.context

//            val idCatImage = Utils.getDrawableInt(context, cats.image)
//            Utils.setImageToImageView(context, reholder.catImageView, idCatImage)

            Utils.setCircleImageToImageView(context, reholder.catbackgroundImageView, R.drawable.white_background, 0, 0)


            reholder.catbackgroundImageView.setOnClickListener { view ->

                itemClickListener.onItemClick(view, dataList[position], position)

            }

        }

    }

    override fun getItemCount(): Int {
        return dataList.size
    }

    inner class PlaceViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var catImageView: ImageView = view.catImageView
        var catNameTextView: TextView = view.catNameTextView
        var catbackgroundImageView: ImageView = view.catbackgroundImageView

    }

}
