package org.wit.hillfort.main

import android.app.Application
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.info
import org.wit.hillfort.models.HillfortStore
import org.wit.hillfort.models.UserJSONStore
import org.wit.hillfort.models.UserModel
import org.wit.hillfort.models.UserStore
import org.wit.hillfort.models.firebase.HillfortFireStore

class MainApp : Application(), AnkoLogger {

    lateinit var hillforts: HillfortStore
    lateinit var users: UserStore
    lateinit var currentUser: UserModel

    override fun onCreate() {
        super.onCreate()
        hillforts = HillfortFireStore(applicationContext)
        users = UserJSONStore(applicationContext)
        info("Hillfort started")
    }
}