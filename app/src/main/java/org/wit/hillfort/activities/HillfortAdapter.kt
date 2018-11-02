package org.wit.hillfort.activities

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.card_hillfort.view.*
import org.wit.hillfort.R
import org.wit.hillfort.helpers.readImageFromPath
import org.wit.hillfort.models.HillfortModel

interface HillfortListener {
    fun onHillfortClick(placemark: HillfortModel)
}

class HillfortAdapter constructor(private var hillforts: List<HillfortModel>, private val listener:HillfortListener) : RecyclerView.Adapter<HillfortAdapter.MainHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, p1: Int): MainHolder {
        return MainHolder(LayoutInflater.from(parent?.context).inflate(R.layout.card_hillfort, parent, false))
    }

    override fun onBindViewHolder(holder: MainHolder, position: Int) {
        val hillfort = hillforts[holder.adapterPosition]
        holder.bind(hillfort, listener)
    }

    override fun getItemCount(): Int = hillforts.size

    class MainHolder constructor(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bind(hillfort: HillfortModel, listener:HillfortListener) {
            itemView.hillfortTitle.text = hillfort.title
            itemView.description.text = hillfort.description
            if (hillfort.images.isNotEmpty()) itemView.imageIcon.setImageBitmap(readImageFromPath(itemView.context, hillfort.images.first()))
            itemView.setOnClickListener { listener.onHillfortClick(hillfort) }
        }
    }
}


class HillfortImageAdapter constructor(private var images: List<String>) : RecyclerView.Adapter<HillfortImageAdapter.MainHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, p1: Int): MainHolder {
        return MainHolder(LayoutInflater.from(parent?.context).inflate(R.layout.image_item, parent, false))
    }

    override fun onBindViewHolder(holder: MainHolder, position: Int) {

        val image = images[position]
        val imageView = holder.imageView
        Picasso.get()
            .load(image)
            .fit()
            .into(imageView)
    }

    override fun getItemCount(): Int = images.size

    class MainHolder constructor(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var imageView: ImageView = itemView.findViewById(R.id.iv_image)
    }
}