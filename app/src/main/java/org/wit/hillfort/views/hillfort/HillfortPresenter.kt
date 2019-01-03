package org.wit.hillfort.views.hillfort

import android.annotation.SuppressLint
import android.content.Intent
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import kotlinx.android.synthetic.main.activity_hillfort.*
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.async
import org.jetbrains.anko.intentFor
import org.wit.hillfort.R
import org.wit.hillfort.helpers.checkLocationPermissions
import org.wit.hillfort.helpers.isPermissionGranted
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
    var defaultLocation = Location(52.245696, -7.139102, 15f)
    var hillfort = HillfortModel()
    var location = Location(52.245696, -7.139102, 15f)
    var edit = false

    var map: GoogleMap? = null
    var locationService: FusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(view)

    init{
        if (view.intent.hasExtra("hillfort_edit")) {
            edit = true
            hillfort = view.intent.extras.getParcelable<HillfortModel>("hillfort_edit")
            view.showHillfort(hillfort)
            locationUpdate(hillfort.location.lat, hillfort.location.lng)
        } else {
            if (checkLocationPermissions(view)) {
                doSetCurrentLocation()
            }
            //hillfort.location = defaultLocation
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
                async(UI) {
                    app.hillforts.update(hillfort.copy())
                }
            } else {
                async(UI) {
                    app.hillforts.create(hillfort.copy())
                }
            }
            view?.finish()
        }
    }

    fun doCancel() {
        view?.finish()
    }

    fun doDelete(){
        async(UI) {
            app.hillforts.delete(hillfort)
            view?.finish()
        }
    }

    fun doSelectImage(){
        view?.let{
            showImagePicker(view!!, IMAGE_REQUEST)
        }
    }

    fun doShowHillfort(){
        view!!.showHillfort(hillfort)
    }

    fun doSetLocation() {
        view?.navigateTo(VIEW.LOCATION, LOCATION_REQUEST, "location", Location(hillfort.location.lat, hillfort.location.lng, hillfort.location.zoom))
    }

    fun doConfigureMap(m: GoogleMap) {
        map = m
        locationUpdate(hillfort.location.lat, hillfort.location.lng)
    }

    fun locationUpdate(lat: Double, lng: Double) {
        hillfort.location.lat = lat
        hillfort.location.lng = lng
        hillfort.location.zoom = 15f
        map?.clear()
        map?.uiSettings?.setZoomControlsEnabled(true)
        val options = MarkerOptions().title(hillfort.title).position(LatLng(hillfort.location.lat, hillfort.location.lng))
        map?.addMarker(options)
        map?.moveCamera(CameraUpdateFactory.newLatLngZoom(LatLng(hillfort.location.lat, hillfort.location.lng), hillfort.location.zoom))
        view?.showHillfort(hillfort)
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
                    hillfort.location = location.copy()
                    locationUpdate(hillfort.location.lat, hillfort.location.lng)
                }
            }
        }
    }

    override fun doRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        if (isPermissionGranted(requestCode, grantResults)) {
            doSetCurrentLocation()
        } else {
            locationUpdate(defaultLocation.lat, defaultLocation.lng)
        }
    }

    @SuppressLint("MissingPermission")
    fun doSetCurrentLocation() {
        locationService.lastLocation.addOnSuccessListener {
            locationUpdate(it.latitude, it.longitude)
        }
    }

    fun createShareIntent(): Intent{
        val text = "Hillfort: ${hillfort.title}, Description: ${hillfort.description}, Location: ${LatLng(hillfort.location.lat, hillfort.location.lng)}, Rating: ${hillfort.rating}"
        val shareIntent = Intent(Intent.ACTION_SEND)
        shareIntent.type = "text/plain"
        shareIntent.putExtra(Intent.EXTRA_TEXT, text)
        return shareIntent
    }
}