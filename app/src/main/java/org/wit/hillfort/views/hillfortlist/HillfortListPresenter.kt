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
import org.wit.hillfort.views.BaseView
import org.wit.hillfort.views.map.HillfortMapView
import org.wit.hillfort.views.SettingsView
import org.wit.hillfort.views.hillfort.HillfortView

class HillfortListPresenter(view: BaseView) : BasePresenter(view) {

    init{
        app = view.application as MainApp
    }

    fun getHillforts()=app.hillforts.findAll()

    fun doAddHillfort(){
        view!!.startActivityForResult<HillfortView>(0)
    }

    fun doEditHillfort(hillfort: HillfortModel){
        view!!.startActivityForResult(view!!.intentFor<HillfortView>().putExtra("hillfort_edit", hillfort),0)
    }

    fun doShowHillfortsMap(){
        view!!.startActivity<HillfortMapView>()
    }

    fun doShowSettings(){
        view!!.startActivityForResult<SettingsView>(0)
    }

    fun doLogout(){
        view!!.setResult(AppCompatActivity.RESULT_OK)
        view!!.finish()
    }

    fun doHome(){
        view!!.drawer.openDrawer(GravityCompat.START)
    }

    fun doShowFavorites(checked: Boolean){
        view!!.showHillforts(if (checked) app.hillforts.findAll().filter { it.favorite } else app.hillforts.findAll())
    }
}