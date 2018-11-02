package org.wit.hillfort.main

import android.app.Application
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.info
import org.wit.hillfort.models.*

class MainApp : Application(), AnkoLogger {

    //val hillforts = ArrayList<HillfortModel>()
    lateinit var hillforts: HillfortStore
    lateinit var users: UserStore

    override fun onCreate() {
        super.onCreate()
        hillforts = HillfortJSONStore(applicationContext)
        users = UserJSONStore(applicationContext)
        info("Hillfort started")
    }
}