package org.wit.hillfort.models

import android.nfc.Tag

interface HillfortStore {
    suspend fun findAll(): MutableList<HillfortModel>
    suspend fun create(hillfort: HillfortModel)
    suspend fun update(hillfort:HillfortModel)
    suspend fun delete(hillfort: HillfortModel)
    suspend fun findById(id:Long) : HillfortModel?
    fun clear()
}