package org.wit.hillfort.models.json

import android.content.Context
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import org.jetbrains.anko.AnkoLogger
import org.wit.hillfort.helpers.*
import org.wit.hillfort.models.HillfortModel
import org.wit.hillfort.models.HillfortStore
import java.util.*
import kotlin.collections.ArrayList

class HillfortJSONStore : HillfortStore, AnkoLogger {

    val context: Context
    var hillforts = mutableListOf<HillfortModel>()
    val JSON_FILE = "hillforts.json"
    val gsonBuilder = GsonBuilder().setPrettyPrinting().create()
    val listType = object : TypeToken<java.util.ArrayList<HillfortModel>>() {}.type

    fun generateRandomId(): Long {
        return Random().nextLong()
    }

    constructor (context: Context) {
        this.context = context
        if (exists(context, JSON_FILE)) {
            deserialize()
        }
    }

    override suspend fun findAll(): MutableList<HillfortModel> {
        return hillforts
    }

    override suspend fun create(hillfort: HillfortModel) {
        hillfort.id = generateRandomId()
        hillforts.add(hillfort)
        serialize()
    }

    override suspend fun update(hillfort: HillfortModel) {
        var foundHillfort: HillfortModel? = hillforts.find { p -> p.id == hillfort.id }
        if (foundHillfort != null) {
            foundHillfort.title = hillfort.title
            foundHillfort.description = hillfort.description
            foundHillfort.visitedCheckbox = hillfort.visitedCheckbox
            foundHillfort.additionalNotes = hillfort.additionalNotes
            foundHillfort.images = ArrayList(hillfort.images)
            foundHillfort.day = hillfort.day
            foundHillfort.month = hillfort.month
            foundHillfort.year = hillfort.year
            foundHillfort.location = hillfort.location
            foundHillfort.favorite = hillfort.favorite
            foundHillfort.rating = hillfort.rating
            serialize()
        }
    }

    override suspend fun delete(hillfort: HillfortModel) {
        hillforts.remove(hillfort)
        serialize()
    }

    private suspend fun serialize() {
        val jsonString = gsonBuilder.toJson(hillforts, listType)
        write(context, JSON_FILE, jsonString)
    }

    private fun deserialize() {
        val jsonString = read(context, JSON_FILE)
        hillforts = Gson().fromJson(jsonString, listType)
    }

    override suspend fun findById(id:Long) : HillfortModel? {
        val foundPlacemark: HillfortModel? = hillforts.find { it.id == id }
        return foundPlacemark
    }

    override fun clear() {
        hillforts.clear()
    }
}