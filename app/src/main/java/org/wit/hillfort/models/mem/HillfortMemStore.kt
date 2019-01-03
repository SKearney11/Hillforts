package org.wit.hillfort.models.mem

import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.info
import org.wit.hillfort.models.HillfortModel
import org.wit.hillfort.models.HillfortStore

var lastHillfortId = 0L



class HillfortMemStore: HillfortStore, AnkoLogger {
    private fun getId(): Long {
        return lastHillfortId++
    }

    val hillforts = ArrayList<HillfortModel>()

    suspend override fun findAll(): MutableList<HillfortModel> {
        return hillforts
    }

    suspend override fun create(hillfort: HillfortModel) {
        hillfort.id = getId()
        hillforts.add(hillfort)
        logAll()
    }

    suspend override fun update(hillfort: HillfortModel) {
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

    suspend override fun delete(hillfort: HillfortModel) {
        hillforts.remove(hillfort)
    }

    fun logAll() {
        hillforts.forEach{ info("${it}") }
    }

    suspend override fun findById(id:Long) : HillfortModel? {
        val foundPlacemark: HillfortModel? = hillforts.find { it.id == id }
        return foundPlacemark
    }
}