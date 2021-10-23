package com.example.linah_alkhurayyif_flickrbrowserapp

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.item_row.view.*

class ImageRVAdapter(val activity: MainActivity, val images: ArrayList<FlickrImage>): RecyclerView.Adapter<ImageRVAdapter.MyViewHolder>() {
    class MyViewHolder(itemView: View):RecyclerView.ViewHolder(itemView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(
            R.layout.item_row,
            parent,
            false
        )
        return MyViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val image=images[position]
        holder.itemView.apply{
            item_textView.text = image.titleImage
            Glide.with(activity).load(image.linkImage).into(item_imageView)
            cardView.setOnClickListener { activity.openImg(image.linkImage) }
        }

    }

    override fun getItemCount()=images.size
}