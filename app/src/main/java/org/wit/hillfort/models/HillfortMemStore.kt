package org.wit.hillfort.models

import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.info

var lastHillfortId = 0L



class HillfortMemStore:HillfortStore, AnkoLogger {
    private fun getId(): Long {
        return lastHillfortId++
    }

    val hillforts = ArrayList<HillfortModel>()

    override fun findAll(): List<HillfortModel> {
        return hillforts
    }

    override fun create(hillfort: HillfortModel) {
        hillfort.id = getId()
        hillforts.add(hillfort)
        logAll()
    }

    override fun update(hillfort: HillfortModel) {
        var foundHillfort: HillfortModel? = hillforts.find { p -> p.id == hillfort.id }
        if (foundHillfort != null) {
            foundHillfort.title = hillfort.title
            foundHillfort.description = hillfort.description
            foundHillfort.images = ArrayList(hillfort.images)
            foundHillfort.lat = hillfort.lat
            foundHillfort.lng = hillfort.lng
            foundHillfort.zoom = hillfort.zoom
            logAll()
        }
    }

    override fun delete(hillfort: HillfortModel) {
        hillforts.remove(hillfort)
    }

    fun logAll() {
        hillforts.forEach{ info("${it}") }
    }
}