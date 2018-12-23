package org.wit.hillfort.views.map

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import kotlinx.android.synthetic.main.content_hillfort_maps.*
import org.wit.hillfort.helpers.readImageFromPath
import org.wit.hillfort.main.MainApp

class HillfortMapPresenter(val view: HillfortMapView){

    var app: MainApp

    init{
        app = view.application as MainApp
    }

    fun doPopulateMap(map: GoogleMap){
        map.uiSettings.setZoomControlsEnabled(true)
        map.setOnMarkerClickListener(view)
        app.hillforts.findAll().forEach {
            val loc = LatLng(it.lat, it.lng)
            val options = MarkerOptions().title(it.title).position(loc)
            map.addMarker(options).tag = it.id
            map.moveCamera(CameraUpdateFactory.newLatLngZoom(loc, it.zoom))
        }
    }

    fun doMarkerSelected(marker: Marker){
        val tag = marker.tag as Long
        val hillfort = app.hillforts.findById(tag)
        view.currentTitle.text = marker.title
        view.currentDescription.text = hillfort!!.description
        if (hillfort.images.isNotEmpty()){
            view.imageView3.setImageBitmap(readImageFromPath(view.applicationContext, hillfort.images.first()))
        }else{
            view.imageView3.setImageBitmap(null)
        }
    }
}