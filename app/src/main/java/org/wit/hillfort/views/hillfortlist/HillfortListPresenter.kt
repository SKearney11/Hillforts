package org.wit.hillfort.views.hillfortlist

import android.support.v4.view.GravityCompat
import android.support.v7.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_hillfort_list.*
import org.jetbrains.anko.intentFor
import org.jetbrains.anko.startActivity
import org.jetbrains.anko.startActivityForResult
import org.wit.hillfort.main.MainApp
import org.wit.hillfort.models.HillfortModel
import org.wit.hillfort.views.BasePresenter
import org.wit.hillfort.views.map.HillfortMapView
import org.wit.hillfort.views.SettingsView
import org.wit.hillfort.views.hillfort.HillfortView

class HillfortListPresenter(val activity: HillfortListView): BasePresenter(activity) {

    init{
        app = activity.application as MainApp
    }

    fun getHillforts()=app.hillforts.findAll()

    fun doAddHillfort(){
        activity.startActivityForResult<HillfortView>(0)
    }

    fun doEditHillfort(hillfort: HillfortModel){
        activity.startActivityForResult(activity.intentFor<HillfortView>().putExtra("hillfort_edit", hillfort),0)
    }

    fun doShowHillfortsMap(){
        activity.startActivity<HillfortMapView>()
    }

    fun doShowSettings(){
        activity.startActivityForResult<SettingsView>(0)
    }

    fun doLogout(){
        activity.setResult(AppCompatActivity.RESULT_OK)
        activity.finish()
    }

    fun doHome(){
        activity.drawer.openDrawer(GravityCompat.START)
    }

    fun doShowFavorites(checked: Boolean){
        activity.showHillforts(if (checked) app.hillforts.findAll().filter { it.favorite } else app.hillforts.findAll())
    }
}