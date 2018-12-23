package org.wit.hillfort.views.hillfort

import android.content.Intent
import kotlinx.android.synthetic.main.activity_hillfort.*
import org.jetbrains.anko.intentFor
import org.wit.hillfort.R
import org.wit.hillfort.helpers.showImagePicker
import org.wit.hillfort.main.MainApp
import org.wit.hillfort.models.HillfortModel
import org.wit.hillfort.models.Location
import org.wit.hillfort.views.editlocation.EditLocationView

class HillfortPresenter(val activity: HillfortView) {

    val IMAGE_REQUEST = 1
    val LOCATION_REQUEST = 2

    var hillfort = HillfortModel()
    var location = Location(52.245696, -7.139102, 15f)
    lateinit var app : MainApp
    var edit = false

    init{
        app = activity.application as MainApp
        if (activity.intent.hasExtra("hillfort_edit")) {
            edit = true
            hillfort = activity.intent.extras.getParcelable<HillfortModel>("hillfort_edit")
            activity.showHillfort(hillfort)
        }
    }

    fun doAddOrSave(title: String, description: String, additionalNotes: String, visitedCheckbox: Boolean, dayOfMonth: Int, month: Int, year: Int){
        hillfort.title = title
        hillfort.description = description
        hillfort.additionalNotes = additionalNotes
        hillfort.visitedCheckbox = visitedCheckbox
        hillfort.day = dayOfMonth
        hillfort.month = month
        hillfort.year = year
        if (hillfort.title.isNotEmpty()) {
            if (edit) {
                app.hillforts.update(hillfort.copy())
            } else {
                app.hillforts.create(hillfort.copy())
            }
            activity.finish()
        }
    }

    fun doCancel() {
        activity.finish()
    }

    fun doDelete(){
        app.hillforts.delete(hillfort)
        activity.finish()
    }

    fun doSelectImage(){
        showImagePicker(activity, IMAGE_REQUEST)
    }

    fun doSetLocation(){
        if (hillfort.zoom != 0f) {
            location.lat =  hillfort.lat
            location.lng = hillfort.lng
            location.zoom = hillfort.zoom
        }
        activity.startActivityForResult(activity.intentFor<EditLocationView>().putExtra("location", location), LOCATION_REQUEST)
    }

    fun doActivityResult(requestCode: Int, resultCode: Int, data: Intent){
        when (requestCode) {
            IMAGE_REQUEST -> {
                if (data != null) {
                    hillfort.images.add(data.getData().toString())
                    activity.loadImages()
                    activity.chooseImage.setText(R.string.change_hillfort_image)
                }
            }
            LOCATION_REQUEST -> {
                if (data != null) {
                    val location = data.extras.getParcelable<Location>("location")
                    hillfort.lat = location.lat
                    hillfort.lng = location.lng
                    hillfort.zoom = location.zoom
                }
            }
        }
    }
}