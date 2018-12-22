package org.wit.hillfort.activities

import android.support.v4.view.GravityCompat
import android.support.v7.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_hillfort_list.*
import org.jetbrains.anko.intentFor
import org.jetbrains.anko.startActivity
import org.jetbrains.anko.startActivityForResult
import org.wit.hillfort.main.MainApp
import org.wit.hillfort.models.HillfortModel

class HillfortListPresenter(val activity: HillfortListActivity) {
    lateinit var app: MainApp

    init{
        app = activity.application as MainApp
    }

    fun getHillforts()=app.hillforts.findAll()

    fun doAddHillfort(){
        activity.startActivityForResult<HillfortActivity>(0)
    }

    fun doEditHillfort(hillfort: HillfortModel){
        activity.startActivityForResult(activity.intentFor<HillfortActivity>().putExtra("hillfort_edit", hillfort),0)
    }

    fun doShowHillfortsMap(){
        activity.startActivity<HillfortMapsActivity>()
    }

    fun doShowSettings(){
        activity.startActivityForResult<SettingsActivity>(0)
    }

    fun doLogout(){
        activity.setResult(AppCompatActivity.RESULT_OK)
        activity.finish()
    }

    fun doHome(){
        activity.drawer.openDrawer(GravityCompat.START)
    }
}