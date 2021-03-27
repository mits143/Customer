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
import com.otgs.customerapp.model.response.GarageInformationData
import com.otgs.customerapp.utils.Utils
import kotlinx.android.synthetic.main.cell_garage.view.*
import java.util.*



class GarageAdapter(val dataList: ArrayList<GarageInformationData>) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private lateinit var itemClickListener: OnItemClickListener
    private lateinit var context: Context

    interface OnItemClickListener {
        fun onItemClick(view: View, obj: GarageInformationData, position: Int)
    }

    fun setOnItemClickListener(mItemClickListener: OnItemClickListener) {
        this.itemClickListener = mItemClickListener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val itemView =
            LayoutInflater.from(parent.context).inflate(R.layout.cell_garage, parent, false)

        return PlaceViewHolder(itemView)
    }

    override fun onBindViewHolder(reholder: RecyclerView.ViewHolder, position: Int) {

        if (reholder is PlaceViewHolder) {
            val cats = dataList[position]

            reholder.txtName.text = cats.garage_name
            reholder.txtAddress.text = cats.contact_address
            reholder.txtDistance.text = cats.distance_from_current_location
            context = reholder.imgView.context
            Utils.setImageToImageView(
                context,
                reholder.imgView,
                R.drawable.white_background
            )

            context = reholder.txtLocation.context
            reholder.txtLocation.setOnClickListener { view ->
//                val gmmIntentUri = Uri.parse("geo:" +  cats.lat + "," + cats.lng)
//                val mapIntent = Intent(Intent.ACTION_VIEW, gmmIntentUri)
//                mapIntent.setPackage("com.google.android.apps.maps")
//                mapIntent.resolveActivity(context.packageManager)?.let {
//                    context.startActivity(mapIntent)
//                }
                val builder = Uri.Builder()
                builder.scheme("https")
                    .authority("www.google.com")
                    .appendPath("maps")
                    .appendPath("dir")
                    .appendPath("")
                    .appendQueryParameter("api", "1")
                    .appendQueryParameter("destination", cats.lat + "," + cats.lng)
                val url = builder.build().toString()
                val intent = Intent(Intent.ACTION_VIEW)
                intent.data = Uri.parse(url)
                context.startActivity(intent)
            }

            reholder.itemView.setOnClickListener { view ->

                itemClickListener.onItemClick(view, dataList[position], position)

            }

        }

    }

    override fun getItemCount(): Int {
        return dataList.size
    }

    inner class PlaceViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var imgView: ImageView = view.imgView
        var txtName: TextView = view.txtName
        var txtAddress: TextView = view.txtAddress
        var txtDistance: TextView = view.txtDistance
        var txtLocation: TextView = view.txtLocation

    }

}
