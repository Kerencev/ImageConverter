package com.kerencev.imageconverter.view

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.kerencev.imageconverter.R
import com.kerencev.imageconverter.utils.load

interface OnItemClick {
    fun onClick(path: String)
}

class GalleryAdapter(private val onItemClick: OnItemClick) :
    RecyclerView.Adapter<GalleryAdapter.GalleryViewHolder>() {

    private val data = ArrayList<String>()

    fun setData(listOfPhotos: List<String>) {
        data.clear()
        data.addAll(listOfPhotos)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GalleryViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_gallery, parent, false)
        return GalleryViewHolder(view)
    }

    override fun onBindViewHolder(holder: GalleryViewHolder, position: Int) {
        holder.bind(data[position])
    }

    override fun getItemCount() = data.size

    inner class GalleryViewHolder(private val view: View) : RecyclerView.ViewHolder(view) {

        private val imgPhoto = view.findViewById<ImageView>(R.id.img)

        fun bind(path: String) {
            imgPhoto.load(path)
            view.setOnClickListener {
                onItemClick.onClick(path)
            }
        }
    }
}