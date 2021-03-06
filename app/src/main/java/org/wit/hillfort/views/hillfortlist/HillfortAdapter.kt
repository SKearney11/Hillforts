package org.wit.hillfort.views.hillfortlist

import android.app.Activity
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import com.bumptech.glide.Glide
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.card_hillfort.view.*
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.info
import org.wit.hillfort.R
import org.wit.hillfort.helpers.readImageFromPath
import org.wit.hillfort.models.HillfortModel

interface HillfortListener {
    fun onHillfortClick(placemark: HillfortModel)
}

class HillfortAdapter constructor(private var hillforts: List<HillfortModel>, private val listener: HillfortListener) : androidx.recyclerview.widget.RecyclerView.Adapter<HillfortAdapter.MainHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, p1: Int): MainHolder {
        return MainHolder(
            LayoutInflater.from(parent?.context).inflate(
                R.layout.card_hillfort,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: MainHolder, position: Int) {
        val hillfort = hillforts[holder.adapterPosition]
        holder.bind(hillfort, listener)
    }

    override fun getItemCount(): Int = hillforts.size

    class MainHolder constructor(itemView: View) : androidx.recyclerview.widget.RecyclerView.ViewHolder(itemView) {

        fun bind(hillfort: HillfortModel, listener: HillfortListener) {
            itemView.hillfortTitle.text = hillfort.title
            itemView.description.text = hillfort.description
            if (hillfort.images.isNotEmpty()) Glide.with(itemView.context).load(hillfort.images.first()).into(itemView.imageIcon)
            itemView.setOnClickListener { listener.onHillfortClick(hillfort) }
        }
    }
}

class HillfortImageAdapter constructor(private var images: List<String>) : androidx.recyclerview.widget.RecyclerView.Adapter<HillfortImageAdapter.MainHolder>(), AnkoLogger{

    override fun onCreateViewHolder(parent: ViewGroup, p1: Int): MainHolder {
        return MainHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.image_item,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: MainHolder, position: Int) {
        val image = images[position]
        holder.bind(image)
    }

    override fun getItemCount(): Int = images.size

    class MainHolder constructor(itemView: View) : androidx.recyclerview.widget.RecyclerView.ViewHolder(itemView), AnkoLogger {

        fun bind(image: String){
            var imageView: ImageView = itemView.findViewById(R.id.iv_image)
            Glide.with(itemView.context).load(image).into(imageView)
        }
    }
}