package org.wit.hillfort.views.hillfort

import android.content.Intent
import kotlinx.android.synthetic.main.activity_hillfort.*
import org.jetbrains.anko.intentFor
import org.wit.hillfort.R
import org.wit.hillfort.helpers.showImagePicker
import org.wit.hillfort.main.MainApp
import org.wit.hillfort.models.HillfortModel
import org.wit.hillfort.models.Location
import org.wit.hillfort.views.BasePresenter
import org.wit.hillfort.views.BaseView
import org.wit.hillfort.views.VIEW
import org.wit.hillfort.views.editlocation.EditLocationView

class HillfortPresenter(view: BaseView) : BasePresenter(view) {

    val IMAGE_REQUEST = 1
    val LOCATION_REQUEST = 2

    var hillfort = HillfortModel()
    var location = Location(52.245696, -7.139102, 15f)
    var edit = false

    init{
        if (view.intent.hasExtra("hillfort_edit")) {
            edit = true
            hillfort = view.intent.extras.getParcelable<HillfortModel>("hillfort_edit")
            view.showHillfort(hillfort)
        }
    }

    fun doAddOrSave(title: String, description: String, additionalNotes: String, visitedCheckbox: Boolean, dayOfMonth: Int, month: Int, year: Int, favorite: Boolean, rating: Float){
        hillfort.title = title
        hillfort.description = description
        hillfort.additionalNotes = additionalNotes
        hillfort.visitedCheckbox = visitedCheckbox
        hillfort.day = dayOfMonth
        hillfort.month = month
        hillfort.year = year
        hillfort.favorite = favorite
        hillfort.rating = rating
        if (hillfort.title.isNotEmpty()) {
            if (edit) {
                app.hillforts.update(hillfort.copy())
            } else {
                app.hillforts.create(hillfort.copy())
            }
            view?.finish()
        }
    }

    fun doCancel() {
        view?.finish()
    }

    fun doDelete(){
        app.hillforts.delete(hillfort)
        view?.finish()
    }

    fun doSelectImage(){
        view?.let{
            showImagePicker(view!!, IMAGE_REQUEST)
        }
    }

    fun doShowHillfort(){
        view!!.showHillfort(hillfort)
    }

    fun doSetLocation(){
        if (edit == false) {
            view?.navigateTo(VIEW.LOCATION, LOCATION_REQUEST, "location", location)
        } else {
            view?.navigateTo(VIEW.LOCATION, LOCATION_REQUEST, "location", Location(hillfort.lat, hillfort.lng, hillfort.zoom))
        }
        /*
        if (hillfort.zoom != 0f) {
            location.lat =  hillfort.lat
            location.lng = hillfort.lng
            location.zoom = hillfort.zoom
        }
        activity.startActivityForResult(activity.intentFor<EditLocationView>().putExtra("location", location), LOCATION_REQUEST)
        */
    }

    override fun doActivityResult(requestCode: Int, resultCode: Int, data: Intent) {
        when (requestCode) {
            IMAGE_REQUEST -> {
                if (data != null) {
                    if (!hillfort.images.contains(data.data.toString())) hillfort.images.add(data.getData().toString())
                    //activity.loadImages()
                    view!!.chooseImage.setText(R.string.change_hillfort_image)
                    view!!.showHillfort(hillfort)
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