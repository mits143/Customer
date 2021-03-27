package com.otgs.customerapp.adapter

import android.content.Context
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.otgs.customerapp.R


class ImagesAdapter(
    internal var context: Context,
    val dataList: ArrayList<Uri>
) :
    RecyclerView.Adapter<ImagesAdapter.ViewHolder>() {

    //this method is returning the view for each item in the list
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ImagesAdapter.ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.cell_image, parent, false)
        return ViewHolder(v)
    }

    //this method is binding the data on the list
    override fun onBindViewHolder(holder: ImagesAdapter.ViewHolder, position: Int) {
        holder.bindItems(context, dataList[position])
    }

    //this method is giving the size of the list
    override fun getItemCount(): Int {
        return dataList.size
    }

    //the class is hodling the list view
    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bindItems(context: Context, data: Uri) {
            val imageView = itemView.findViewById(R.id.imageView) as ImageView
            val imgClose = itemView.findViewById(R.id.imgClose) as ImageView

            var requestOptions = RequestOptions()
            requestOptions = requestOptions.transforms(CenterCrop(), RoundedCorners(20))
            Glide.with(context).load(data).apply(requestOptions).into(imageView)

            imgClose.setOnClickListener {
                removeItem(adapterPosition)
            }
        }
    }

    fun removeItem(position: Int) {
        dataList.removeAt(position)
        notifyItemRemoved(position)
        notifyItemRangeChanged(position, dataList.size)
    }
}